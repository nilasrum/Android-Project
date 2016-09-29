package com.example.mursalin.onlinequizv03;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UpdateParticipateTask extends AsyncTask<String, Void, String> {

    LoginActivity loginActivity;
    String id;
    Context context;
    DisplayExamListActivity activity;

    public UpdateParticipateTask(Context context, DisplayExamListActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {

        String par_url = loginActivity.serverip + "/parupdate.php";
        id = params[0];

        try {
            URL url = new URL(par_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            bw.write(data);
            bw.flush();
            bw.close();
            os.close();

            InputStream response = httpURLConnection.getInputStream();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context,"Password Accepted",Toast.LENGTH_SHORT).show();
        //JsonLoadExamQues jsonLoadExamQues = new JsonLoadExamQues(context,activity,0);
        //jsonLoadExamQues.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
