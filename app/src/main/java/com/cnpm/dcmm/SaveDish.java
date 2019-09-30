package com.cnpm.dcmm;

public class SaveDish {
        private String emailuser;
        private String namedish;
    public SaveDish(){}
    public SaveDish(String emailuser, String namedish) {
        this.emailuser = emailuser;
        this.namedish = namedish;
    }

    public String getEmailuser() {
        return emailuser;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public String getNamedish() {
        return namedish;
    }

    public void setNamedish(String namedish) {
        this.namedish = namedish;
    }


}
