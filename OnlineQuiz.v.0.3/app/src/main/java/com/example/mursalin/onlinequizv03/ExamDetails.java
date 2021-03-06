package com.example.mursalin.onlinequizv03;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    String ename,edate,estarttime,eduration,ebrowse,epass,qpath;
    public ProgressDialog progressDialog;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private File quesfile;
    private String email,pass;

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

        Bundle info = getIntent().getExtras();
        email = info.getString("email");
        pass = info.getString("pass");
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
                        home.putExtra("email",email);
                        home.putExtra("pass",pass);
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

        String release = Build.VERSION.RELEASE;
        Log.i("talat","*"+release);
        if(release.startsWith("6")){

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }

        }else{

            new FileChooser(ExamDetails.this,getApplicationContext()).setFileListener(new FileChooser.FileSelectedListener() {
                @Override public void fileSelected(final File file) {
                    browse.setText(file.getAbsolutePath());
                    quesfile = file;
                }}).showDialog();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new FileChooser(ExamDetails.this,getApplicationContext()).setFileListener(new FileChooser.FileSelectedListener() {
                        @Override public void fileSelected(final File file) {
                            browse.setText(file.getAbsolutePath());
                            quesfile = file;
                        }}).showDialog();

                } else {

                }
                return;
            }

        }

    }

    void SetUpExamButtonClicked(){
        ename = examname.getText().toString();
        edate = examdate.getText().toString();
        estarttime = starttime.getText().toString();
        eduration = duration.getText().toString();
        epass = exampass.getText().toString();
        qpath = browse.getText().toString();
        if(ename.isEmpty() || edate.isEmpty() || estarttime.isEmpty() || eduration.isEmpty() || epass.isEmpty() || qpath.isEmpty()){
            Toast.makeText(getApplicationContext(),"please fill up all feild",Toast.LENGTH_SHORT).show();
            return;
        }
        if(ename.contains(" ")){
            Toast.makeText(getApplicationContext(),"space not allowed in exam name",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(ExamDetails.this, R.style.MyTheme_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Setting up...");
        progressDialog.show();

        SetUpExamBackgroundTask setUpExamBackgroundTask = new SetUpExamBackgroundTask(this,ExamDetails.this,email,pass);
        setUpExamBackgroundTask.execute(ename,edate,estarttime,eduration,epass,qpath);

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
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        if(id==998){
            return new TimePickerDialog(this,
                    timePickerListener, hour, minute,false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
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
            return String.valueOf(c);
    }

}

