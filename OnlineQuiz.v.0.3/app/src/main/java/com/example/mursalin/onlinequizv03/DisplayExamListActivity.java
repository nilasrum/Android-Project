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

public class DisplayExamListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    String jason_str;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListAdapter listAdapter;
    ListView listView;
    Context context;
    StoredExamInfo storedExamInfo[];
    String regid;

    String examname, examdate, starttime, duration, password;
    String popdate,popexam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_exam_list);
        storedExamInfo = new StoredExamInfo[100];
        jason_str = getIntent().getExtras().getString("jason");
        regid = getIntent().getExtras().getString("id");
        listAdapter = new ListAdapter(this, R.layout.exam_layout);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter);
        context = this.getApplicationContext();
        try {
            jsonObject = new JSONObject(jason_str);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;

            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                examname = JO.getString("name");
                examdate = JO.getString("date");
                starttime = JO.getString("time");
                duration = JO.getString("duration");
                password = JO.getString("pass");

                storedExamInfo[count] = new StoredExamInfo(examname, examdate, starttime, duration, password);

                Exams exams = new Exams(examname, starttime);
                listAdapter.add(exams);
                count++;
            }

            if (count == 0) {
                Toast.makeText(DisplayExamListActivity.this, "No Exam Found", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("talat",String.valueOf( storedExamInfo[position].exname));


                if(regid.equals("Admin")){

                    ShowPopUpMenu(view,storedExamInfo[position].exname,storedExamInfo[position].exdate);

                }else {
                    PasswordDialog passwordDialog = new PasswordDialog();
                    passwordDialog.PassExamInfo = storedExamInfo;
                    passwordDialog.pos = position;
                    passwordDialog.activity = DisplayExamListActivity.this;
                    passwordDialog.context = getApplicationContext();
                    passwordDialog.id = regid;
                    passwordDialog.show(getSupportFragmentManager(), "exam_password");
                }
            }
        });


    }

    public void ShowPopUpMenu(View view,String xname,String xdate){

        popdate = xdate;
        popexam = xname;
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(DisplayExamListActivity.this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.pop_up_option_layout,popupMenu.getMenu());
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        //Log.i("talat", String.valueOf(item.getOrder()));

        switch (item.getItemId()){
            case R.id.popdelete :Log.i("talat","delete");Log.i("talat","delete this!!");
                DeleteExamTask deleteExamTask = new DeleteExamTask(DisplayExamListActivity.this,getApplicationContext());
                deleteExamTask.execute(popexam,popdate);
                Log.i("talat","after deleting");
                return true;
            case R.id.viewres : Log.i("talat","res");
                JasonResultParser jasonResultParser = new JasonResultParser(getApplicationContext());
                jasonResultParser.execute(popexam);
                return true;
            default:return false;
        }
    }


}
