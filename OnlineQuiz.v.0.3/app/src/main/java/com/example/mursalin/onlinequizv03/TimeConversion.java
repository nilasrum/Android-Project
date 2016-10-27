package com.example.mursalin.onlinequizv03;


import android.util.Log;

public class TimeConversion {


    int ConvertToMin(String time){
        Log.i("talat",time);
        int ret=0;
        String temp;
        temp = time.substring(0,time.lastIndexOf(':'));
        int hh,mm;
        hh = Integer.parseInt(temp);
        temp = time.substring(time.lastIndexOf(':')+1,time.length());
        Log.i("talat","min "+temp);
        mm = Integer.parseInt(temp);
        ret = hh*60+mm;
        return  ret;
    }

}
