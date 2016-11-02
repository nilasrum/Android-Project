package com.example.mursalin.onlinequizv03;


import android.app.ProgressDialog;
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

public class ChangeAdminTask extends AsyncTask<String, Void, String> {

    Context context;
    String oemail, opass,nemail,npass;
    ProgressDialog progressDialog;
    int flag=0;

    ChangeAdminTask(Context ctx,ProgressDialog progressDialog) {
        this.context = ctx;
        this.progressDialog = progressDialog;
    }

    @Override
    protected String doInBackground(String... params) {

        String login_url = LoginActivity.serverip + "/admin_change.php";

        oemail = params[0];
        opass = params[1];
        nemail = params[2];
        npass = params[3];

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("oemail", "UTF-8") + "=" + URLEncoder.encode(oemail, "UTF-8") + "&" +
                    URLEncoder.encode("opass", "UTF-8") + "=" + URLEncoder.encode(opass, "UTF-8") + "&" +
                    URLEncoder.encode("nemail", "UTF-8") + "=" + URLEncoder.encode(nemail, "UTF-8") + "&" +
                    URLEncoder.encode("npass", "UTF-8") + "=" + URLEncoder.encode(npass, "UTF-8");
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

        } catch (Exception e) {
            flag =1;

        }

        return null;
    }

    @Override
    protected void onPostExecute(String res) {

        progressDialog.setCancelable(true);
        progressDialog.dismiss();
        if(flag==1)
        {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(context,R.style.AlertDialogCustom).create();
            alertDialog.setMessage("Connection Failed");
            alertDialog.show();
            return;
        }
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
        Intent apage = new Intent(context, AdminHomeActivity.class);
        apage.putExtra("email",nemail);
        apage.putExtra("pass",npass);
        apage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(apage);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}