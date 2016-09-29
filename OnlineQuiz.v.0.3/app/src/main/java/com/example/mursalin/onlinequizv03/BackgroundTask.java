package com.example.mursalin.onlinequizv03;


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


public class BackgroundTask extends AsyncTask<String, Void, String> {

    Context ctx;
    AlertDialog alertDialog;
    String email, pass, reg;
    LoginActivity loginActivity;
    SignupActivity signupActivity;

    BackgroundTask(Context ctx, LoginActivity activity) {
        this.ctx = ctx;
        this.loginActivity = activity;
    }

    BackgroundTask(Context ctx, SignupActivity activity) {
        this.ctx = ctx;
        this.signupActivity = activity;
    }


    @Override
    protected String doInBackground(String... params) {

        String reg_url = loginActivity.serverip + "/register.php";
        String login_url = loginActivity.serverip + "/login.php";
        String task = params[0];
        if (task.equals("signup")) {
            reg = params[1];
            email = params[2];
            pass = params[3];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("reg", "UTF-8") + "=" + URLEncoder.encode(reg, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                bw.write(data);
                bw.flush();
                bw.close();
                os.close();

                InputStream response = httpURLConnection.getInputStream();
                response.close();
                return "Registration Completed";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (task.equals("login")) {

            email = params[1];
            pass = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
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

                e.printStackTrace();
            }


        }
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        if (res.equals("Registration Completed")) {
            Toast.makeText(ctx, "Registration Completed", Toast.LENGTH_LONG).show();
            signupActivity.progressDialog.dismiss();
            Intent npage = new Intent(ctx, UserHomeActivity.class);
            npage.putExtra("userid", reg);
            npage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(npage);
        } else {

            if (res.equals("log in failed...try again")) {
                alertDialog.setMessage(res);
                alertDialog.show();
            } else {

                String id = res.substring(5, 15);
                //Log.i("talat","this is "+id);
                loginActivity.progressDialog.dismiss();
                Toast.makeText(ctx, "log in successful", Toast.LENGTH_SHORT).show();
                Intent upage = new Intent(ctx, UserHomeActivity.class);
                upage.putExtra("userid", id);
                upage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(upage);

            }
        }
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
