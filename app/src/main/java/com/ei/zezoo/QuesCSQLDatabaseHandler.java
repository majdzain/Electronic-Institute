package com.ei.zezoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuesCSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QuessR_Database.db";
    private static String DATABASE_PATH = "/data/data/com.zezoo.ElectronicInstitute/databases/";
    private static final String DATABASE_EX_PATH = "/ElectronicInstitute/Databases/";
    private static final String TABLE_NAME = "QuessR";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_NUMLIKE = "numlike";
    private static final String KEY_NUMDIS = "numdis";
    private static final String KEY_IDG = "idg";
    private static final String KEY_ID= "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER = "user";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_ISTEACHER = "isteacher";
    private static final String KEY_ISLIKE = "islike";
    private static final String KEY_ISDIS = "isdis";

    private static final String[] COLUMNS = {KEY_COLUMN,KEY_NUMLIKE, KEY_NUMDIS, KEY_IDG, KEY_ID, KEY_NAME, KEY_USER, KEY_TEXT, KEY_TIME, KEY_DATE, KEY_ISTEACHER,KEY_ISLIKE,KEY_ISDIS};

    SQLiteDatabase db;
    Context context;

    String CREATION_TABLE = "CREATE TABLE QuessR ( "
            + "colum INTEGER PRIMARY KEY AUTOINCREMENT, "   + "numlike INTEGER, "+ "numdis INTEGER, "+ "idg TEXT, " + "id TEXT, "+ "name TEXT, "+ "user TEXT, "+ "text TEXT, " + "time TEXT," + "date TEXT,"+ "isteacher INTEGER,"+ "islike INTEGER,"+ "isdis INTEGER )";

    public QuesCSQLDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
// you can implement here migration process
        db.execSQL(CREATION_TABLE);
        this.onCreate(db);
    }

    public void deleteQues(QuesListChildItem Ques) {
        // Get reference to writable DB
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(Ques.getColumn())});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "QuessR.db");
        } catch (Exception e) {

        }
    }

    public QuesListChildItem getQues(int Column) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " colum = ?", // c. selections
                new String[]{String.valueOf(Column)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        QuesListChildItem Ques = new QuesListChildItem();
        Ques.setColumn(cursor.getInt(0));
        Ques.setNumLike(cursor.getInt(1));
        Ques.setNumDis(cursor.getInt(2));
        Ques.setIdG(cursor.getString(3));
        Ques.setId(cursor.getString(4));
        Ques.setName(cursor.getString(5));
        Ques.setUser(cursor.getString(6));
        Ques.setText(cursor.getString(7));
        Ques.setTime(cursor.getString(8));
        Ques.setDate(cursor.getString(9));
        Ques.setTeacher(cursor.getInt(10) > 0);
        Ques.setLike(cursor.getInt(11) > 0);
        Ques.setDis(cursor.getInt(12) > 0);
        return Ques;
    }
    public List<QuesListChildItem> allQuess() {

        List<QuesListChildItem> Quess = new ArrayList<QuesListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        QuesListChildItem Ques = null;

        if (cursor.moveToFirst()) {
            do {
                Ques = new QuesListChildItem();
                Ques.setColumn(cursor.getInt(0));
                Ques.setNumLike(cursor.getInt(1));
                Ques.setNumDis(cursor.getInt(2));
                Ques.setIdG(cursor.getString(3));
                Ques.setId(cursor.getString(4));
                Ques.setName(cursor.getString(5));
                Ques.setUser(cursor.getString(6));
                Ques.setText(cursor.getString(7));
                Ques.setTime(cursor.getString(8));
                Ques.setDate(cursor.getString(9));
                Ques.setTeacher(cursor.getInt(10) > 0);
                Ques.setLike(cursor.getInt(11) > 0);
                Ques.setDis(cursor.getInt(12) > 0);
                Quess.add(Ques);
            } while (cursor.moveToNext());
        }
        return Quess;
    }

    public List<QuesListChildItem> allQuess(String id) {

        List<QuesListChildItem> Quess = new ArrayList<QuesListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        QuesListChildItem Ques = null;

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(3).matches(id)){
                    Ques = new QuesListChildItem();
                    Ques.setColumn(cursor.getInt(0));
                    Ques.setNumLike(cursor.getInt(1));
                    Ques.setNumDis(cursor.getInt(2));
                    Ques.setIdG(cursor.getString(3));
                    Ques.setId(cursor.getString(4));
                    Ques.setName(cursor.getString(5));
                    Ques.setUser(cursor.getString(6));
                    Ques.setText(cursor.getString(7));
                    Ques.setTime(cursor.getString(8));
                    Ques.setDate(cursor.getString(9));
                    Ques.setTeacher(cursor.getInt(10) > 0);
                    Ques.setLike(cursor.getInt(11) > 0);
                    Ques.setDis(cursor.getInt(12) > 0);
                    Quess.add(Ques);
                }
            } while (cursor.moveToNext());
        }
        return Quess;
    }

    public List<String> allSubjects(int type,int study) {

        List<String> folders = new ArrayList<String>();
        HashMap<Integer, QuesListChildItem> QuessWithoutGroup = new HashMap<Integer, QuesListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        QuesListChildItem Ques = null;
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(3) == type && cursor.getInt(4) == study && (cursor.getInt(2) == 0 || cursor.getInt(2) == 1)) {
                    folder = ElecUtils.getSubject(cursor.getInt(3),cursor.getInt(4),cursor.getInt(5));
                    if (!folders.contains(folder)) {
                        folders.add(folder);
                    }
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }
    public boolean isLiked(String idG,String id) {
        HashMap<Integer, QuesListChildItem> QuessWithoutGroup = new HashMap<Integer, QuesListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        boolean is = false;
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(3).matches(idG) && cursor.getString(4).matches(id)) {
                    is = (cursor.getInt(11) > 0);
                }
            } while (cursor.moveToNext());
        }
        return is;
    }
    public boolean isDisLiked(String idG,String id) {
        HashMap<Integer, QuesListChildItem> QuessWithoutGroup = new HashMap<Integer, QuesListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        boolean is = false;
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(3).matches(idG) && cursor.getString(4).matches(id)) {
                    is = (cursor.getInt(12) > 0);
                }
            } while (cursor.moveToNext());
        }
        return is;
    }
    public List<String> allDates(int type,int study) {

        List<String> folders = new ArrayList<String>();
        HashMap<Integer, QuesListChildItem> QuessWithoutGroup = new HashMap<Integer, QuesListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        QuesListChildItem Ques = null;
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(3) == type && cursor.getInt(4) == study) {
                    folder = cursor.getString(10);
                    if (!folders.contains(folder)) {
                        folders.add(folder);
                    }
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }

    public void addQues(QuesListChildItem Ques) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Ques.getColumn());
        values.put(KEY_NUMLIKE, Ques.getNumLike());
        values.put(KEY_NUMDIS, Ques.getNumDis());
        values.put(KEY_IDG, Ques.getIdG());
        values.put(KEY_ID, Ques.getId());
        values.put(KEY_NAME, Ques.getName());
        values.put(KEY_USER, Ques.getUser());
        values.put(KEY_TEXT, Ques.getText());
        values.put(KEY_TIME, Ques.getTime());
        values.put(KEY_DATE, Ques.getDate());
        values.put(KEY_ISTEACHER, (Ques.isTeacher()) ? 1 : 0);
        values.put(KEY_ISLIKE, (Ques.isLike()) ? 1 : 0);
        values.put(KEY_ISDIS, (Ques.isDis()) ? 1 : 0);
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "QuessR.db");
        } catch (Exception e) {

        }
    }

    public int updateQues(QuesListChildItem Ques) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Ques.getColumn());
        values.put(KEY_NUMLIKE, Ques.getNumLike());
        values.put(KEY_NUMDIS, Ques.getNumDis());
        values.put(KEY_IDG, Ques.getIdG());
        values.put(KEY_ID, Ques.getId());
        values.put(KEY_NAME, Ques.getName());
        values.put(KEY_USER, Ques.getUser());
        values.put(KEY_TEXT, Ques.getText());
        values.put(KEY_TIME, Ques.getTime());
        values.put(KEY_DATE, Ques.getDate());
        values.put(KEY_ISTEACHER, (Ques.isTeacher()) ? 1 : 0);
        values.put(KEY_ISLIKE, (Ques.isLike()) ? 1 : 0);
        values.put(KEY_ISDIS, (Ques.isDis()) ? 1 : 0);
        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(Ques.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "QuessR.db");
        } catch (Exception e) {

        }
        return i;
    }

    public void backup(String outFileName) throws IOException {
        //database path
        final String inFileName = context.getDatabasePath(DATABASE_NAME).toString();
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);
        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        // Transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        // Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    public void importDB(String inFileName) throws IOException {
        final String outFileName = context.getDatabasePath(DATABASE_NAME).toString();
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);
        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        // Transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        // Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }
    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
}
