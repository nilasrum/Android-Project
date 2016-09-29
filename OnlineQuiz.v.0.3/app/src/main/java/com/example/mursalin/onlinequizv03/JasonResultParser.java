package com.example.mursalin.onlinequizv03;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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


public class JasonResultParser extends AsyncTask<String, Void, String> {

    UserHomeActivity activity;
    Context context;
    String id;

    JasonResultParser(Context context,UserHomeActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    JasonResultParser(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String exam_url = LoginActivity.serverip + "/result_list.php";
        String examtable = params[0];
        Log.i("talat","coming to res parcing");

        try {
            URL url = new URL(exam_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("examtable", "UTF-8") + "=" + URLEncoder.encode(examtable, "UTF-8");
            bw.write(data);
            bw.flush();
            bw.close();
            os.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((response = bufferedReader.readLine()) != null) {
                stringBuilder.append(response+"\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.i("talat","it is ok if it COMES HERE");
        //activity.jasonmsg.append(result);
        Intent reslistpage = new Intent(context,DisplayResListActivity.class);
        reslistpage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reslistpage.putExtra("jason",result);
        context.startActivity(reslistpage);

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
