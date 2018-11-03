package com.dev.fd.feederdaddyadmin.model;

public class DeliveryBoysModel {

    String username,password,name,fathername,mothername,permanentaddress, correspondanceaddress, aadhardetail, drivinglicence, phonenumber, city, dateofjoining;

    public DeliveryBoysModel() {
    }

    public DeliveryBoysModel(String username, String password, String name, String fathername, String mothername, String permanentaddress, String correspondanceaddress, String aadhardetail, String drivinglicence, String phonenumber, String city, String dateofjoining) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.fathername = fathername;
        this.mothername = mothername;
        this.permanentaddress = permanentaddress;
        this.correspondanceaddress = correspondanceaddress;
        this.aadhardetail = aadhardetail;
        this.drivinglicence = drivinglicence;
        this.phonenumber = phonenumber;
        this.city = city;
        this.dateofjoining = dateofjoining;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDrivinglicence() {
        return drivinglicence;
    }

    public void setDrivinglicence(String drivinglicence) {
        this.drivinglicence = drivinglicence;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDateofjoining() {
        return dateofjoining;
    }

    public void setDateofjoining(String dateofjoining) {
        this.dateofjoining = dateofjoining;
    }
}
