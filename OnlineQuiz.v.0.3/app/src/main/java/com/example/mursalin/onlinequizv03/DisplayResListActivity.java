package com.example.mursalin.onlinequizv03;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayResListActivity extends AppCompatActivity  {


    String jason_str;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ResListAdapter reslistAdapter;
    ListView listView;
    Context context;
    StoredExamInfo storedExamInfo[];

    String reg,ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_res_list);
        //storedExamInfo = new StoredExamInfo[100];
        jason_str = getIntent().getExtras().getString("jason");
        //regid = getIntent().getExtras().getString("id");
        reslistAdapter = new ResListAdapter(this, R.layout.res_layout);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(reslistAdapter);
        context = this.getApplicationContext();
        Log.i("talat","????");
        try {
            jsonObject = new JSONObject(jason_str);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            Log.i("talat","????????????????");
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                reg = JO.getString("reg");
                ans = JO.getString("ans");

                //storedResInfo[count] = new StoredResInfo(reg, ans);
                Log.i("talat",reg+" lets see "+ans);

                Result result = new Result(reg, ans);
                reslistAdapter.add(result);
                count++;
            }

            if (count == 0) {
                //Toast.makeText(DisplayExamListActivity.this, "No Exam Found", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
