package com.example.mursalin.onlinequizv03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ListAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Exams object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        ExamHolder examHolder;
        if(row==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.exam_layout,parent,false);
            examHolder = new ExamHolder();
            examHolder.text_name = (TextView) row.findViewById(R.id.examname);
            examHolder.text_date = (TextView)row.findViewById(R.id.examtime);
            row.setTag(examHolder);
        }
        else{
            examHolder = (ExamHolder) row.getTag();
        }
        Exams exams = (Exams) this.getItem(position);
        examHolder.text_name.setText(exams.getExamname());
        examHolder.text_date.setText(exams.getExamTime());
        return row;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    static class ExamHolder{

        TextView text_name,text_date;
    }
}
