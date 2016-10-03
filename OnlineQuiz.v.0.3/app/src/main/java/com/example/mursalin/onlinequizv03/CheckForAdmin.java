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

public class CheckForAdmin extends AsyncTask<String, Void, String> {

    Context ctx;
    String email, pass;
    LoginActivity loginActivity;
    int flag=0;

    CheckForAdmin(Context ctx, LoginActivity activity) {
        this.ctx = ctx;
        this.loginActivity = activity;
    }


    @Override
    protected String doInBackground(String... params) {

        String login_url = loginActivity.serverip + "/admin_check.php";

        email = params[0];
        pass = params[1];

        Log.i("talat","is it sunday?");

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

            Log.i("talat","is it monday?");

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
            Log.i("talat","thn is it ok  "+response);
            return response;

        } catch (Exception e) {
            flag =1;
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String res) {

        if(flag==1)return;
        if(res.equals("admin")){

            Log.i("talat",res);
            Intent adminhome = new Intent(ctx,AdminHomeActivity.class);
            adminhome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(adminhome);
        }else if(res.equals("user")) {
            BackgroundTask backgroundTask;
            backgroundTask = new BackgroundTask(ctx,loginActivity);
            backgroundTask.execute("login", email, pass);
        }

    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}