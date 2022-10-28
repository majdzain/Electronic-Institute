package com.ei.zezoo;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.provider.MediaStore.Images.Thumbnails.IMAGE_ID;

public class UserSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users_Database.db";
    private static String DATABASE_PATH = "/data/data/com.zezoo.ElectronicInstitute/databases/";
    private static final String DATABASE_EX_PATH = "/ElectronicInstitute/Databases/";
    private static final String TABLE_NAME = "Users";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_USER = "user";
    private static final String KEY_USERID = "userId";
    private static final String KEY_NAME = "name";
    private static final String KEY_FATHER = "father";
    private static final String KEY_MOTHER = "mother";
    private static final String KEY_PLACE = "place";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_ID = "id";
    private static final String KEY_FROM = "frome";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STUDY = "study";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PROFILE = "profile";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_BILL = "bill";
    private static final String KEY_BILLS = "bills";
    private static final String KEY_STUDY1 = "study1";
    private static final String KEY_STUDY2 = "study2";
    private static final String KEY_STUDY3 = "study3";
    private static final String KEY_STUDY4 = "study4";
    private static final String KEY_STUDY5 = "study5";
    private static final String KEY_STUDY6 = "study6";
    private static final String KEY_STUDY7 = "study7";
    private static final String KEY_STUDY8 = "study8";
    private static final String KEY_STUDY9 = "study9";
    private static final String KEY_STUDY10 = "study10";
    private static final String KEY_STUDY11 = "study11";
    private static final String KEY_STUDY12 = "study12";
    private static final String KEY_STUDY13 = "study13";
    private static final String KEY_STUDY14 = "study14";
    private static final String KEY_STUDY15 = "study15";
    private static final String KEY_STUDY16 = "study16";
    private static final String KEY_STUDY17 = "study17";
    private static final String KEY_STUDY18 = "study18";
    private static final String KEY_STUDY19 = "study19";
    private static final String KEY_STUDY20 = "study20";
    private static final String KEY_ISTEACHER = "isteacher";
    private static final String KEY_NUMCOMMENTS = "numComments";
    private static final String KEY_ISCURRENT = "iscurrent";
    private static final String KEY_ISBANNED = "isbanned";
    private static final String KEY_PASS = "pass";
    private static final String KEY_STATUS = "status";

    private static final String[] COLUMNS = {KEY_COLUMN,KEY_USER,KEY_STUDY,KEY_NAME,KEY_FATHER,KEY_MOTHER,KEY_PLACE,KEY_BIRTHDAY,KEY_ID,KEY_FROM,KEY_PHONE,KEY_TELEPHONE,KEY_EMAIL,KEY_PROFILE, KEY_TIME, KEY_DATE,KEY_BILLS,KEY_BILL,KEY_ISTEACHER,KEY_USERID,KEY_NUMCOMMENTS,KEY_TYPE,KEY_ISCURRENT,KEY_PASS,KEY_STATUS,KEY_ISBANNED,KEY_STUDY1,KEY_STUDY2,KEY_STUDY3,KEY_STUDY4,KEY_STUDY5,KEY_STUDY6,KEY_STUDY7,KEY_STUDY8,KEY_STUDY9,KEY_STUDY10,KEY_STUDY11,KEY_STUDY12,KEY_STUDY13,KEY_STUDY14,KEY_STUDY15,KEY_STUDY16,KEY_STUDY17,KEY_STUDY18,KEY_STUDY19,KEY_STUDY20};

    SQLiteDatabase db;
    Context context;

    String CREATION_TABLE = "CREATE TABLE Users ( "
            + "colum INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "user TEXT, " + "study INTEGER, " + "name TEXT," + "father TEXT," + "mother TEXT," + "place TEXT," + "birthday TEXT," + "id TEXT," + "frome TEXT," + "phone TEXT," + "telephone TEXT," + "email TEXT," + "profile TEXT," + "time TEXT,"+ "date TEXT,"+ "bills TEXT,"+ "bill TEXT,"+ "isteacher INTEGER,"+ "userId TEXT,"+ "numComments INTEGER,"+ "type INTEGER,"+ "iscurrent INTEGER,"+ "pass TEXT,"+"status TEXT,"+ "isbanned INTEGER,"+ "study1 INTEGER,"+ "study2 INTEGER,"+ "study3 INTEGER,"+ "study4 INTEGER,"+ "study5 INTEGER,"+ "study6 INTEGER,"+ "study7 INTEGER,"+ "study8 INTEGER,"+ "study9 INTEGER,"+ "study10 INTEGER,"+ "study11 INTEGER,"+ "study12 INTEGER,"+ "study13 INTEGER,"+ "study14 INTEGER,"+ "study15 INTEGER,"+ "study16 INTEGER,"+ "study17 INTEGER,"+ "study18 INTEGER,"+ "study19 INTEGER," + "study20 INTEGER )";

    public UserSQLDatabaseHandler(Context context) {
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

    public void deleteUser(UserListChildItem User) {
        // Get reference to writable DB
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(User.getColumn())});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Users.db");
        } catch (Exception e) {

        }
    }

    public UserListChildItem getUser(int Column) {
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

        UserListChildItem User = new UserListChildItem();
        User.setColumn(cursor.getInt(0));
        User.setUser(cursor.getString(1));
        User.setStudy(cursor.getInt(2));
        User.setName(cursor.getString(3));
        User.setFather(cursor.getString(4));
        User.setMother(cursor.getString(5));
        User.setPlace(cursor.getString(6));
        User.setBirthday(cursor.getString(7));
        User.setId(cursor.getString(8));
        User.setFrom(cursor.getString(9));
        User.setPhone(cursor.getString(10));
        User.setTelephone(cursor.getString(11));
        User.setEmail(cursor.getString(12));
        User.setProfile(BitmapFactory.decodeByteArray(cursor.getBlob(13) , 0, cursor.getBlob(13).length));
        User.setTime(cursor.getString(14));
        User.setDate(cursor.getString(15));
        User.setBills(cursor.getString(16));
        User.setBill(cursor.getString(17));
        User.setTeacher(cursor.getInt(18) > 0);
        User.setUserId(cursor.getString(19));
        User.setNumComments(cursor.getInt(20));
        User.setType(cursor.getInt(21));
        User.setCurrent(cursor.getInt(22) > 0);
        User.setPassword(cursor.getString(23));
        User.setStatus(cursor.getString(24));
        User.setBanned(cursor.getInt(25) > 0);
        ArrayList<Integer> studies = new ArrayList<>();
        for(int i = 26;i<46;i++){
            if(cursor.getInt(i) != 0)
                studies.add(cursor.getInt(i));
        }
        User.setStudies(studies);
        return User;
    }

    public List<UserListChildItem> allUsers() {

        List<UserListChildItem> Users = new ArrayList<UserListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        UserListChildItem User = null;

        if (cursor.moveToFirst()) {
            do {
                User = new UserListChildItem();
                User.setColumn(cursor.getInt(0));
                User.setUser(cursor.getString(1));
                User.setStudy(cursor.getInt(2));
                User.setName(cursor.getString(3));
                User.setFather(cursor.getString(4));
                User.setMother(cursor.getString(5));
                User.setPlace(cursor.getString(6));
                User.setBirthday(cursor.getString(7));
                User.setId(cursor.getString(8));
                User.setFrom(cursor.getString(9));
                User.setPhone(cursor.getString(10));
                User.setTelephone(cursor.getString(11));
                User.setEmail(cursor.getString(12));
                User.setProfile(BitmapFactory.decodeByteArray(cursor.getBlob(13) , 0, cursor.getBlob(13).length));
                User.setTime(cursor.getString(14));
                User.setDate(cursor.getString(15));
                User.setBills(cursor.getString(16));
                User.setBill(cursor.getString(17));
                User.setTeacher(cursor.getInt(18) > 0);
                User.setUserId(cursor.getString(19));
                User.setNumComments(cursor.getInt(20));
                User.setType(cursor.getInt(21));
                User.setCurrent(cursor.getInt(22) > 0);
                User.setPassword(cursor.getString(23));
                User.setStatus(cursor.getString(24));
                User.setBanned(cursor.getInt(25) > 0);
                ArrayList<Integer> studies = new ArrayList<>();
                for(int i = 26;i<46;i++){
                    if(cursor.getInt(i) != 0)
                        studies.add(cursor.getInt(i));
                }
                User.setStudies(studies);
                Users.add(User);
            } while (cursor.moveToNext());
        }
        return Users;
    }

    public UserListChildItem getCurrentUser() {

        List<UserListChildItem> Users = new ArrayList<UserListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        UserListChildItem User = null;

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(22) > 0) {
                    User = new UserListChildItem();
                    User.setColumn(cursor.getInt(0));
                    User.setUser(cursor.getString(1));
                    User.setStudy(cursor.getInt(2));
                    User.setName(cursor.getString(3));
                    User.setFather(cursor.getString(4));
                    User.setMother(cursor.getString(5));
                    User.setPlace(cursor.getString(6));
                    User.setBirthday(cursor.getString(7));
                    User.setId(cursor.getString(8));
                    User.setFrom(cursor.getString(9));
                    User.setPhone(cursor.getString(10));
                    User.setTelephone(cursor.getString(11));
                    User.setEmail(cursor.getString(12));
                    User.setProfile(BitmapFactory.decodeByteArray(cursor.getBlob(13), 0, cursor.getBlob(13).length));
                    User.setTime(cursor.getString(14));
                    User.setDate(cursor.getString(15));
                    User.setBills(cursor.getString(16));
                    User.setBill(cursor.getString(17));
                    User.setTeacher(cursor.getInt(18) > 0);
                    User.setUserId(cursor.getString(19));
                    User.setNumComments(cursor.getInt(20));
                    User.setType(cursor.getInt(21));
                    User.setCurrent(cursor.getInt(22) > 0);
                    User.setPassword(cursor.getString(23));
                    User.setStatus(cursor.getString(24));
                    User.setBanned(cursor.getInt(25) > 0);
                    ArrayList<Integer> studies = new ArrayList<>();
                    for(int i = 26;i<46;i++){
                        if(cursor.getInt(i) != 0)
                            studies.add(cursor.getInt(i));
                    }
                    User.setStudies(studies);
                    break;
                }
            } while (cursor.moveToNext());
        }
        return User;
    }


    public List<String> allUsersNames() {
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, UserListChildItem> UsersWithoutGroup = new HashMap<Integer, UserListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        UserListChildItem User = null;
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                folder = cursor.getString(1);
                folders.add(folder);
            } while (cursor.moveToNext());
        }
        return folders;
    }
    public List<Integer> allUsersStudies() {
        List<Integer> folders = new ArrayList<Integer>();
        HashMap<Integer, UserListChildItem> UsersWithoutGroup = new HashMap<Integer, UserListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        UserListChildItem User = null;
        int folder = 0;
        if (cursor.moveToFirst()) {
            do {
                folder = cursor.getInt(2);
                folders.add(folder);
            } while (cursor.moveToNext());
        }
        return folders;
    }


    public void addUser(UserListChildItem User) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, User.getColumn());
        values.put(KEY_USER, User.getUser());
        values.put(KEY_USERID, User.getUserId());
        values.put(KEY_STUDY, User.getStudy());
        values.put(KEY_NAME, User.getName());
        values.put(KEY_FATHER, User.getFather());
        values.put(KEY_MOTHER, User.getMother());
        values.put(KEY_PLACE, User.getPlace());
        values.put(KEY_BIRTHDAY, User.getBirthday());
        values.put(KEY_ID, User.getId());
        values.put(KEY_FROM, User.getFrom());
        values.put(KEY_PHONE, User.getPhone());
        values.put(KEY_TELEPHONE, User.getTelephone());
        values.put(KEY_EMAIL, User.getEmail());
        values.put(KEY_TIME, User.getTime());
        values.put(KEY_DATE, User.getDate());
        values.put(KEY_BILLS, User.getBills());
        values.put(KEY_BILL, User.getBill());
        values.put(KEY_STUDY1,0);
        values.put(KEY_STUDY2,0);
        values.put(KEY_STUDY3,0);
        values.put(KEY_STUDY4,0);
        values.put(KEY_STUDY5,0);
        values.put(KEY_STUDY6,0);
        values.put(KEY_STUDY7,0);
        values.put(KEY_STUDY8,0);
        values.put(KEY_STUDY9,0);
        values.put(KEY_STUDY10,0);
        values.put(KEY_STUDY11,0);
        values.put(KEY_STUDY12,0);
        values.put(KEY_STUDY13,0);
        values.put(KEY_STUDY14,0);
        values.put(KEY_STUDY15,0);
        values.put(KEY_STUDY16,0);
        values.put(KEY_STUDY17,0);
        values.put(KEY_STUDY18,0);
        values.put(KEY_STUDY19,0);
        values.put(KEY_STUDY20,0);
        values.put(KEY_ISTEACHER,(User.isTeacher()) ? 1 : 0);
        values.put(KEY_ISCURRENT,(User.isCurrent()) ? 1 : 0);
        values.put(KEY_NUMCOMMENTS, User.getNumComments());
        values.put(KEY_TYPE, User.getType());
        values.put(KEY_PASS, User.getPassword());
        values.put(KEY_STATUS, User.getStatus());
        values.put(KEY_ISBANNED,(User.isBanned()) ? 1 : 0);
        for(int i = 0;i<User.getStudies().size();i++){
            values.put("study"+String.valueOf(i+1),User.getStudies().get(i));
        }
        Bitmap bitmap = User.getProfile();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(KEY_PROFILE, stream.toByteArray());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Users.db");
        } catch (Exception e) {

        }
        if(User.isCurrent()) {
            for (int i = 0; i < allUsers().size(); i++) {
                if (allUsers().get(i).getColumn() != User.getColumn()) {
                    UserListChildItem u = allUsers().get(i);
                    u.setCurrent(false);
                    updateUser(u);
                }
            }
        }
    }
    public void setCurrentUser(UserListChildItem User) {
      User.setCurrent(true);
       updateUser(User);
        for(int i =0;i<allUsers().size();i++){
           if(allUsers().get(i).getColumn() != User.getColumn()){
               UserListChildItem u = allUsers().get(i);
               u.setCurrent(false);
               updateUser(u);
           }
       }

    }

    public int updateUser(UserListChildItem User) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, User.getColumn());
        values.put(KEY_USER, User.getUser());
        values.put(KEY_USERID, User.getUserId());
        values.put(KEY_STUDY, User.getStudy());
        values.put(KEY_NAME, User.getName());
        values.put(KEY_FATHER, User.getFather());
        values.put(KEY_MOTHER, User.getMother());
        values.put(KEY_PLACE, User.getPlace());
        values.put(KEY_BIRTHDAY, User.getBirthday());
        values.put(KEY_ID, User.getId());
        values.put(KEY_FROM, User.getFrom());
        values.put(KEY_PHONE, User.getPhone());
        values.put(KEY_TELEPHONE, User.getTelephone());
        values.put(KEY_EMAIL, User.getEmail());
        values.put(KEY_TIME, User.getTime());
        values.put(KEY_DATE, User.getDate());
        values.put(KEY_BILLS, User.getBills());
        values.put(KEY_BILL, User.getBill());
        values.put(KEY_STUDY1,0);
        values.put(KEY_STUDY2,0);
        values.put(KEY_STUDY3,0);
        values.put(KEY_STUDY4,0);
        values.put(KEY_STUDY5,0);
        values.put(KEY_STUDY6,0);
        values.put(KEY_STUDY7,0);
        values.put(KEY_STUDY8,0);
        values.put(KEY_STUDY9,0);
        values.put(KEY_STUDY10,0);
        values.put(KEY_STUDY11,0);
        values.put(KEY_STUDY12,0);
        values.put(KEY_STUDY13,0);
        values.put(KEY_STUDY14,0);
        values.put(KEY_STUDY15,0);
        values.put(KEY_STUDY16,0);
        values.put(KEY_STUDY17,0);
        values.put(KEY_STUDY18,0);
        values.put(KEY_STUDY19,0);
        values.put(KEY_STUDY20,0);
        values.put(KEY_TYPE, User.getType());
        values.put(KEY_ISTEACHER,(User.isTeacher()) ? 1 : 0);
        values.put(KEY_NUMCOMMENTS, User.getNumComments());
        values.put(KEY_ISCURRENT,(User.isCurrent()) ? 1 : 0);
        values.put(KEY_PASS, User.getPassword());
        values.put(KEY_STATUS, User.getStatus());
        values.put(KEY_ISBANNED,(User.isBanned()) ? 1 : 0);
        for(int i = 0;i<User.getStudies().size();i++){
            values.put("study"+String.valueOf(i+1),User.getStudies().get(i));
        }
        Bitmap bitmap = User.getProfile();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(KEY_PROFILE, stream.toByteArray());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(User.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstitute" + File.separator + "Users.db");
        } catch (Exception e) {

        }
        if(User.isCurrent()) {
            for (int j = 0; j < allUsers().size(); j++) {
                if (allUsers().get(j).getColumn() != User.getColumn()) {
                    UserListChildItem u = allUsers().get(j);
                    u.setCurrent(false);
                    updateUser(u);
                }
            }
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
