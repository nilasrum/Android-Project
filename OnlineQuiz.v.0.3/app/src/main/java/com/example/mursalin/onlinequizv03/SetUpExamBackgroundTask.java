package com.example.mursalin.onlinequizv03;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SetUpExamBackgroundTask extends AsyncTask<String,Void,String>{


    ExamDetails activity;
    Context contex;
    String examname,starttime,duration,examdate,pass,path;
    String email,pas;
    int f;


    SetUpExamBackgroundTask(ExamDetails activity,Context contex,String email,String pass){
        this.activity = activity;
        this.contex = contex;
        this.email = email;
        this.pas = pass;
    }

    @Override
    protected String doInBackground(String... params) {
        examname = params[0];
        examdate = params[1];
        starttime = params[2];
        duration = params[3];
        pass = params[4];
        path = params[5];
        f=0;
        String exam_url = LoginActivity.serverip+"/examdetails.php";

        //in exam_info
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
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String nline = "";
            while ((nline = bufferedReader.readLine()) != null) {
                response += nline;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            f=1;
            e.printStackTrace();
        }

        //upload ques
        File sdcard = Environment.getExternalStorageDirectory();
        Log.i("talat","in background");
        File file = new File(path);
        Log.i("talat","table name : "+examname);
        exam_url = LoginActivity.serverip+"/insert_ques.php";

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line="",ques="",op1="",op2="",op3="",op4="",ans="";
            int flag=0;

            while ((line = br.readLine()) != null) {
                flag++;
                if(flag==1)ques=line;
                if(flag==2)op1=line;
                if(flag==3)op2=line;
                if(flag==4)op3=line;
                if(flag==5)op4=line;
                if(flag==6){
                    flag=0;
                    ans=line;
                    Log.i("talat",ques+op1+op2+op3+op4+ans);
                    try {
                        URL url = new URL(exam_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        OutputStream os = httpURLConnection.getOutputStream();
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        String data = URLEncoder.encode("examname", "UTF-8") + "=" + URLEncoder.encode(examname, "UTF-8") + "&" +
                                URLEncoder.encode("ques", "UTF-8") + "=" + URLEncoder.encode(ques, "UTF-8") + "&" +
                                URLEncoder.encode("op1", "UTF-8") + "=" + URLEncoder.encode(op1, "UTF-8")+"&"+
                                URLEncoder.encode("op2", "UTF-8") + "=" + URLEncoder.encode(op2, "UTF-8")+"&"+
                                URLEncoder.encode("op3", "UTF-8") + "=" + URLEncoder.encode(op3, "UTF-8")+"&"+
                                URLEncoder.encode("op4", "UTF-8") + "=" + URLEncoder.encode(op4, "UTF-8")+"&"+
                                URLEncoder.encode("ans", "UTF-8") + "=" + URLEncoder.encode(ans, "UTF-8");
                        bw.write(data);
                        bw.flush();
                        bw.close();
                        os.close();

                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                        String response = "";
                        String nline = "";
                        while ((nline = bufferedReader.readLine()) != null) {
                            response += nline;
                        }

                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                    } catch (IOException e) {
                        f=1;
                        e.printStackTrace();

                    }
                }
            }
            br.close();
        }
        catch (Exception e) {
            //need to add proper error handling here
            f=1;
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(f==1){
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(activity,R.style.AlertDialogCustom).create();
            alertDialog.setMessage("Connection Failed");
            alertDialog.show();
            return;
        }
        activity.progressDialog.dismiss();
        Toast.makeText(contex,"Exam Setup Completed",Toast.LENGTH_LONG).show();
        JasonExamListParser jasonExamListParser = new JasonExamListParser(contex);
        jasonExamListParser.email = email;
        jasonExamListParser.pass = pas;
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
