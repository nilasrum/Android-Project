package com.example.mursalin.onlinequizv03;

import android.content.Context;
import android.content.Intent;
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

public class TempUpdateAnsTask extends AsyncTask<String,Void,String> {

    String ques[] = new String[100];
    String opt1[] = new String[100];
    String opt2[] = new String[100];
    String opt3[] = new String[100];
    String opt4[] = new String[100];
    String ans[] = new String[100];
    String flag,id,tablename;
    int cur,tot;
    Context context;

    LoginActivity loginActivity;

    @Override
    protected String doInBackground(String... params) {

        String ans_update_url =  loginActivity.serverip + "/answer_update.php";
        id = params[0];

        //Log.i("talat","tempupdateans  activity Start");

        try {
            URL url = new URL(ans_update_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                    URLEncoder.encode("flag", "UTF-8") + "=" + URLEncoder.encode(flag, "UTF-8")+ "&" +
                    URLEncoder.encode("tablename", "UTF-8") + "=" + URLEncoder.encode(tablename, "UTF-8");
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
        //where to go
        int nxt=0,f=0;
        for(nxt = cur+1;nxt<tot;nxt++){
            if(flag.charAt(nxt)=='0'){
                f=1;
                break;
            }
        }
        if(nxt>=tot){
            nxt=tot-1;
            f=0;
        }
        if(nxt==tot-1 && flag.charAt(nxt)!='0'){
            for(nxt = cur-1;nxt>=0;nxt--){
                if(nxt<0)break;
                if(flag.charAt(nxt)=='0'){
                    f=1;
                    break;
                }
            }
        }
        if(f==1){

            Intent temp = new Intent(context, QuestionPageActivity.class);
            temp.putExtra("ques", ques);
            temp.putExtra("opt1", opt1);
            temp.putExtra("opt2", opt2);
            temp.putExtra("opt3", opt3);
            temp.putExtra("opt4", opt4);
            temp.putExtra("ans",ans);
            temp.putExtra("cur", nxt);
            temp.putExtra("tot", tot);
            temp.putExtra("flag",flag);
            temp.putExtra("id",id);
            temp.putExtra("tablename",tablename);
            temp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Log.i("talat","tempupdateans activity end");

            context.startActivity(temp);

        }else {

            Toast.makeText(context,"You Have Answered All Question",Toast.LENGTH_SHORT).show();
            Intent  homepage = new Intent(context,UserHomeActivity.class);
            homepage.putExtra("userid",id);
            homepage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(homepage);

        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
