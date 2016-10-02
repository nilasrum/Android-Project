package com.example.mursalin.onlinequizv03;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SetUpExamBackgroundTask extends AsyncTask<String,Void,String>{


    ExamDetails activity;
    Context contex;
    String examname,starttime,duration,examdate,pass;


    SetUpExamBackgroundTask(ExamDetails activity,Context contex){
        this.activity = activity;
        this.contex = contex;
    }

    @Override
    protected String doInBackground(String... params) {
        examname = params[0];
        examdate = params[1];
        starttime = params[2];
        duration = params[3];
        pass = params[4];
        String exam_url = LoginActivity.serverip+"/examdetails.php";


        try {
            URL url = new URL(exam_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("examname", "UTF-8") + "=" + URLEncoder.encode(examname, "UTF-8") + "&" +
                    URLEncoder.encode("examdate", "UTF-8") + "=" + URLEncoder.encode(examdate, "UTF-8") + "&" +
                    URLEncoder.encode("starttime", "UTF-8") + "=" + URLEncoder.encode(starttime, "UTF-8")+"&"+
                    URLEncoder.encode("duration", "UTF-8") + "=" + URLEncoder.encode(duration, "UTF-8")+"&"+
                    URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
            bw.write(data);
            bw.flush();
            bw.close();
            os.close();

            InputStream response = httpURLConnection.getInputStream();
            response.close();
            return "Exam Setup Completed";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        activity.progressDialog.dismiss();
        Toast.makeText(contex,"Exam Setup Completed",Toast.LENGTH_LONG).show();
        JasonExamListParser jasonExamListParser = new JasonExamListParser(contex);
        jasonExamListParser.execute(examdate,"Admin");

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
