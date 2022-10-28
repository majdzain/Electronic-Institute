package com.ei.zezoo;

public class QuesListGroupItem {
    private int Column,Type,Study,Subject,Num;
    private String Id,Name,Title,User,Text,Time,Date;
    public QuesListGroupItem(int column, int type, int study, int subject, int num, String id, String name, String title, String user, String text, String time, String date, boolean isTeacher) {
        Column = column;
        Type = type;
        Study = study;
        Subject = subject;
        Num = num;
        Id = id;
        Name = name;
        Title = title;
        User = user;
        Text = text;
        Time = time;
        Date = date;
        this.isTeacher = isTeacher;
    }
    QuesListGroupItem(){

    }
    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
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

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
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

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }



    private boolean isTeacher;
}
