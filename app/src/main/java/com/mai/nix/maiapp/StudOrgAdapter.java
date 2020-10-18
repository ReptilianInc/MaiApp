package com.mai.nix.maiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mai.nix.maiapp.model.SimpleListModel;
import java.util.ArrayList;

/**
 * Created by Nix on 10.08.2017.
 */

public class StudOrgAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SimpleListModel> mOrgModels;
    public StudOrgAdapter(Context context, ArrayList<SimpleListModel> models) {
        mContext = context;
        mOrgModels = models;
    }

    @Override
    public int getCount() {
        return mOrgModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mOrgModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_simple_list_item, viewGroup, false);
        }

        TextView text1 = (TextView)view.findViewById(R.id.title);
        TextView text2 = (TextView)view.findViewById(R.id.leader);
        TextView text3 = (TextView)view.findViewById(R.id.address);
        TextView text4 = (TextView)view.findViewById(R.id.phone);
        text1.setText(mOrgModels.get(i).getTitle());
        text2.setText(mOrgModels.get(i).getLeader());
        text3.setText(mOrgModels.get(i).getAddress());
        if(mOrgModels.get(i).getPhone() != null){
            text4.setText(mOrgModels.get(i).getPhone());
        }else{
            text4.setHeight(0);
        }

        return view;
    }
}
