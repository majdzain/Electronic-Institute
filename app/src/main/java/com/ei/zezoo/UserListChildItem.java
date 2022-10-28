package com.ei.zezoo;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class UserListChildItem {

    private String  User ,Name,Father,Mother,Place,Birthday,Id,From,Phone,Telephone,Email, Time, Date,Bill,Bills,UserId,Password;
    private ArrayList<Integer> Studies;
    private int Column,Study,NumComments,Type;
    private Bitmap Profile;
    private boolean isTeacher,isCurrent,isBanned;
    private String Status;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public UserListChildItem(int column, String user, String userId, int type, int study, String name, String father, String mother, String place, String birthday, String id, String from, String phone, String telephone, String email, String time, String date, String bills, String bill, ArrayList<Integer> studies, Bitmap profile, boolean isTeacher, int numComments, boolean isCurrent,String pass,String status,boolean isBanned) {
        User = user;
        UserId = userId;
        Study = study;
        Name = name;
        Father = father;
        Mother = mother;
        Place = place;
        Birthday = birthday;
        Id = id;
        From = from;
        Phone = phone;
        Telephone = telephone;
        Email = email;
        Time = time;
        Date = date;
        Column = column;
        Bills = bills;
        Bill = bill;
        Studies = studies;
        Profile = profile;
        this.isTeacher = isTeacher;
        NumComments = numComments;
        Type = type;
        this.isCurrent = isCurrent;
        Password = pass;
        Status = status;
        this.isBanned = isBanned;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public int getStudy() {
        return Study;
    }

    public void setStudy(int study) {
        Study = study;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFather() {
        return Father;
    }

    public void setFather(String father) {
        Father = father;
    }

    public String getMother() {
        return Mother;
    }

    public void setMother(String mother) {
        Mother = mother;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    public Bitmap getProfile() {
        return Profile;
    }

    public void setProfile(Bitmap Profile) {
        this.Profile = Profile;
    }


    UserListChildItem() {
            Studies = new ArrayList<>();
    }


    public String getBill() {
        return Bill;
    }

    public void setBill(String bill) {
        Bill = bill;
    }

    public String getBills() {
        return Bills;
    }

    public void setBills(String bills) {
        Bills = bills;
    }

    public ArrayList<Integer> getStudies() {
        return Studies;
    }

    public void setStudies(ArrayList<Integer> studies) {
        Studies = studies;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getNumComments() {
        return NumComments;
    }

    public void setNumComments(int numComments) {
        NumComments = numComments;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
