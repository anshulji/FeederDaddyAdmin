package com.dev.fd.feederdaddyadmin.model;
public class User{

    private String username,password, zone,phone,city;

    public User() {
    }

    public User(String username, String password, String restaurantname, String phone, String city) {
        this.username = username;
        this.password = password;
        this.zone = restaurantname;
        this.phone = phone;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUsername() {
        return username;

    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
