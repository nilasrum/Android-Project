package com.example.mursalin.onlinequizv03;


public class Exams {

    String examname,time;

    public Exams(String examname,String time) {
        this.examname = examname;
        this.time = time;
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

    public void setTime(String time) {
        this.time = time;
    }
}
