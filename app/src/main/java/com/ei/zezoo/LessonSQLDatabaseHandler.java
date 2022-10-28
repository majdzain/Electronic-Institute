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

public class LessonSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Lessons_Database.db";
    private static String DATABASE_PATH = "/data/data/com.zezoo.ElectronicInstitute/databases/";
    private static final String DATABASE_EX_PATH = "/ElectronicInstitute/Databases/";
    private static final String TABLE_NAME = "Lessons";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_TYPE= "type";
    private static final String KEY_STUDY= "study";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_LESSON= "lesson";
    private static final String KEY_DOWNLOADID = "downloadId";
    private static final String KEY_SUBJECTN = "subjectN";
    private static final String KEY_LESSONN= "lessonN";
    private static final String KEY_FILE = "file";
    private static final String KEY_ISNEW = "isnew";
    private static final String KEY_DOWNLOAD = "download";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";

    private static final String[] COLUMNS = {KEY_COLUMN,KEY_TYPE,KEY_STUDY, KEY_SUBJECT, KEY_LESSON, KEY_DOWNLOADID, KEY_SUBJECTN, KEY_LESSONN, KEY_FILE, KEY_ISNEW, KEY_DOWNLOAD, KEY_TIME, KEY_DATE};

    SQLiteDatabase db;
    Context context;

    String CREATION_TABLE = "CREATE TABLE Lessons ( "
            + "colum INTEGER PRIMARY KEY AUTOINCREMENT, " + "type INTEGER, " + "study INTEGER, "  + "subject INTEGER, "+ "lesson INTEGER, " + "downloadId INTEGER, "+ "subjectN TEXT, "+ "lessonN TEXT, "+ "file TEXT, "+ "isnew INTEGER, "+ "download INTEGER, " + "time TEXT," + "date TEXT )";

    public LessonSQLDatabaseHandler(Context context) {
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

    public void deleteLesson(LessonListChildItem Lesson) {
        // Get reference to writable DB
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(Lesson.getColumn())});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Lessons.db");
        } catch (Exception e) {

        }
    }

    public LessonListChildItem getLesson(int Column) {
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

        LessonListChildItem Lesson = new LessonListChildItem();
        Lesson.setColumn(cursor.getInt(0));
        Lesson.setType(cursor.getInt(1));
        Lesson.setStudy(cursor.getInt(2));
        Lesson.setSubject(cursor.getInt(3));
        Lesson.setLesson(cursor.getInt(4));
        Lesson.setDownloadId(cursor.getInt(5));
        Lesson.setSubjectN(cursor.getString(6));
        Lesson.setLessonN(cursor.getString(7));
        Lesson.setFile(cursor.getString(8));
        Lesson.setNew(cursor.getInt(9) > 0);
        Lesson.setDownload(cursor.getInt(10));
        Lesson.setTime(cursor.getString(11));
        Lesson.setDate(cursor.getString(12));
        return Lesson;
    }

    public List<LessonListChildItem> allLessons() {

        List<LessonListChildItem> Lessons = new ArrayList<LessonListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        LessonListChildItem Lesson = null;

        if (cursor.moveToFirst()) {
            do {
                Lesson = new LessonListChildItem();
                Lesson.setColumn(cursor.getInt(0));
                Lesson.setType(cursor.getInt(1));
                Lesson.setStudy(cursor.getInt(2));
                Lesson.setSubject(cursor.getInt(3));
                Lesson.setLesson(cursor.getInt(4));
                Lesson.setDownloadId(cursor.getInt(5));
                Lesson.setSubjectN(cursor.getString(6));
                Lesson.setLessonN(cursor.getString(7));
                Lesson.setFile(cursor.getString(8));
                Lesson.setNew(cursor.getInt(9) > 0);
                Lesson.setDownload(cursor.getInt(10));
                Lesson.setTime(cursor.getString(11));
                Lesson.setDate(cursor.getString(12));
                Lessons.add(Lesson);
            } while (cursor.moveToNext());
        }
        return Lessons;
    }

    public List<LessonListChildItem> allLessons(int type ,int study,String subject) {

        List<LessonListChildItem> Lessons = new ArrayList<LessonListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        LessonListChildItem Lesson = null;

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(1) == type && cursor.getInt(2) == study && cursor.getString(6).matches(subject)) {
                    Lesson = new LessonListChildItem();
                    Lesson.setColumn(cursor.getInt(0));
                    Lesson.setType(cursor.getInt(1));
                    Lesson.setStudy(cursor.getInt(2));
                    Lesson.setSubject(cursor.getInt(3));
                    Lesson.setLesson(cursor.getInt(4));
                    Lesson.setDownloadId(cursor.getInt(5));
                    Lesson.setSubjectN(cursor.getString(6));
                    Lesson.setLessonN(cursor.getString(7));
                    Lesson.setFile(cursor.getString(8));
                    Lesson.setNew(cursor.getInt(9) > 0);
                    Lesson.setDownload(cursor.getInt(10));
                    Lesson.setTime(cursor.getString(11));
                    Lesson.setDate(cursor.getString(12));
                    Lessons.add(Lesson);
                }
            } while (cursor.moveToNext());
        }
        return Lessons;
    }

    public List<String> allSubjects(int type,int study) {
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, LessonListChildItem> LessonsWithoutGroup = new HashMap<Integer, LessonListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        LessonListChildItem Lesson = null;
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(1) == type && cursor.getInt(2) == study) {
                    folder = cursor.getString(6);
                    if (!folders.contains(folder)) {
                        folders.add(folder);
                    }
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }
    public List<String> allLessonsNames(int Subject) {
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, LessonListChildItem> LessonsWithoutGroup = new HashMap<Integer, LessonListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        LessonListChildItem Lesson = new LessonListChildItem();
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(3) == Subject) {
                    folder = cursor.getString(7);
                    if (!folders.contains(folder)) {
                        folders.add(folder);
                    }
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }
    public List<String> allDates() {
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, LessonListChildItem> LessonsWithoutGroup = new HashMap<Integer, LessonListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        LessonListChildItem Lesson = null;
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                folder = cursor.getString(8);
                if (!folders.contains(folder)) {
                    folders.add(folder);
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }

    public void addLesson(LessonListChildItem Lesson) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Lesson.getColumn());
        values.put(KEY_TYPE, Lesson.getType());
        values.put(KEY_STUDY, Lesson.getStudy());
        values.put(KEY_SUBJECT, Lesson.getSubject());
        values.put(KEY_LESSON, Lesson.getLesson());
        values.put(KEY_DOWNLOADID, Lesson.getDownloadId());
        values.put(KEY_SUBJECTN, Lesson.getSubjectN());
        values.put(KEY_LESSONN, Lesson.getLessonN());
        values.put(KEY_FILE, Lesson.getFile());
        values.put(KEY_DOWNLOAD, Lesson.getDownload());
        values.put(KEY_ISNEW, (Lesson.isNew()) ? 1 : 0);
        values.put(KEY_TIME, Lesson.getTime());
        values.put(KEY_DATE, Lesson.getDate());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Lessons.db");
        } catch (Exception e) {

        }
    }

    public int updateLesson(LessonListChildItem Lesson) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Lesson.getColumn());
        values.put(KEY_TYPE, Lesson.getType());
        values.put(KEY_STUDY, Lesson.getStudy());
        values.put(KEY_SUBJECT, Lesson.getSubject());
        values.put(KEY_LESSON, Lesson.getLesson());
        values.put(KEY_DOWNLOADID, Lesson.getDownloadId());
        values.put(KEY_SUBJECTN, Lesson.getSubjectN());
        values.put(KEY_LESSONN, Lesson.getLessonN());
        values.put(KEY_FILE, Lesson.getFile());
        values.put(KEY_DOWNLOAD, Lesson.getDownload());
        values.put(KEY_ISNEW, (Lesson.isNew()) ? 1 : 0);
        values.put(KEY_TIME, Lesson.getTime());
        values.put(KEY_DATE, Lesson.getDate());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(Lesson.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Lessons.db");
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
