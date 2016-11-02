package com.example.mursalin.onlinequizv03;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionPageActivity extends AppCompatActivity {


    String ques[] = new String[100];
    String opt1[] = new String[100];
    String opt2[] = new String[100];
    String opt3[] = new String[100];
    String opt4[] = new String[100];
    String ans[] = new String[100];
    String flag,id,tablename;
    int cur, tot, nxt, prv;

    TextView question, curno, totno;
    CheckBox option1, option2, option3, option4;
    Button prevbutton, submitbutton, nextbutton;

    @Override
    public void onBackPressed() {
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);

        ques = getIntent().getExtras().getStringArray("ques");
        opt1 = getIntent().getExtras().getStringArray("opt1");
        opt2 = getIntent().getExtras().getStringArray("opt2");
        opt3 = getIntent().getExtras().getStringArray("opt3");
        opt4 = getIntent().getExtras().getStringArray("opt4");
        ans = getIntent().getExtras().getStringArray("ans");
        cur = getIntent().getExtras().getInt("cur");
        tot = getIntent().getExtras().getInt("tot");
        flag = getIntent().getExtras().getString("flag");
        id = getIntent().getExtras().getString("id");
        tablename = getIntent().getExtras().getString("tablename");
        //nxt = cur+1;
        //prv = cur-1;

        //set no
        curno = (TextView) findViewById(R.id.curno);
        curno.setText(String.valueOf(cur + 1));
        totno = (TextView) findViewById(R.id.totno);
        totno.setText(String.valueOf(tot));

        question = (TextView) findViewById(R.id.question);
        question.setText(ques[cur]);

        option1 = (CheckBox) findViewById(R.id.option1);
        option1.setText(opt1[cur]);
        if(opt1[cur].equals("@"))option1.setVisibility(View.GONE);

        option2 = (CheckBox) findViewById(R.id.option2);
        option2.setText(opt2[cur]);
        if(opt2[cur].equals("@"))option2.setVisibility(View.GONE);

        option3 = (CheckBox) findViewById(R.id.option3);
        option3.setText(opt3[cur]);
        if(opt3[cur].equals("@"))option3.setVisibility(View.GONE);

        option4 = (CheckBox) findViewById(R.id.option4);
        option4.setText(opt4[cur]);
        if(opt4[cur].equals("@"))option4.setVisibility(View.GONE);

        prevbutton = (Button) findViewById(R.id.prevbutton);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrevButtonClicked();
            }
        });


        nextbutton = (Button) findViewById(R.id.nextbutton);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextButtonClicked();
            }
        });


        submitbutton = (Button) findViewById(R.id.submitbutton);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitButtonClicked();
            }
        });

    }

    void PrevButtonClicked() {
        //set no
        prv = 0;
        for (prv = cur-1; prv >= 0; prv--) {
            if (flag.charAt(prv) == '0') break;
        }
        //Log.i("talat","looking for prev "+prv);
        if(prv<0)return;
        if (prv == 0 && flag.charAt(0) != '0') {

            Toast.makeText(getApplicationContext(),"No Prevoius Question To Ans",Toast.LENGTH_SHORT).show();

        } else {
            Intent temp = new Intent(this, TempQuestionActivity.class);
            temp.putExtra("ques", ques);
            temp.putExtra("opt1", opt1);
            temp.putExtra("opt2", opt2);
            temp.putExtra("opt3", opt3);
            temp.putExtra("opt4", opt4);
            temp.putExtra("ans",ans);
            temp.putExtra("cur", prv);
            temp.putExtra("tot", tot);
            temp.putExtra("flag",flag);
            temp.putExtra("id",id);
            temp.putExtra("tablename",tablename);
            startActivity(temp);
        }
    }

    void NextButtonClicked() {
        nxt =0;
        for(nxt =cur+1;nxt<tot;nxt++){
            if(flag.charAt(nxt)=='0')break;
        }
        if(nxt>=tot)return;
        if (nxt == tot-1 && flag.charAt(nxt)!='0'){
            Toast.makeText(getApplicationContext(), "No next Question to ans", Toast.LENGTH_LONG).show();
        }
        else {
            //set no
            Intent temp = new Intent(this, TempQuestionActivity.class);
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
            startActivity(temp);
        }
    }

    void SubmitButtonClicked() {

        char ansflag = getAns();
        StringBuilder nflag = new StringBuilder(flag);
        nflag.setCharAt(cur, ansflag);

        //update server
        UpdateAnswerTask updateAnswerTask = new UpdateAnswerTask();
        updateAnswerTask.ques = ques;
        updateAnswerTask.opt1 = opt1;
        updateAnswerTask.opt2 = opt2;
        updateAnswerTask.opt3 = opt3;
        updateAnswerTask.opt4 = opt4;
        updateAnswerTask.ans = ans;
        updateAnswerTask.cur = cur;
        updateAnswerTask.tot = tot;
        updateAnswerTask.tablename = tablename;
        updateAnswerTask.context = getApplicationContext();
        updateAnswerTask.flag = nflag.toString();

        Log.i("talat","Questionpage activity end");
        updateAnswerTask.execute(id);

    }

    public char getAns(){
        int ret = 0;
        String ckans="";
        if(option1.isChecked()){
            ret+=1;
            ckans = option1.getText().toString();
        }
        Log.i("talat","QQQ 1");
        if(option2.isChecked()){
            ret+=1;
            ckans = option2.getText().toString();
        }
        Log.i("talat","QQQ 2");
        if(option3.isChecked()){
            ret+=1;
            ckans = option3.getText().toString();
        }
        Log.i("talat","QQQ 3");
        if(option4.isChecked()){
            ret+=1;
            ckans = option4.getText().toString();
        }
        Log.i("talat","Q page just look here "+ans[cur]);
        if(ret>=2 || ret==0 )return '2';
        if(ckans.equals(ans[cur]))return '1';
        return '2';
    }

}

