package com.ei.zezoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Comments_Database.db";
    private static String DATABASE_PATH = "/data/data/com.zezoo.ElectronicInstitute/databases/";
    private static final String DATABASE_EX_PATH = "/ElectronicInstitute/Databases/";
    private static final String TABLE_NAME = "Comments";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_USER = "user";
    private static final String KEY_ISTEACHER = "isteacher";
    private static final String KEY_NAME = "name";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE1 = "image1";
    private static final String KEY_IMAGE2 = "image2";
    private static final String KEY_IMAGE3 = "image3";
    private static final String KEY_IMAGE4 = "image4";
    private static final String KEY_IMAGE5 = "image5";
    private static final String KEY_IMAGE6 = "image6";
    private static final String KEY_IMAGE7 = "image7";
    private static final String KEY_IMAGE8 = "image8";
    private static final String KEY_IMAGE9 = "image9";
    private static final String KEY_IMAGE10 = "image10";
    private static final String KEY_REPLY = "reply";
    private static final String KEY_TYPE = "type";
    private static final String KEY_STUDYTYPE = "studyType";
    private static final String KEY_STUDY = "study";
    private static final String KEY_LESSON = "lesson";
    private static final String KEY_RECORD_DATA = "recordData";
    private static final String KEY_RECORD_TIME = "recordTime";

    private static final String[] COLUMNS = {KEY_COLUMN,KEY_STUDYTYPE,KEY_STUDY, KEY_SUBJECT,KEY_LESSON, KEY_ISTEACHER, KEY_NAME, KEY_COMMENT, KEY_TIME, KEY_DATE, KEY_IMAGE1, KEY_IMAGE2, KEY_IMAGE3, KEY_IMAGE4, KEY_IMAGE5, KEY_IMAGE6, KEY_IMAGE7, KEY_IMAGE8, KEY_IMAGE9, KEY_IMAGE10, KEY_USER, KEY_ID, KEY_REPLY,KEY_TYPE,KEY_RECORD_DATA,KEY_RECORD_TIME};

    SQLiteDatabase db;
    Context context;

    String CREATION_TABLE = "CREATE TABLE Comments ( "
            + "colum INTEGER PRIMARY KEY AUTOINCREMENT, " + "studyType INTEGER, " + "study INTEGER, " + "subject INTEGER, " + "lesson INTEGER, " + "isteacher INTEGER, " + "name TEXT, " + "comment TEXT, " + "time TEXT," + "date TEXT," + "image1 TEXT," + "image2 TEXT," + "image3 TEXT," + "image4 TEXT," + "image5 TEXT," + "image6 TEXT," + "image7 TEXT," + "image8 TEXT," + "image9 TEXT," + "image10 TEXT,"+ "user TEXT,"+ "id TEXT,"+ "reply TEXT," + "type TEXT,"+ "recordData TEXT," + "recordTime INTEGER )";

    public CommentSQLDatabaseHandler(Context context) {
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

    public void deleteComment(CommentListChildItem Comment) {
        // Get reference to writable DB
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(Comment.getColumn())});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Comments.db");
        } catch (Exception e) {

        }
    }

    public CommentListChildItem getComment(int Column) {
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

        CommentListChildItem Comment = new CommentListChildItem();
        Comment.setColumn(cursor.getInt(0));
        Comment.setStudyType(cursor.getInt(1));
        Comment.setStudy(cursor.getInt(2));
        Comment.setSubject(cursor.getInt(3));
        Comment.setLesson(cursor.getInt(4));
        Comment.setTeacher(cursor.getInt(5) > 0);
        Comment.setName(cursor.getString(6));
        Comment.setComment(cursor.getString(7));
        Comment.setTime(cursor.getString(8));
        Comment.setDate(cursor.getString(9));
        //Comment.setImage1(BitmapFactory.decodeByteArray(cursor.getBlob(7), 0, cursor.getBlob(7).length));
        Comment.setImage1(cursor.getString(10));
        Comment.setImage2(cursor.getString(11));
        Comment.setImage3(cursor.getString(12));
        Comment.setImage4(cursor.getString(13));
        Comment.setImage5(cursor.getString(14));
        Comment.setImage6(cursor.getString(15));
        Comment.setImage7(cursor.getString(16));
        Comment.setImage8(cursor.getString(17));
        Comment.setImage9(cursor.getString(18));
        Comment.setImage10(cursor.getString(19));
        Comment.setUser(cursor.getString(20));
        Comment.setId(cursor.getString(21));
        Comment.setReply(cursor.getString(22));
        Comment.setType(cursor.getString(23));
        Comment.setRecrordData(cursor.getString(24));
        Comment.setRecordTime(cursor.getInt(25));
        return Comment;
    }

    public List<CommentListChildItem> allComments() {

        List<CommentListChildItem> Comments = new ArrayList<CommentListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                CommentListChildItem Comment = new CommentListChildItem();
                Comment.setColumn(cursor.getInt(0));
                Comment.setStudyType(cursor.getInt(1));
                Comment.setStudy(cursor.getInt(2));
                Comment.setSubject(cursor.getInt(3));
                Comment.setLesson(cursor.getInt(4));
                Comment.setTeacher(cursor.getInt(5) > 0);
                Comment.setName(cursor.getString(6));
                Comment.setComment(cursor.getString(7));
                Comment.setTime(cursor.getString(8));
                Comment.setDate(cursor.getString(9));
                //Comment.setImage1(BitmapFactory.decodeByteArray(cursor.getBlob(7), 0, cursor.getBlob(7).length));
                Comment.setImage1(cursor.getString(10));
                Comment.setImage2(cursor.getString(11));
                Comment.setImage3(cursor.getString(12));
                Comment.setImage4(cursor.getString(13));
                Comment.setImage5(cursor.getString(14));
                Comment.setImage6(cursor.getString(15));
                Comment.setImage7(cursor.getString(16));
                Comment.setImage8(cursor.getString(17));
                Comment.setImage9(cursor.getString(18));
                Comment.setImage10(cursor.getString(19));
                Comment.setUser(cursor.getString(20));
                Comment.setId(cursor.getString(21));
                Comment.setReply(cursor.getString(22));
                Comment.setType(cursor.getString(23));
                Comment.setRecrordData(cursor.getString(24));
                Comment.setRecordTime(cursor.getInt(25));
                Comments.add(Comment);
            } while (cursor.moveToNext());
        }
        return Comments;
    }
    public List<CommentListChildItem> allCommentsAt(int type,int study,int subject,int lesson) {

        List<CommentListChildItem> Comments = new ArrayList<CommentListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(1) == type && cursor.getInt(2) == study && cursor.getInt(3) == subject && cursor.getInt(4) == lesson) {
                    CommentListChildItem Comment = new CommentListChildItem();
                    Comment.setColumn(cursor.getInt(0));
                    Comment.setStudyType(cursor.getInt(1));
                    Comment.setStudy(cursor.getInt(2));
                    Comment.setSubject(cursor.getInt(3));
                    Comment.setLesson(cursor.getInt(4));
                    Comment.setTeacher(cursor.getInt(5) > 0);
                    Comment.setName(cursor.getString(6));
                    Comment.setComment(cursor.getString(7));
                    Comment.setTime(cursor.getString(8));
                    Comment.setDate(cursor.getString(9));
                    //Comment.setImage1(BitmapFactory.decodeByteArray(cursor.getBlob(7), 0, cursor.getBlob(7).length));
                    Comment.setImage1(cursor.getString(10));
                    Comment.setImage2(cursor.getString(11));
                    Comment.setImage3(cursor.getString(12));
                    Comment.setImage4(cursor.getString(13));
                    Comment.setImage5(cursor.getString(14));
                    Comment.setImage6(cursor.getString(15));
                    Comment.setImage7(cursor.getString(16));
                    Comment.setImage8(cursor.getString(17));
                    Comment.setImage9(cursor.getString(18));
                    Comment.setImage10(cursor.getString(19));
                    Comment.setUser(cursor.getString(20));
                    Comment.setId(cursor.getString(21));
                    Comment.setReply(cursor.getString(22));
                    Comment.setType(cursor.getString(23));
                    Comment.setRecrordData(cursor.getString(24));
                    Comment.setRecordTime(cursor.getInt(25));
                    Comments.add(Comment);
                }
            } while (cursor.moveToNext());
        }
        return Comments;
    }




    public List<String> allIds() {
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, CommentListChildItem> CommentsWithoutGroup = new HashMap<Integer, CommentListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        CommentListChildItem Comment = new CommentListChildItem();
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                folder = cursor.getString(21);
                if (!folders.contains(folder)) {
                    folders.add(folder);
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }

    public void addComment(CommentListChildItem Comment) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Comment.getColumn());
        values.put(KEY_SUBJECT, Comment.getSubject());
        values.put(KEY_ISTEACHER, (Comment.isTeacher()) ? 1 : 0);
        values.put(KEY_NAME, Comment.getName());
        values.put(KEY_COMMENT, Comment.getComment());
        values.put(KEY_TIME, Comment.getTime());
        values.put(KEY_DATE, Comment.getDate());
        //if (Comment.getImage1() != null) {
       //     ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
       //     Comment.getImage1().compress(Bitmap.CompressFormat.PNG, 100, stream1);
       //     values.put(KEY_IMAGE1, stream1.toByteArray());
      //  }
        values.put(KEY_IMAGE1, Comment.getImage1());
        values.put(KEY_IMAGE2, Comment.getImage2());
        values.put(KEY_IMAGE3, Comment.getImage3());
        values.put(KEY_IMAGE4, Comment.getImage4());
        values.put(KEY_IMAGE5, Comment.getImage5());
        values.put(KEY_IMAGE6, Comment.getImage6());
        values.put(KEY_IMAGE7, Comment.getImage7());
        values.put(KEY_IMAGE8, Comment.getImage8());
        values.put(KEY_IMAGE9, Comment.getImage9());
        values.put(KEY_IMAGE10, Comment.getImage10());
        values.put(KEY_USER, Comment.getUser());
        values.put(KEY_ID, Comment.getId());
        values.put(KEY_REPLY, Comment.getReply());
        values.put(KEY_TYPE, Comment.getType());
        values.put(KEY_STUDYTYPE, Comment.getStudyType());
        values.put(KEY_STUDY, Comment.getStudy());
        values.put(KEY_LESSON, Comment.getLesson());
        values.put(KEY_RECORD_DATA, Comment.getRecrordData());
        values.put(KEY_RECORD_TIME,Comment.getRecordTime());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Comments.db");
        } catch (Exception e) {

        }
    }

    public int updateComment(CommentListChildItem Comment) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, Comment.getColumn());
        values.put(KEY_SUBJECT, Comment.getSubject());
        values.put(KEY_ISTEACHER, (Comment.isTeacher()) ? 1 : 0);
        values.put(KEY_NAME, Comment.getName());
        values.put(KEY_COMMENT, Comment.getComment());
        values.put(KEY_TIME, Comment.getTime());
        values.put(KEY_DATE, Comment.getDate());
        values.put(KEY_IMAGE1, Comment.getImage1());
        values.put(KEY_IMAGE2, Comment.getImage2());
        values.put(KEY_IMAGE3, Comment.getImage3());
        values.put(KEY_IMAGE4, Comment.getImage4());
        values.put(KEY_IMAGE5, Comment.getImage5());
        values.put(KEY_IMAGE6, Comment.getImage6());
        values.put(KEY_IMAGE7, Comment.getImage7());
        values.put(KEY_IMAGE8, Comment.getImage8());
        values.put(KEY_IMAGE9, Comment.getImage9());
        values.put(KEY_IMAGE10, Comment.getImage10());
        values.put(KEY_USER, Comment.getUser());
        values.put(KEY_ID, Comment.getId());
        values.put(KEY_REPLY, Comment.getReply());
        values.put(KEY_TYPE, Comment.getType());
        values.put(KEY_STUDYTYPE, Comment.getStudyType());
        values.put(KEY_STUDY, Comment.getStudy());
        values.put(KEY_LESSON, Comment.getLesson());
        values.put(KEY_RECORD_DATA, Comment.getRecrordData());
        values.put(KEY_RECORD_TIME,Comment.getRecordTime());
        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(Comment.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Comments.db");
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
