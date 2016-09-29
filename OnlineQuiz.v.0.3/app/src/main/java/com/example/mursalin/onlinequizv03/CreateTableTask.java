package com.example.mursalin.onlinequizv03;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class CreateTableTask extends AsyncTask<String,Void,String> {

    String tablename;

    @Override
    protected String doInBackground(String... params) {

        String new_table_url = LoginActivity.serverip+"/new_res_par_table.php";
        tablename = params[0];

        try {
            URL url = new URL(new_table_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("tablename", "UTF-8") + "=" + URLEncoder.encode(tablename, "UTF-8");
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
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
