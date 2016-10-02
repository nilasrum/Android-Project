package com.example.mursalin.onlinequizv03;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class UserHomeActivity extends AppCompatActivity {

    private DrawerLayout userhomeDL;
    private ActionBarDrawerToggle userhomeDT;
    private NavigationView userhomeNV;

    TextView userid;
    Button viewexambutton;
    EditText examdate;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        userid = (TextView) findViewById(R.id.userid);
        Bundle usenname = getIntent().getExtras();
        id = usenname.getString("userid");
        userid.append(" " + id);
        viewexambutton = (Button) findViewById(R.id.viewexambutton);
        viewexambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewExamButtonClicked();
            }
        });

        //date picker
        examdate = (EditText)findViewById(R.id.examdateid);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //drawer part
        userhomeDL = (DrawerLayout)findViewById(R.id.userhomeDL_id);
        userhomeDT = new ActionBarDrawerToggle(this,userhomeDL,R.string.open,R.string.close);

        userhomeDL.addDrawerListener(userhomeDT);
        userhomeDT.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userhomeNV = (NavigationView)findViewById(R.id.userhome_NV_id);
        userhomeNV.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                userhomeDL.closeDrawers();
                switch (item.getItemId()){
                    case R.id.navigation_home_admin :
                        return true;
                    case R.id.navigation_logout_admin :
                        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_SHORT).show();
                        Intent loginpage = new Intent(UserHomeActivity.this,LoginActivity.class);
                        startActivity(loginpage);
                        return true;
                    default: return true;
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(userhomeDT.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    void ViewExamButtonClicked() {
        String date = examdate.getText().toString();
        JasonExamListParser jasonExamListParser = new JasonExamListParser(this, UserHomeActivity.this);
        jasonExamListParser.execute(date,id);
    }


    //date picker

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
