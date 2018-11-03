package com.dev.fd.feederdaddyadmin.model;

public class AdminModel {

    private String username,city,zone,phone,password,fathername,mothername,permanentaddress,correspondanceaddress,aadhardetail, dateofjoining;

    public AdminModel() {
    }

    public AdminModel(String username, String city, String zone, String phone, String password, String fathername, String mothername, String permanentaddress, String correspondanceaddress, String aadhardetail, String dateofjoining) {
        this.username = username;
        this.city = city;
        this.zone = zone;
        this.phone = phone;
        this.password = password;
        this.fathername = fathername;
        this.mothername = mothername;
        this.permanentaddress = permanentaddress;
        this.correspondanceaddress = correspondanceaddress;
        this.aadhardetail = aadhardetail;
        this.dateofjoining = dateofjoining;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getPermanentaddress() {
        return permanentaddress;
    }

    public void setPermanentaddress(String permanentaddress) {
        this.permanentaddress = permanentaddress;
    }

    public String getCorrespondanceaddress() {
        return correspondanceaddress;
    }

    public void setCorrespondanceaddress(String correspondanceaddress) {
        this.correspondanceaddress = correspondanceaddress;
    }

    public String getAadhardetail() {
        return aadhardetail;
    }

    public void setAadhardetail(String aadhardetail) {
        this.aadhardetail = aadhardetail;
    }

    public String getDateofjoining() {
        return dateofjoining;
    }

    public void setDateofjoining(String dateofjoining) {
        this.dateofjoining = dateofjoining;
    }
}
