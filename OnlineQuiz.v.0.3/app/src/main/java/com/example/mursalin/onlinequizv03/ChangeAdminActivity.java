package com.example.mursalin.onlinequizv03;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeAdminActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private AdminHomeActivity activity;
    private String email,password;
    private EditText eemail,epass,eold;
    Button btn;

    //public ChangeAdminActivity(AdminHomeActivity activity) {
      //  this.activity = activity;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_admin);

        Bundle info = getIntent().getExtras();
        email = info.getString("email");
        password = info.getString("pass");

        btn = (Button)findViewById(R.id.ACButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeButtonClicked();
            }
        });

        //app drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.admin_home_navigationView_id);
        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.nav_head_admin);
        text.setText("Admin");

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
                        Intent homepage = new Intent(ChangeAdminActivity.this,AdminHomeActivity.class);
                        homepage.putExtra("email",email);
                        homepage.putExtra("pass",password);
                        startActivity(homepage);
                        return true;
                    case R.id.navigation_logout_admin :
                        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_SHORT).show();
                        Intent loginpage = new Intent(ChangeAdminActivity.this,LoginActivity.class);
                        startActivity(loginpage);
                        return true;
                    default: return true;
                }

            }
        });

    }

    void ChangeButtonClicked(){
        String nemail,npass,opass;
        eemail = (EditText)findViewById(R.id.newEmail);
        nemail = eemail.getText().toString();
        epass = (EditText)findViewById(R.id.newPassword);
        npass = epass.getText().toString();
        eold = (EditText)findViewById(R.id.oldPassword);
        opass = eold.getText().toString();
        if(opass.equals(password)){
            ProgressDialog progressDialog = new ProgressDialog(ChangeAdminActivity.this,R.style.MyTheme_dialog);
            progressDialog.setMessage("updating...please wait");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            ChangeAdminTask changeAdminTask = new ChangeAdminTask(getApplicationContext(),progressDialog);
            changeAdminTask.execute(email,password,nemail,npass);
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.AlertDialogCustom).create();
            alertDialog.setMessage("Wrong Password");
            alertDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
