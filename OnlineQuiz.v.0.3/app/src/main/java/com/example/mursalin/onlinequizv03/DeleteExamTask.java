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

public class DeleteExamTask extends AsyncTask<String, Void, String> {


    DisplayExamListActivity activity;
    Context contex;
    String delexamname, delexamdate;
    boolean f;


    DeleteExamTask(DisplayExamListActivity activity, Context contex,boolean f) {
        this.activity = activity;
        this.contex = contex;
        this.f=f;
    }

    @Override
    protected String doInBackground(String... params) {
        delexamname = params[0];
        delexamdate = params[1];

        String del_exam_url = LoginActivity.serverip + "/delete_exam.php";

        try {
            URL url = new URL(del_exam_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("delexamname", "UTF-8") + "=" + URLEncoder.encode(delexamname, "UTF-8") + "&" +
                    URLEncoder.encode("delexamdate", "UTF-8") + "=" + URLEncoder.encode(delexamdate, "UTF-8");
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
        Toast.makeText(contex, res, Toast.LENGTH_LONG).show();
        if (res.startsWith("Exam")) {
            JasonExamListParser jasonExamListParser = new JasonExamListParser(contex);
            if(f==true)jasonExamListParser.execute("","Admin");
            else jasonExamListParser.execute(delexamdate,"Admin");
            Log.i("talat","delete task");
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
