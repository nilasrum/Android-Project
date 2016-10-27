package com.example.mursalin.onlinequizv03;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Calendar;

public class PasswordDialog extends DialogFragment{

    LayoutInflater inflater;
    View view;
    EditText input;
    String inppass;
    StoredExamInfo PassExamInfo[];
    Context context;
    DisplayExamListActivity activity;
    String id;
    int pos;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.passwordinputlayout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Exam Password");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input = (EditText) view.findViewById(R.id.input);
                inppass = input.getText().toString();
                if (inppass.equals(PassExamInfo[pos].getExpass())) {
                    int flag = 0;
                    try {
                        flag = isValid(pos);
                    }catch (Exception e){

                    }
                    if (flag==0) {
                        Toast.makeText(context, "Exam Has Not Started", Toast.LENGTH_SHORT).show();
                    } else if(flag==1) {

                        ParticipateQueryTask participateQueryTask = new ParticipateQueryTask(context,activity);
                        participateQueryTask.execute(id,PassExamInfo[pos].getExname());
                    }else if(flag==2){
                        JasonResultParser jasonResultParser = new JasonResultParser(context);
                        jasonResultParser.execute(PassExamInfo[pos].getExname(),id);
                    }
                } else {
                    Toast.makeText(context,"Password Mismatch",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    int isValid(int pos) {

        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String curdate = String.valueOf(date)+"/"+ String.valueOf(month+1)+"/"+String.valueOf(year);
        Log.i("talat","cur-date : "+curdate);
        if(!(PassExamInfo[pos].getExdate().equals(curdate))) {
            if(ExtractYear(PassExamInfo[pos].getExdate())<year)return 2;
            if(ExtractYear(PassExamInfo[pos].getExdate())>year)return 0;
            if(ExtractMonth(PassExamInfo[pos].getExdate())<(month+1))return 2;
            if(ExtractMonth(PassExamInfo[pos].getExdate())>(month+1))return 0;
            if(ExtractDay(PassExamInfo[pos].getExdate())<(date))return 2;
            if(ExtractDay(PassExamInfo[pos].getExdate())>(date))return 0;
        }
        Log.i("talat","???");
        TimeConversion timeConversion = new TimeConversion();
        int examtime, curtime, lasttime;
        String time = PassExamInfo[pos].extime;
        examtime = timeConversion.ConvertToMin(time);
        lasttime = examtime+PassExamInfo[pos].getExdur();
        curtime = c.get(Calendar.HOUR_OF_DAY);
        curtime = curtime * 60 + c.get(Calendar.MINUTE);
        Log.i("talat",curtime+" "+lasttime);
        if(curtime>=examtime && curtime<=lasttime) return 1;
        if(curtime>lasttime)return 2;
        if(curtime<examtime)return 0;
        return 1;
    }
    int ExtractDay(String date){
        String temp;
        temp = date.substring(0,date.indexOf('/'));
        Log.i("talat","day "+temp);
        return Integer.parseInt(temp);
    }
    int ExtractMonth(String date){
        String temp;
        temp = date.substring(date.indexOf('/')+1,date.lastIndexOf('/'));
        Log.i("talat","month "+temp);
        return Integer.parseInt(temp);
    }
    int ExtractYear(String date){
        String temp;
        temp = date.substring(date.lastIndexOf('/')+1,date.length());
        Log.i("talat","year+ "+temp);
        return Integer.parseInt(temp);
    }
}
