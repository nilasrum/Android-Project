package com.example.mursalin.onlinequizv03;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AdminHomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView navigationView;
    Button setexambutton,searchexambutton,changeAdmin;
    TextView searchdate;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private String email,pass;

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
        Bundle info = getIntent().getExtras();
        email = info.getString("email");
        pass = info.getString("pass");
        changeAdmin = (Button)findViewById(R.id.changeAdmin);
        changeAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminChangeButtonClicked();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.admin_home_navigationView_id);
        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.nav_head_admin);
        text.setText("Admin");


        //datepicker
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //app drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.admin_home_drawer_layout_id);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);


        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(Exception e){

        }
        navigationView = (NavigationView)findViewById(R.id.admin_home_navigationView_id);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.navigation_home_admin :
                        Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_logout_admin :
                        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_SHORT).show();
                        Intent loginpage = new Intent(AdminHomeActivity.this,LoginActivity.class);
                        startActivity(loginpage);
                        return true;
                    default: return true;
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }

    void AdminChangeButtonClicked(){
        Intent adminchange = new Intent(AdminHomeActivity.this,ChangeAdminActivity.class);
        adminchange.putExtra("email",email);
        adminchange.putExtra("pass",pass);
        startActivity(adminchange);
    }

    void SetExamButtonClicked(){
        Intent exampage = new Intent(AdminHomeActivity.this,ExamDetails.class);
        exampage.putExtra("email",email);
        exampage.putExtra("pass",pass);
        startActivity(exampage);
    }

    void SearchExamButtonClicked(){
        String date = searchdate.getText().toString();
        JasonExamListParser jasonExamListParser = new JasonExamListParser(AdminHomeActivity.this);
        jasonExamListParser.email=email;
        jasonExamListParser.pass = pass;
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
