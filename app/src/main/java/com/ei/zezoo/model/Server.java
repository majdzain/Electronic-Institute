package com.ei.zezoo.model;

public class Server {
    private String country;
    private String ovpn;
    private String ovpnUserName;
    private String ovpnUserPassword;


    public Server() {
    }

    public Server(String country, String ovpn) {
        this.country = country;
        this.ovpn = ovpn;
    }

    public Server(String country, String ovpn, String ovpnUserName, String ovpnUserPassword) {
        this.country = country;
        this.ovpn = ovpn;
        this.ovpnUserName = ovpnUserName;
        this.ovpnUserPassword = ovpnUserPassword;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getOvpn() {
        return ovpn;
    }

    public void setOvpn(String ovpn) {
        this.ovpn = ovpn;
    }

    public String getOvpnUserName() {
        return ovpnUserName;
    }

    public void setOvpnUserName(String ovpnUserName) {
        this.ovpnUserName = ovpnUserName;
    }

    public String getOvpnUserPassword() {
        return ovpnUserPassword;
    }

    public void setOvpnUserPassword(String ovpnUserPassword) {
        this.ovpnUserPassword = ovpnUserPassword;
    }
}
