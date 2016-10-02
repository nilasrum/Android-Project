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

    /*boolean isValid(int pos) {

        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String curdate = String.valueOf(date)+"/"+ String.valueOf(month+1)+"/"+String.valueOf(year);
        Log.i("talat","cur-date : "+curdate);
        if(!(PassExamInfo[pos].getExdate().equals(curdate)))return false;
        TimeConversion timeConversion = new TimeConversion();
        int examtime, curtime, lasttime;
        String time = PassExamInfo[pos].extime;
        examtime = timeConversion.ConvertToMin(time);
        lasttime = examtime+PassExamInfo[pos].getExdur();
        curtime = c.get(Calendar.HOUR_OF_DAY);
        curtime = curtime * 60 + c.get(Calendar.MINUTE);
        Log.i("talat",curtime+" "+lasttime);
        if(curtime>=examtime && curtime<=lasttime) return true;
        return false;
    }*/
    public  String ConvertTime(String time) throws ParseException{
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date date = parseFormat.parse(time);
        String str = displayFormat.format(date );
        //System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
        return str;
    }
    public  long getTimeDiff(String dateStart , String dateStop) throws ParseException{
//        String dateStart = "11/03/11 11:30 PM";
       // dateStart = ConvertTime(dateStart) ;
//        System.out.println(dateStart);
//        String dateStop = "11/03/15 00:30:00";
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
        }
        long d = d2.getTime() - d1.getTime();
        long diffMinutes = d / (60 * 1000) % 60;
        long diffHours = d / (60 * 60 * 1000);
        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
        System.out.println("Time in hours: " + diffHours + " hours.");
        long TotalDiff = diffMinutes + 60*diffHours ;
        return TotalDiff;
    }
    public  int isValid(int pos) throws ParseException{//int pos
        String StartTime;
        StartTime = PassExamInfo[pos].getExdate() + " " + PassExamInfo[pos].extime;
        // dd/MM/yyyy HH:mm a
         long dur = (long)PassExamInfo[pos].getExdur();
        StartTime = ConvertTime(StartTime);
        //long dur = 250;
        //StartTime = "02/10/2016 20:25";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String CurTime = dateFormat.format(date);
        long d = getTimeDiff(StartTime , CurTime) ;
        System.out.println("time diff "+ d + " dur " + dur);
        int ret = 0;
        if(d >= 0 && d <= dur)ret = 1;
        if(d < 0)ret = 0;
        if(d > dur)ret = 2;
        return ret;
    }

}
