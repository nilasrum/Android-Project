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
                    if (!isValid(pos)) {
                        Toast.makeText(context, "Cant Participate Now", Toast.LENGTH_SHORT).show();
                    } else {

                        ParticipateQueryTask participateQueryTask = new ParticipateQueryTask(context,activity);
                        participateQueryTask.execute(id,PassExamInfo[pos].getExname());
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

    boolean isValid(int pos) {

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
    }

}
