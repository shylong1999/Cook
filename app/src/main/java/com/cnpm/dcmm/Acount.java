package com.cnpm.dcmm;

import java.util.ArrayList;

public class Acount {
    private String email,uid,image;
    private ArrayList<String> saved;

    public Acount(String email, String uid, String image, ArrayList<String> saved) {
        this.email = email;
        this.uid = uid;
        this.image = image;
        this.saved = saved;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getSaved() {
        return saved;
    }

    public void setSaved(ArrayList<String> saved) {
        this.saved = saved;
    }
}
