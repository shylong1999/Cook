package com.cnpm.dcmm;

public class Acount{
            private String tk;
            private String usename;
            public Acount(){}

    public Acount(String tk, String usename) {
        this.tk = tk;
        this.usename = usename;
    }

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String stt) {
        this.usename = stt;
    }
}

