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

public class NotifSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Notifs_Database.db";
    private static String DATABASE_PATH = "/data/data/com.zezoo.ElectronicInstitute/databases/";
    private static final String DATABASE_EX_PATH = "/ElectronicInstitute/Databases/";
    private static final String TABLE_NAME = "Notifs";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_ID= "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TYPEN = "typeN";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_STUDY = "study";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOWNLOAD = "download";
    private static final String KEY_ISNEW = "isnew";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";

    private static final String[] COLUMNS = {KEY_COLUMN,KEY_ID,KEY_TYPEN,KEY_TYPE, KEY_STUDY, KEY_SUBJECT, KEY_NAME, KEY_DOWNLOAD, KEY_ISNEW, KEY_TIME, KEY_DATE};

    SQLiteDatabase db;
    Context context;

    String CREATION_TABLE = "CREATE TABLE Notifs ( "
            + "colum INTEGER PRIMARY KEY AUTOINCREMENT, " + "id INTEGER, " + "typeN INTEGER, " + "type INTEGER, "+ "study INTEGER, "+ "subject INTEGER, " + "name TEXT, "+ "download INTEGER, "+ "isnew INTEGER, " + "time TEXT," + "date TEXT )";

    public NotifSQLDatabaseHandler(Context context) {
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

    public void deleteNotif(NotifListChildItem Notif) {
        // Get reference to writable DB
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(Notif.getColumn())});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Notifs.db");
        } catch (Exception e) {

        }
    }

    public NotifListChildItem getNotif(int Column) {
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

        NotifListChildItem Notif = new NotifListChildItem();
        Notif.setColumn(cursor.getInt(0));
        Notif.setId(cursor.getInt(1));
        Notif.setTypeN(cursor.getInt(2));
        Notif.setType(cursor.getInt(3));
        Notif.setStudy(cursor.getInt(4));
        Notif.setSubject(cursor.getInt(5));
        Notif.setName(cursor.getString(6));
        Notif.setDownload(cursor.getInt(7));
        Notif.setNew(cursor.getInt(8) > 0);
        Notif.setTime(cursor.getString(9));
        Notif.setDate(cursor.getString(10));
        return Notif;
    }
    public List<NotifListChildItem> allNotifs() {

        List<NotifListChildItem> Notifs = new ArrayList<NotifListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        NotifListChildItem Notif = null;

        if (cursor.moveToFirst()) {
            do {
                    Notif = new NotifListChildItem();
                    Notif.setColumn(cursor.getInt(0));
                    Notif.setId(cursor.getInt(1));
                    Notif.setTypeN(cursor.getInt(2));
                    Notif.setType(cursor.getInt(3));
                    Notif.setStudy(cursor.getInt(4));
                    Notif.setSubject(cursor.getInt(5));
                    Notif.setName(cursor.getString(6));
                    Notif.setDownload(cursor.getInt(7));
                    Notif.setNew(cursor.getInt(8) > 0);
                    Notif.setTime(cursor.getString(9));
                    Notif.setDate(cursor.getString(10));
                    Notifs.add(Notif);
            } while (cursor.moveToNext());
        }
        return Notifs;
    }
    
    public List<NotifListChildItem> allNotifs(int type,int study) {

        List<NotifListChildItem> Notifs = new ArrayList<NotifListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        NotifListChildItem Notif = null;

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(3) == type && cursor.getInt(4) == study){
                        Notif = new NotifListChildItem();
                        Notif.setColumn(cursor.getInt(0));
                        Notif.setId(cursor.getInt(1));
                        Notif.setTypeN(cursor.getInt(2));
                        Notif.setType(cursor.getInt(3));
                        Notif.setStudy(cursor.getInt(4));
                        Notif.setSubject(cursor.getInt(5));
                        Notif.setName(cursor.getString(6));
                        Notif.setDownload(cursor.getInt(7));
                        Notif.setNew(cursor.getInt(8) > 0);
                        Notif.setTime(cursor.getString(9));
                        Notif.setDate(cursor.getString(10));
                        Notifs.add(Notif);
                    }
            } while (cursor.moveToNext());
        }
        return Notifs;
    }

    public List<String> allSubjects(int type,int study) {
       
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, NotifListChildItem> NotifsWithoutGroup = new HashMap<Integer, NotifListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        NotifListChildItem Notif = null;
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
    public List<String> allTypes(int type,int study) {
       
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, NotifListChildItem> NotifsWithoutGroup = new HashMap<Integer, NotifListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        NotifListChildItem Notif = new NotifListChildItem();
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(3) == type && cursor.getInt(4) == study) {
                    folder = ElecUtils.getNamefromTypeN(cursor.getInt(2));
                    if (!folders.contains(folder)) {
                        folders.add(folder);
                    }
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }
    public List<String> allDates(int type,int study) {
       
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, NotifListChildItem> NotifsWithoutGroup = new HashMap<Integer, NotifListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        NotifListChildItem Notif = null;
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

    public void addNotif(NotifListChildItem Notif) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Notif.getColumn());
        values.put(KEY_ID, Notif.getId());
        values.put(KEY_TYPE, Notif.getType());
        values.put(KEY_TYPEN, Notif.getTypeN());
        values.put(KEY_SUBJECT, Notif.getSubject());
        values.put(KEY_STUDY, Notif.getStudy());
        values.put(KEY_NAME, Notif.getName());
        values.put(KEY_DOWNLOAD, Notif.getDownload());
        values.put(KEY_ISNEW, (Notif.isNew()) ? 1 : 0);
        values.put(KEY_TIME, Notif.getTime());
        values.put(KEY_DATE, Notif.getDate());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Notifs.db");
        } catch (Exception e) {

        }
    }

    public int updateNotif(NotifListChildItem Notif) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Notif.getColumn());
        values.put(KEY_ID, Notif.getId());
        values.put(KEY_TYPE, Notif.getType());
        values.put(KEY_TYPEN, Notif.getTypeN());
        values.put(KEY_SUBJECT, Notif.getSubject());
        values.put(KEY_STUDY, Notif.getStudy());
        values.put(KEY_NAME, Notif.getName());
        values.put(KEY_DOWNLOAD, Notif.getDownload());
        values.put(KEY_ISNEW, (Notif.isNew()) ? 1 : 0);
        values.put(KEY_TIME, Notif.getTime());
        values.put(KEY_DATE, Notif.getDate());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(Notif.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Notifs.db");
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
