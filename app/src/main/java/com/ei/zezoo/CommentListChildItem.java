package com.ei.zezoo;


public class CommentListChildItem {
    int Column,RecordTime;
    boolean isTeacher;
    String Name;
    String Comment;
    String Time;
    String Date;
    private String Id,Reply;
    private String User;
    String RecrordData;
    String Image1,Image2,Image3,Image4,Image5,Image6,Image7,Image8,Image9,Image10;
    private String Type;

    public CommentListChildItem(int studyType, int study, int subject, int lesson) {
        StudyType = studyType;
        Study = study;
        Subject = subject;
        Lesson = lesson;
    }

    private int StudyType,Study,Subject,Lesson;

    public static int TYPE_TEXT = 1;
    public static int TYPE_AUDIO = 21;

    public String getRecrordData() {
        return RecrordData;
    }

    public void setRecrordData(String recrordData) {
        RecrordData = recrordData;
    }




    public int getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(int recordTime) {
        RecordTime = recordTime;
    }


    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public String getImage3() {
        return Image3;
    }

    public void setImage3(String image3) {
        Image3 = image3;
    }

    public String getImage4() {
        return Image4;
    }

    public void setImage4(String image4) {
        Image4 = image4;
    }

    public String getImage5() {
        return Image5;
    }

    public void setImage5(String image5) {
        Image5 = image5;
    }

    public String getImage6() {
        return Image6;
    }

    public void setImage6(String image6) {
        Image6 = image6;
    }

    public String getImage7() {
        return Image7;
    }

    public void setImage7(String image7) {
        Image7 = image7;
    }

    public String getImage8() {
        return Image8;
    }

    public void setImage8(String image8) {
        Image8 = image8;
    }

    public String getImage9() {
        return Image9;
    }

    public void setImage9(String image9) {
        Image9 = image9;
    }

    public String getImage10() {
        return Image10;
    }

    public void setImage10(String image10) {
        Image10 = image10;
    }


    public int getStudyType() {
        return StudyType;
    }

    public void setStudyType(int studyType) {
        StudyType = studyType;
    }

    public int getStudy() {
        return Study;
    }

    public void setStudy(int study) {
        Study = study;
    }

    public int getLesson() {
        return Lesson;
    }

    public void setLesson(int lesson) {
        Lesson = lesson;
    }

    public CommentListChildItem(int column, String id, int studyType, int study, int subject, int lesson, boolean isTeacher, String user, String name, String comment, String time, String date, String image1, String image2, String image3, String image4, String image5, String image6, String image7, String image8, String image9, String image10, int recordTime, String recordData, String reply, String type) {
        Column = column;
        Id = id;
        Subject = subject;
        this.isTeacher = isTeacher;
        User = user;
        Name = name;
        Comment = comment;
        Time = time;
        Date = date;
        Image1 = image1;
        Image2 = image2;
        Image3 = image3;
        Image4 = image4;
        Image5 = image5;
        Image6 = image6;
        Image7 = image7;
        Image8 = image8;
        Image9 = image9;
        Image10 = image10;
        RecordTime = recordTime;
        RecrordData = recordData;
        Reply = reply;
        Type = type;
        StudyType = studyType;
        Study = study;
        Subject = subject;
        Lesson = lesson;
    }
    public CommentListChildItem() {
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

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
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


    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
