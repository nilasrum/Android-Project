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

public class ParticipateQueryTask extends AsyncTask<String, Void, String> {

    String id;
    Context context;
    LoginActivity loginActivity;
    DisplayExamListActivity activity;
    String flag = "00000000000000000000000000000000000000000000000000";
    String tablename;

    public ParticipateQueryTask(Context context, DisplayExamListActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {

        String par_url = loginActivity.serverip + "/parquery.php";
        id = params[0];
        tablename = params[1];

        try {
            URL url = new URL(par_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
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
        if (res.equals("new student")) {

            Toast.makeText(context, "Password Accepted", Toast.LENGTH_SHORT).show();
            JsonLoadExamQues jsonLoadExamQues = new JsonLoadExamQues(context, activity,0,flag);
            jsonLoadExamQues.execute(id,tablename);

        } else {

            AlreadyAnsweredTask alreadyAnsweredTask = new AlreadyAnsweredTask(activity,context);
            alreadyAnsweredTask.execute(id,tablename);

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
