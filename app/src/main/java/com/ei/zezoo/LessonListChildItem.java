package com.ei.zezoo;

public class LessonListChildItem {
    int Column;
    int Type;
    int Study;
    int Subject;
    int Lesson;
    int DownloadId;

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

    boolean isNew;
    int Download;

    String SubjectN,LessonN,File,Time,Date;
    public LessonListChildItem() {
    }

    public LessonListChildItem(int column,int type, int study, int subject, int lesson, int downloadId, String subjectN, String lessonN, String file,boolean isnew,int download, String time, String date) {
        Column = column;
        Type = type;
        Study = study;
        Subject = subject;
        Lesson = lesson;
        DownloadId = downloadId;
        SubjectN = subjectN;
        LessonN = lessonN;
        File = file;
        isNew = isnew;
        Download = download;
        Time = time;
        Date = date;
    }
    public int getStudy() {
        return Study;
    }

    public void setStudy(int study) {
        Study = study;
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

    public int getLesson() {
        return Lesson;
    }

    public void setLesson(int lesson) {
        Lesson = lesson;
    }

    public int getDownloadId() {
        return DownloadId;
    }

    public void setDownloadId(int downloadId) {
        DownloadId = downloadId;
    }

    public String getSubjectN() {
        return SubjectN;
    }

    public void setSubjectN(String subjectN) {
        SubjectN = subjectN;
    }

    public String getLessonN() {
        return LessonN;
    }

    public void setLessonN(String lessonN) {
        LessonN = lessonN;
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
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

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
