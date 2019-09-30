package com.cnpm.dcmm;

public class Dish {
    private String image;
    private String namedish;
    private String emailuser;
    private String imageuser;
    private String make;
    private String date;
    public  Dish(){}
    public Dish(String image, String namedish, String emailuser, String imageuser, String make, String date) {
        this.image = image;
        this.namedish = namedish;
        this.emailuser = emailuser;
        this.imageuser = imageuser;
        this.make = make;
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNamedish() {
        return namedish;
    }

    public void setNamedish(String namedish) {
        this.namedish = namedish;
    }

    public String getEmailuser() {
        return emailuser;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public String getImageuser() {
        return imageuser;
    }

    public void setImageuser(String imageuser) {
        this.imageuser = imageuser;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
