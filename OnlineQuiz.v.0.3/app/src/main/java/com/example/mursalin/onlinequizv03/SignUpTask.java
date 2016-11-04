package com.example.mursalin.onlinequizv03;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

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


public class SignUpTask extends AsyncTask<String, Void, String> {

    Context ctx;
    SignupActivity signupActivity;
    int flag;


    SignUpTask(Context ctx, SignupActivity activity) {
        this.ctx = ctx;
        this.signupActivity = activity;
    }


    @Override
    protected String doInBackground(String... params) {

        flag=0;
        String login_url = LoginActivity.serverip+"/emailcheck.php";
        String email = params[0];

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            bw.write(data);
            bw.flush();
            bw.close();
            os.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return response;

        } catch (IOException e) {

            flag=1;
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        //Log.i("talat",res+" "+"error");
        if(flag==1){
            signupActivity.progressDialog.setCancelable(true);
            signupActivity.progressDialog.dismiss();
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(ctx,R.style.AlertDialogCustom).create();
            alertDialog.setMessage("Connection Failed");
            alertDialog.show();
            return;
        }
        if (res.startsWith("match")) {

            signupActivity.progressDialog.dismiss();
            Toast.makeText(ctx,"Invalid Email",Toast.LENGTH_LONG);
            signupActivity.signupemail.setError("Email already registered");
            return;

        } else {

            String reg,email,pass;
            reg = signupActivity.regnum.getText().toString();
            email = signupActivity.signupemail.getText().toString();
            pass = signupActivity.signuppassword.getText().toString();

            BackgroundTask backgroundtask = new BackgroundTask(ctx,signupActivity);
            backgroundtask.execute("signup",reg,email,pass);

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
