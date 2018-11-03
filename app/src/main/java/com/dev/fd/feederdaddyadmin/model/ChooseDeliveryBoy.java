package com.dev.fd.feederdaddyadmin.model;

public class ChooseDeliveryBoy {

    String currentload, name,dbphone;

    public ChooseDeliveryBoy() {
    }

    public ChooseDeliveryBoy(String currentload, String name, String dbphone) {
        this.currentload = currentload;
        this.name = name;
        this.dbphone = dbphone;
    }

    public String getDbphone() {
        return dbphone;
    }

    public void setDbphone(String dbphone) {
        this.dbphone = dbphone;
    }

    public String getCurrentload() {
        return currentload;
    }

    public void setCurrentload(String currentload) {
        this.currentload = currentload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
