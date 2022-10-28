package com.ei.zezoo;

public class DownloadListChildItem {
    private String  Title ,Name, Time, Date,User;
    private int Type,Study,Subject,Lesson;
    static final int UNKNOWN = 0;
    static final int STARTED = 1;
    static final int RESUMED = 2;
    static final int PAUSED = 3;
    static final int STOPED = 4;
    static final int RUNNING = 5;
    static final int COMPLETED = 6;
    static final int ERROR = 7;

    DownloadListChildItem(){

    }
    public DownloadListChildItem(int column, int id, String title, String name, String user, int download, int progress, String time, String date, int type, int study, int subject, int lesson) {
        Title = title;
        Name = name;
        Time = time;
        Date = date;
        Column = column;
        Id = id;
        Download = download;
        Progress = progress;
        User = user;
        Type = type;
        Study = study;
        Subject = subject;
        Lesson = lesson;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getDownload() {
        return Download;
    }

    public void setDownload(int download) {
        Download = download;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }



    private int Column,Id,Download,Progress;

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getStudy() {
        return Study;
    }

    public void setStudy(int study) {
        Study = study;
    }

    public int getSubject() {
        return Subject;
    }

    public void setSubject(int subject) {
        Subject = subject;
    }

    public int getLesson() {
        return Lesson;
    }

    public void setLesson(int lesson) {
        Lesson = lesson;
    }
}
