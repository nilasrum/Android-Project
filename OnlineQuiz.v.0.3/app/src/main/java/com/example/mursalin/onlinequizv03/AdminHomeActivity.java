package com.example.mursalin.onlinequizv03;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class AdminHomeActivity extends AppCompatActivity {


    Button setexambutton,searchexambutton;
    TextView searchdate;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        searchdate = (TextView) findViewById(R.id.searchdate);
        setexambutton = (Button) findViewById(R.id.setexambutton);
        setexambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetExamButtonClicked();
            }
        });
        searchexambutton = (Button) findViewById(R.id.searchexambutton);
        searchexambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchExamButtonClicked();
            }
        });

        //datepicker
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //showDate(year, month+1, day);
    }

    /*@Override
    public void onBackPressed() {
        //
    }*/

    void SetExamButtonClicked(){
        Intent exampage = new Intent(AdminHomeActivity.this,ExamDetails.class);
        startActivity(exampage);
    }

    void SearchExamButtonClicked(){
        String date = searchdate.getText().toString();
        JasonExamListParser jasonExamListParser = new JasonExamListParser(AdminHomeActivity.this);
        jasonExamListParser.execute(date,"Admin");
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
        searchdate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
