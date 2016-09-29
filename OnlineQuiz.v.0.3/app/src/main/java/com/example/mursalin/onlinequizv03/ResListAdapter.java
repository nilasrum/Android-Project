package com.example.mursalin.onlinequizv03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ResListAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ResListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Result object) {
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
        ResHolder resHolder;
        if(row==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.res_layout,parent,false);
            resHolder = new ResHolder();
            resHolder.text_reg = (TextView) row.findViewById(R.id.reg);
            resHolder.text_ans = (TextView)row.findViewById(R.id.ans);
            row.setTag(resHolder);
        }
        else{
            resHolder = (ResHolder) row.getTag();
        }
        Result result = (Result) this.getItem(position);
        resHolder.text_reg.setText(result.getReg());
        resHolder.text_ans.setText(result.getAns());
        return row;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    static class ResHolder{

        TextView text_reg,text_ans;
    }
}
