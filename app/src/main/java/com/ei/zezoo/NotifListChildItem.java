package com.ei.zezoo;

public class NotifListChildItem {
    private String  Name, Time, Date;
    private boolean isNew;
    private int Column, Subject,TypeN,Type,Study,Id,Download;

    NotifListChildItem() {

    }

    NotifListChildItem(int column, int id, int typeN, int type, int study, int subject, String name,int download,boolean isnew, String time, String date) {
        Subject = subject;
        Name = name;
        Column = column;
        Time = time;
        Date = date;
        Type = type;
        TypeN = typeN;
        Study = study;
        Id = id;
        Download = download;
        isNew = isnew;
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


    public int getSubject() {
        return Subject;
    }

    public void setSubject(int subject) {
        Subject = subject;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getDownload() {
        return Download;
    }

    public void setDownload(int download) {
        Download = download;
    }

    public int getTypeN() {
        return TypeN;
    }

    public void setTypeN(int typeN) {
        TypeN = typeN;
    }

    public int getStudy() {
        return Study;
    }

    public void setStudy(int study) {
        Study = study;
    }
}
