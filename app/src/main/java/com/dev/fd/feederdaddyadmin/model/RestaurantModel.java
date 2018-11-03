package com.dev.fd.feederdaddyadmin.model;

public class RestaurantModel {

    String city,password,phone,restaurantid,restaurantname,username,restaurantaddress,restaurantareaname,dateofjoining,restauranttype;

    public RestaurantModel() {
    }

    public RestaurantModel(String city, String password, String phone, String restaurantid, String restaurantname, String username, String restaurantaddress, String restaurantareaname, String dateofjoining, String restauranttype) {
        this.city = city;
        this.password = password;
        this.phone = phone;
        this.restaurantid = restaurantid;
        this.restaurantname = restaurantname;
        this.username = username;
        this.restaurantaddress = restaurantaddress;
        this.restaurantareaname = restaurantareaname;
        this.dateofjoining = dateofjoining;
        this.restauranttype = restauranttype;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(String restaurantid) {
        this.restaurantid = restaurantid;
    }

    public String getRestaurantname() {
        return restaurantname;
    }

    public void setRestaurantname(String restaurantname) {
        this.restaurantname = restaurantname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestaurantaddress() {
        return restaurantaddress;
    }

    public void setRestaurantaddress(String restaurantaddress) {
        this.restaurantaddress = restaurantaddress;
    }

    public String getRestaurantareaname() {
        return restaurantareaname;
    }

    public void setRestaurantareaname(String restaurantareaname) {
        this.restaurantareaname = restaurantareaname;
    }

    public String getDateofjoining() {
        return dateofjoining;
    }

    public void setDateofjoining(String dateofjoining) {
        this.dateofjoining = dateofjoining;
    }

    public String getRestauranttype() {
        return restauranttype;
    }

    public void setRestauranttype(String restauranttype) {
        this.restauranttype = restauranttype;
    }
}

