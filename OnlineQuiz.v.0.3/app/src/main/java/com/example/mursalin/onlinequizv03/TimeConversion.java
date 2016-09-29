package com.example.mursalin.onlinequizv03;


import android.util.Log;

public class TimeConversion {


    int ConvertToMin(String time){
        int ret=0;
        boolean am,pm;
        am=false;
        pm = false;
        if(time.endsWith("AM"))am=true;
        if(time.endsWith("PM"))pm=true;
        String temp;
        temp = time.substring(0,time.lastIndexOf(':'));
        int hh,mm;
        hh = Integer.parseInt(temp);

        if(am)temp = time.substring(time.lastIndexOf(':')+1,time.lastIndexOf('A')-1);
        else temp = time.substring(time.lastIndexOf(':')+1,time.lastIndexOf('P')-1);
        mm = Integer.parseInt(temp);
        if(am){
            if(hh==12)hh=0;
            ret = hh*60+mm;
        }
        else
        {
            if(hh !=12)hh+=12;
            ret = hh*60+mm;
        }
        return  ret;
    }

}
