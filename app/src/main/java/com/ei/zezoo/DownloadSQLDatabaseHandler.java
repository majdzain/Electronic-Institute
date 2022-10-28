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

public class DownloadSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Downloads_Database.db";
    private static String DATABASE_PATH = "/data/data/com.zezoo.ElectronicInstitute/databases/";
    private static final String DATABASE_EX_PATH = "/ElectronicInstitute/Databases/";
    private static final String TABLE_NAME = "Downloads";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_ID= "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER = "user";
    private static final String KEY_DOWNLOAD = "download";
    private static final String KEY_PROGRESS= "progress";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPE = "type";
    private static final String KEY_STUDY = "study";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_LESSON = "lesson";

    private static final String[] COLUMNS = {KEY_COLUMN,KEY_ID,KEY_TITLE, KEY_NAME, KEY_USER, KEY_DOWNLOAD, KEY_PROGRESS, KEY_TIME, KEY_DATE, KEY_TYPE, KEY_STUDY, KEY_SUBJECT, KEY_LESSON};

    SQLiteDatabase db;
    Context context;

    String CREATION_TABLE = "CREATE TABLE Downloads ( "
            + "colum INTEGER PRIMARY KEY AUTOINCREMENT, " + "id INTEGER, " + "title TEXT, " + "name TEXT, " + "user TEXT, "+ "download INTEGER, "+ "progress INTEGER, " + "time TEXT,"+ "date TEXT, "+ "type INTEGER, "+ "study INTEGER, "+ "subject INTEGER, " + "lesson INTEGER )";

    public DownloadSQLDatabaseHandler(Context context) {
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

    public void deleteDownload(DownloadListChildItem Download) {
        // Get reference to writable DB
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(Download.getColumn())});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Downloads.db");
        } catch (Exception e) {

        }
    }

    public DownloadListChildItem getDownload(int Column) {
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

        DownloadListChildItem Download = new DownloadListChildItem();
        Download.setColumn(cursor.getInt(0));
        Download.setId(cursor.getInt(1));
        Download.setTitle(cursor.getString(2));
        Download.setName(cursor.getString(3));
        Download.setUser(cursor.getString(4));
        Download.setDownload(cursor.getInt(5));
        Download.setProgress(cursor.getInt(6));
        Download.setTime(cursor.getString(7));
        Download.setDate(cursor.getString(8));
        Download.setType(cursor.getInt(9));
        Download.setStudy(cursor.getInt(10));
        Download.setSubject(cursor.getInt(11));
        Download.setLesson(cursor.getInt(12));
        cursor.close();
        return Download;
    }

    public List<DownloadListChildItem> allDownloads() {

        List<DownloadListChildItem> Downloads = new ArrayList<DownloadListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DownloadListChildItem Download = null;

        if (cursor.moveToFirst()) {
            do {
                Download = new DownloadListChildItem();
                Download.setColumn(cursor.getInt(0));
                Download.setId(cursor.getInt(1));
                Download.setTitle(cursor.getString(2));
                Download.setName(cursor.getString(3));
                Download.setUser(cursor.getString(4));
                Download.setDownload(cursor.getInt(5));
                Download.setProgress(cursor.getInt(6));
                Download.setTime(cursor.getString(7));
                Download.setDate(cursor.getString(8));
                Download.setType(cursor.getInt(9));
                Download.setStudy(cursor.getInt(10));
                Download.setSubject(cursor.getInt(11));
                Download.setLesson(cursor.getInt(12));
                Downloads.add(Download);
            } while (cursor.moveToNext());
        }
cursor.close();
        return Downloads;
    }

    public List<String> allDates() {
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, DownloadListChildItem> DownloadsWithoutGroup = new HashMap<Integer, DownloadListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        DownloadListChildItem Download = null;
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                folder = cursor.getString(10);
                if (!folders.contains(folder)) {
                    folders.add(folder);
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }

    public void addDownload(DownloadListChildItem Download) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Download.getColumn());
        values.put(KEY_ID, Download.getId());
        values.put(KEY_TITLE, Download.getTitle());
        values.put(KEY_NAME, Download.getName());
        values.put(KEY_USER, Download.getUser());
        values.put(KEY_DOWNLOAD, Download.getDownload());
        values.put(KEY_PROGRESS, (Download.getProgress()));
        values.put(KEY_TIME, Download.getTime());
        values.put(KEY_DATE, Download.getDate());
        values.put(KEY_TYPE, Download.getType());
        values.put(KEY_STUDY, Download.getStudy());
        values.put(KEY_SUBJECT, Download.getSubject());
        values.put(KEY_LESSON, Download.getLesson());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Downloads.db");
        } catch (Exception e) {

        }
    }

    public int updateDownload(DownloadListChildItem Download) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Download.getColumn());
        values.put(KEY_ID, Download.getId());
        values.put(KEY_TITLE, Download.getTitle());
        values.put(KEY_NAME, Download.getName());
        values.put(KEY_USER, Download.getUser());
        values.put(KEY_DOWNLOAD, Download.getDownload());
        values.put(KEY_PROGRESS, (Download.getProgress()));
        values.put(KEY_TIME, Download.getTime());
        values.put(KEY_DATE, Download.getDate());
        values.put(KEY_TYPE, Download.getType());
        values.put(KEY_STUDY, Download.getStudy());
        values.put(KEY_SUBJECT, Download.getSubject());
        values.put(KEY_LESSON, Download.getLesson());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(Download.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Downloads.db");
        } catch (Exception e) {

        }
        return i;
    }
    boolean isOpened=false;
    SQLiteDatabase database;
    void openDatabase(){
        isOpened = true;
        database = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
       // return db;
    }
    void closeDatabase(){
        database.close();
        isOpened = false;
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Downloads.db");
        } catch (Exception e) {

        }
    }
    public int updateDownloadP(DownloadListChildItem Download) {
        if(!isOpened)
openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Download.getColumn());
        values.put(KEY_ID, Download.getId());
        values.put(KEY_TITLE, Download.getTitle());
        values.put(KEY_NAME, Download.getName());
        values.put(KEY_USER, Download.getUser());
        values.put(KEY_DOWNLOAD, Download.getDownload());
        values.put(KEY_PROGRESS, (Download.getProgress()));
        values.put(KEY_TIME, Download.getTime());
        values.put(KEY_DATE, Download.getDate());
        values.put(KEY_TYPE, Download.getType());
        values.put(KEY_STUDY, Download.getStudy());
        values.put(KEY_SUBJECT, Download.getSubject());
        values.put(KEY_LESSON, Download.getLesson());

        int i = database.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(Download.getColumn())});

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
