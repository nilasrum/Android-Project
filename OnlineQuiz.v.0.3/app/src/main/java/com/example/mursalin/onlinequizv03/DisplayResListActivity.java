package com.example.mursalin.onlinequizv03;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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


    private DrawerLayout displayresDL;
    private ActionBarDrawerToggle displayresDT;
    private NavigationView displayresNV;
    String jason_str;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ResListAdapter reslistAdapter;
    ListView listView;
    Context context;

    String reg,ans,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_res_list);
        jason_str = getIntent().getExtras().getString("jason");
        id = getIntent().getExtras().getString("id");
        reslistAdapter = new ResListAdapter(this, R.layout.res_layout);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(reslistAdapter);
        context = this.getApplicationContext();
        try {
            jsonObject = new JSONObject(jason_str);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                reg = JO.getString("reg");
                ans = JO.getString("ans");
                Result result = new Result(reg, ans);
                reslistAdapter.add(result);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //drawer part
        displayresDL = (DrawerLayout)findViewById(R.id.displayresDL_id);
        displayresDT = new ActionBarDrawerToggle(this,displayresDL,R.string.open,R.string.close);
        displayresDL.addDrawerListener(displayresDT);
        displayresDT.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        displayresNV = (NavigationView)findViewById(R.id.displayres_NV_id);
        displayresNV.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                displayresDL.closeDrawers();
                switch (item.getItemId()){
                    case R.id.navigation_home_admin :
                        if(id.equals("Admin")){
                            Intent homeAd = new Intent(DisplayResListActivity.this,AdminHomeActivity.class);
                            startActivity(homeAd);
                        }else {
                            Intent homeUs = new Intent(DisplayResListActivity.this,UserHomeActivity.class);
                            homeUs.putExtra("userid",id);
                            startActivity(homeUs);
                        }
                        return true;
                    case R.id.navigation_logout_admin :
                        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_SHORT).show();
                        Intent loginpage = new Intent(DisplayResListActivity.this,LoginActivity.class);
                        startActivity(loginpage);
                        return true;
                    default: return true;
                }

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(displayresDT.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
