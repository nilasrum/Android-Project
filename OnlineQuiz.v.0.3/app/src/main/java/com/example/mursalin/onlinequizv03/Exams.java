package com.example.mursalin.onlinequizv03;


public class Exams {

    String examname,time,date;

    public Exams(String examname,String time,String date) {
        this.examname = examname;
        this.time = time;
        this.date=date;
    }

    public String getExamname() {
        return examname;
    }

    public String getExamTime(){
        return time;
    }
    public void setExamname(String examname) {
        this.examname = examname;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
