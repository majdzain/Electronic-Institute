package com.ei.zezoo;

public class QuesListChildItem {
    private int Column,NumLike,NumDis;
    private boolean isTeacher,isLike,isDis;
    public QuesListChildItem(int column, int numLike, int numDis, String idG, String id, String name, String user, String text, String time, String date, boolean isTeacher, boolean isLike, boolean isDis) {
        Column = column;
        NumLike = numLike;
        NumDis = numDis;
        IdG = idG;
        Id = id;
        Name = name;
        User = user;
        Text = text;
        Time = time;
        Date = date;
        this.isTeacher = isTeacher;
        this.isLike = isLike;
        this.isDis = isDis;
    }
    QuesListChildItem (){

    }

    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }

    public int getNumLike() {
        return NumLike;
    }

    public void setNumLike(int numLike) {
        NumLike = numLike;
    }

    public int getNumDis() {
        return NumDis;
    }

    public void setNumDis(int numDis) {
        NumDis = numDis;
    }

    public String getIdG() {
        return IdG;
    }

    public void setIdG(String idG) {
        IdG = idG;
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


    private String IdG,Id,Name,User,Text,Time,Date;

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isDis() {
        return isDis;
    }

    public void setDis(boolean dis) {
        isDis = dis;
    }
}
