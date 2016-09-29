package com.example.mursalin.onlinequizv03;


import android.util.Log;

public class StoredExamInfo {

    String exname,exdate,extime,exdur,expass;

    public StoredExamInfo(String exname, String exdate, String extime, String exdur,String expass) {
        this.exdate = exdate;
        this.exdur = exdur;
        this.extime = extime;
        this.exname = exname;
        this.expass = expass;
    }

    public String getExdate() {
        return exdate;
    }

    public String getExpass() {
        return expass;
    }

    public int getExdur() {
        int ret = Integer.valueOf(exdur);
        return  ret;
    }

    public String getExname() {
        return exname;
    }

    public String getExtime() {
        return extime;
    }
}
