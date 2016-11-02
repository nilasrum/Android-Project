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

public class DisplayExamListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    private DrawerLayout disexdrawerLayout;
    private ActionBarDrawerToggle disexactionBarDrawerToggle;
    private NavigationView disexnavigationView;
    String jason_str;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListAdapter listAdapter;
    ListView listView;
    Context context;
    StoredExamInfo storedExamInfo[];
    String regid;
    boolean f;

    String examname, examdate, starttime, duration, password;
    String popdate,popexam;
    String email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f=false;
        setContentView(R.layout.activity_display_exam_list);
        Bundle info = getIntent().getExtras();
        email = info.getString("email");
        pass = info.getString("pass");
        storedExamInfo = new StoredExamInfo[100];
        jason_str = getIntent().getExtras().getString("jason");
        regid = getIntent().getExtras().getString("id");
        f = getIntent().getExtras().getBoolean("flag");
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

                Exams exams = new Exams(examname, starttime,examdate);
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

        //drawer part
        disexdrawerLayout = (DrawerLayout)findViewById(R.id.display_exam_list_drawer_layout);
        disexactionBarDrawerToggle = new ActionBarDrawerToggle(this,disexdrawerLayout,R.string.open,R.string.close);

        disexdrawerLayout.addDrawerListener(disexactionBarDrawerToggle);
        disexactionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        disexnavigationView = (NavigationView)findViewById(R.id.admin_home_navigationView_id);
        disexnavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                disexdrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.navigation_home_admin :
                        Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_SHORT).show();
                        if(regid.equals("Admin")){
                            Intent home = new Intent(DisplayExamListActivity.this,AdminHomeActivity.class);
                            home.putExtra("email",email);
                            home.putExtra("pass",pass);
                            startActivity(home);
                        }else{
                            Intent uhome = new Intent(DisplayExamListActivity.this,UserHomeActivity.class);
                            uhome.putExtra("userid", regid);
                            startActivity(uhome);
                        }

                        return true;
                    case R.id.navigation_logout_admin :
                        Toast.makeText(getApplicationContext(),"LogOut",Toast.LENGTH_SHORT).show();
                        Intent loginpage = new Intent(DisplayExamListActivity.this,LoginActivity.class);
                        startActivity(loginpage);
                        return true;
                    default: return true;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(regid.equals("Admin")){
            Intent homepage = new Intent(DisplayExamListActivity.this,AdminHomeActivity.class);
            homepage.putExtra("email",email);
            homepage.putExtra("pass",pass);
            startActivity(homepage);
        }else{
            Intent homepage = new Intent(DisplayExamListActivity.this,UserHomeActivity.class);
            homepage.putExtra("userid",regid);
            startActivity(homepage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(disexactionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
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
            case R.id.popdelete :
                DeleteDialog deleteDialog = new DeleteDialog();
                deleteDialog.activity = DisplayExamListActivity.this;
                deleteDialog.context = getApplicationContext();
                deleteDialog.f = f;
                deleteDialog.exam = popexam;
                deleteDialog.date  =popdate;
                deleteDialog.show(getSupportFragmentManager(), "delete_exam");
                return true;
            case R.id.viewres :
                JasonResultParser jasonResultParser = new JasonResultParser(getApplicationContext());
                jasonResultParser.email = email;
                jasonResultParser.pass = pass;
                jasonResultParser.execute(popexam,"Admin");
                return true;
            default:return false;
        }
    }


}
