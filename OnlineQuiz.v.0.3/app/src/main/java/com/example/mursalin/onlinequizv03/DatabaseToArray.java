package com.example.mursalin.onlinequizv03;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseToArray {

    String jason_str;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String ques[] = new String [50];
    String opt1[] = new String [50];
    String opt2[] = new String [50];
    String opt3[] = new String [50];
    String opt4[] = new String [50];
    String ans[]  = new String [50];
    int count = 0;
    int cur;
    String tmp;


    public DatabaseToArray(String input) {
        jason_str = input;
        cur=0;
    }

    public void Extraction(){

        try {
            jsonObject = new JSONObject(jason_str);
            jsonArray = jsonObject.getJSONArray("question");
            count=0;

            while(count<jsonArray.length()){
                JSONObject JO = jsonArray.getJSONObject(count);
                tmp = JO.getString("ques");
                if(tmp.isEmpty())break;
                ques[count] = JO.getString("ques");
                opt1[count] = JO.getString("opt1");
                opt2[count] = JO.getString("opt2");
                opt3[count] = JO.getString("opt3");
                opt4[count] = JO.getString("opt4");
                ans[count]  = JO.getString("ans");
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
