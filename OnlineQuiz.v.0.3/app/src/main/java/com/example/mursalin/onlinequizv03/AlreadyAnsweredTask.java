package com.example.mursalin.onlinequizv03;


import android.content.Context;
import android.os.AsyncTask;
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

public class AlreadyAnsweredTask extends AsyncTask<String, Void, String> {

    DisplayExamListActivity activity;
    Context context;
    String userid,tablename;
    LoginActivity loginActivity;

    public AlreadyAnsweredTask(DisplayExamListActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        userid = params[0];
        tablename = params[1];
        String ans_url = loginActivity.serverip + "/answer_query.php";
        try {
            URL url = new URL(ans_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8")+ "&" +
                    URLEncoder.encode("tablename", "UTF-8") + "=" + URLEncoder.encode(tablename, "UTF-8");
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
        return null;
    }

    @Override
    protected void onPostExecute(String res) {

        int i = 0;
        for (i = 0; i < res.length(); i++) {
            if (res.charAt(i) == '0') break;
        }
        Toast.makeText(context, "Password Accepted", Toast.LENGTH_SHORT).show();
        JsonLoadExamQues jsonLoadExamQues = new JsonLoadExamQues(context, activity, i, res);
        jsonLoadExamQues.execute(userid,tablename);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
