package com.example.mursalin.onlinequizv03;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class ExamDetails extends AppCompatActivity {

    EditText examname,examdate,starttime,duration,browse,exampass;
    Button setexambutton;
    String ename,edate,estarttime,eduration,ebrowse,epass;
    ProgressDialog progressDialog;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_details);
        examname = (EditText) findViewById(R.id.examname);
        examdate = (EditText) findViewById(R.id.examdate);
        starttime = (EditText) findViewById(R.id.starttime);
        duration = (EditText) findViewById(R.id.duration);
        browse = (EditText) findViewById(R.id.browse);
        exampass = (EditText) findViewById(R.id.exampass);
        setexambutton = (Button) findViewById(R.id.setexambutton);
        setexambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetUpExamButtonClicked();
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //showDate(year, month+1, day);
    }

    void SetUpExamButtonClicked(){
        ename = examname.getText().toString();
        edate = examdate.getText().toString();
        estarttime = starttime.getText().toString();
        eduration = duration.getText().toString();
        epass = exampass.getText().toString();

        progressDialog = new ProgressDialog(ExamDetails.this, R.style.MyTheme_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Stting up...");
        progressDialog.show();

        SetUpExamBackgroundTask setUpExamBackgroundTask = new SetUpExamBackgroundTask(this,ExamDetails.this);
        setUpExamBackgroundTask.execute(ename,edate,estarttime,eduration,epass);

        //upload file

        //progressDialog.dismiss();

        //create result table
        CreateTableTask createTableTask = new CreateTableTask();
        createTableTask.execute(ename);

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        examdate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

}

