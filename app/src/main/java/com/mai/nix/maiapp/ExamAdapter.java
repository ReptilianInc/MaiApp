package com.mai.nix.maiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mai.nix.maiapp.model.ExamModel;
import java.util.ArrayList;

/**
 * Created by Nix on 17.08.2017.
 */

public class ExamAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ExamModel> mModels;
    public ExamAdapter(Context context, ArrayList<ExamModel> models) {
        mContext = context;
        mModels = models;
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_exam_list_item, viewGroup, false);
        }
        TextView textView1 = (TextView)view.findViewById(R.id.examDate);
        TextView textView2 = (TextView)view.findViewById(R.id.examDay);
        TextView textView3 = (TextView)view.findViewById(R.id.examTime);
        TextView textView4 = (TextView)view.findViewById(R.id.examTitle);
        TextView textView5 = (TextView)view.findViewById(R.id.examTeacher);
        TextView textView6 = (TextView)view.findViewById(R.id.examRoom);

        textView1.setText(mModels.get(i).getDate());
        textView2.setText(mModels.get(i).getDay());
        textView3.setText(mModels.get(i).getTime());
        textView4.setText(mModels.get(i).getTitle());
        textView5.setText(mModels.get(i).getTeacher());
        textView6.setText(mModels.get(i).getRoom());
        return view;
    }
}
