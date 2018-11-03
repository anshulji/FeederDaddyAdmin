package com.dev.fd.feederdaddyadmin.model;

public class CouponCode {

    private String discount,name;

    public CouponCode() {
    }

    public CouponCode(String discount, String name) {
        this.discount = discount;
        this.name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
