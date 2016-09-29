package com.example.mursalin.onlinequizv03;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends AppCompatActivity {

    public Button loginbutton;
    public TextView link;
    public ProgressDialog progressDialog;
    private String email, password;
    public static String serverip = "http://10.0.2.2/android";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButtonAction();
            }
        });
        link = (TextView) findViewById(R.id.signuplink);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupButtonAction();
            }
        });
    }

    void LoginButtonAction(){
        EditText e = (EditText) findViewById(R.id.email);
        email = e.getText().toString();
        EditText p = (EditText) findViewById(R.id.password);
        password = p.getText().toString();


        if (!isValid()) return;


        // authentication logic

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyTheme_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        checkDatabase(email, password);
    }

    public void SignupButtonAction() {
        Intent signuppage;
        signuppage = new Intent(this, SignupActivity.class);
        startActivity(signuppage);
    }


    public boolean isValid() {
        boolean valid = true;
        String vemail, vpassword;
        EditText e = (EditText) findViewById(R.id.email);
        vemail = e.getText().toString();
        EditText p = (EditText) findViewById(R.id.password);
        vpassword = p.getText().toString();

        if (vemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(vemail).matches()) {
            e.setError("enter a valid email address");
            valid = false;
        } else {
            e.setError(null);
        }

        if (vpassword.isEmpty() || vpassword.length() < 4 || vpassword.length() > 10) {
            p.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            p.setError(null);
        }

        return valid;
    }

    public void checkDatabase(String email, String pass) {

        CheckForAdmin checkForAdmin = new CheckForAdmin(getApplicationContext(),LoginActivity.this);
        checkForAdmin.execute(email,pass);

    }

}
