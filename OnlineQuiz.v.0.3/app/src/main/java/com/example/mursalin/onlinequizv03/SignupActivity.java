package com.example.mursalin.onlinequizv03;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignupActivity extends AppCompatActivity {

    public TextView link;
    public Button signupbutton;
    public EditText signupemail,signuppassword,regnum;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        regnum = (EditText) findViewById(R.id.regnum);
        signupemail = (EditText) findViewById(R.id.signupemail);
        signuppassword = (EditText) findViewById(R.id.signuppassword);

        signupbutton = (Button)findViewById(R.id.signupbutton);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupButtonAction();
            }
        });
        link = (TextView) findViewById(R.id.loginlink);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginLinkAction();
            }
        });
    }

    void SignupButtonAction(){

        String mail = signupemail.getText().toString();
        progressDialog = new ProgressDialog(SignupActivity.this, R.style.MyTheme_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        if(!isValid()){
            progressDialog.dismiss();
            return;
        }

        SignUpTask signUpTask= new SignUpTask(this,SignupActivity.this);
        signUpTask.execute(mail);
    }

    void loginLinkAction(){
        Intent page = new Intent(this,LoginActivity.class);
        startActivity(page);
    }

    public boolean isValid() {
        boolean valid = true;
        String vemail, vpassword,vreg;
        EditText r = (EditText)findViewById(R.id.regnum);
        vreg = r.getText().toString();
        EditText e = (EditText) findViewById(R.id.signupemail);
        vemail = e.getText().toString();
        EditText p = (EditText) findViewById(R.id.signuppassword);
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
        if ((vreg.length()!=10) || (!vreg.startsWith("2"))){
            r.setError("Invalid Reg.Number format");
            valid = false;
        }else{
            r.setError(null);
        }

        return valid;
    }

}
