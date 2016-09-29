package com.example.mursalin.onlinequizv03;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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


public class JsonLoadExamQues extends AsyncTask<String, Void, String> {

    DisplayExamListActivity activity;
    Context context;
    String flag,id,tablename;
    int cur;

    public JsonLoadExamQues(Context context, DisplayExamListActivity activity, int cur,String flag) {
        this.context = context;
        this.activity = activity;
        this.cur = cur;
        this.flag = flag;
    }


    @Override
    protected String doInBackground(String... params) {


        String exam_url = LoginActivity.serverip + "/get_ques.php";
        id = params[0];
        tablename = params[1];

        try {
            URL url = new URL(exam_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("tablename", "UTF-8") + "=" + URLEncoder.encode(tablename, "UTF-8");
            bw.write(data);
            bw.flush();
            bw.close();
            os.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response +="\n"+ line;
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
    protected void onPostExecute(String result) {

        DatabaseToArray databaseToArray = new DatabaseToArray(result);
        databaseToArray.Extraction();

        if (databaseToArray.count < cur) {
            Toast.makeText(context, "All Question Answered", Toast.LENGTH_LONG).show();
            Intent homepage = new Intent(context, UserHomeActivity.class);
            homepage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(homepage);
        } else {

            Intent exampage = new Intent(context, QuestionPageActivity.class);
            exampage.putExtra("ques", databaseToArray.ques);
            exampage.putExtra("opt1", databaseToArray.opt1);
            exampage.putExtra("opt2", databaseToArray.opt2);
            exampage.putExtra("opt3", databaseToArray.opt3);
            exampage.putExtra("opt4", databaseToArray.opt4);
            exampage.putExtra("ans" , databaseToArray.ans);
            exampage.putExtra("cur", cur);
            exampage.putExtra("tot", databaseToArray.count);
            exampage.putExtra("flag",flag);
            exampage.putExtra("id",id);
            exampage.putExtra("tablename",tablename);
            exampage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(exampage);
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
