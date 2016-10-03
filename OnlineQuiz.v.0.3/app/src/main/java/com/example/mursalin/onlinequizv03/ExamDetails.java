package com.example.mursalin.onlinequizv03;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class ExamDetails extends AppCompatActivity {


    private DrawerLayout exdrawerLayout;
    private ActionBarDrawerToggle exactionBarDrawerToggle;
    private NavigationView exnavigationView;

    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 998;

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
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseQues();
            }
        });

        //app drawer
        exdrawerLayout = (DrawerLayout)findViewById(R.id.exdetails_d_l_id);
        exactionBarDrawerToggle = new ActionBarDrawerToggle(this,exdrawerLayout,R.string.open,R.string.close);

        exdrawerLayout.addDrawerListener(exactionBarDrawerToggle);
        exactionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        exnavigationView = (NavigationView)findViewById(R.id.admin_home_navigationView_id);
        exnavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                exdrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.navigation_home_admin :
                        Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(ExamDetails.this,AdminHomeActivity.class);
                        startActivity(home);
                        return true;
                    case R.id.navigation_logout_admin :
                        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_SHORT).show();
                        Intent loginpage = new Intent(ExamDetails.this,LoginActivity.class);
                        startActivity(loginpage);
                        return true;
                    default: return true;
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(exactionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void BrowseQues(){
        new FileChooser(ExamDetails.this).setFileListener(new FileChooser.FileSelectedListener() {
            @Override public void fileSelected(final File file) {
                // do something with the file
            }}).showDialog();
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

    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Log.i("talat","at least here");
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        if(id==998){
            Log.i("talat","hi baby");
            return new TimePickerDialog(this,
                    timePickerListener, hour, minute,false);
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

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    //set current time into text
                    starttime.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)).toString());

                }
            };
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}

