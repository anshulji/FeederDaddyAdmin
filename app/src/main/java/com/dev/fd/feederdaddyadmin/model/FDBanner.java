package com.dev.fd.feederdaddyadmin.model;

public class FDBanner {

    private  String image, name;

    public FDBanner() {
    }

    public FDBanner(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
