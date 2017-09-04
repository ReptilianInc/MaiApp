package com.mai.nix.maiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.mai.nix.maiapp.model.SubjectHeader;
import java.util.ArrayList;

/**
 * Created by Nix on 02.08.2017.
 */

public class SubjectsExpListAdapter extends BaseExpandableListAdapter {
    private ArrayList<SubjectHeader> mGroups;
    private Context mContext;

    public SubjectsExpListAdapter(Context context, ArrayList<SubjectHeader> groups) {
        mContext = context;
        mGroups = groups;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public int getChildrenCount(int i) {
        return mGroups.get(i).getChildren().size();
    }

    @Override
    public Object getGroup(int i) {
        return mGroups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mGroups.get(i).getChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_view, null);
        }
        TextView text1 = (TextView) view.findViewById(R.id.group_date);
        TextView text2 = (TextView)view.findViewById(R.id.group_day);
        text1.setText(mGroups.get(i).getDate());
        text2.setText(mGroups.get(i).getDay());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_view, viewGroup, false);
        }

        TextView textChild1 = (TextView) view.findViewById(R.id.subject_title);
        TextView textChild2 = (TextView) view.findViewById(R.id.subject_teacher);
        TextView textChild3 = (TextView) view.findViewById(R.id.subject_time);
        TextView textChild4 = (TextView) view.findViewById(R.id.subject_type);
        TextView textChild5 = (TextView) view.findViewById(R.id.subject_room);
        textChild1.setText(mGroups.get(i).getChildren().get(i1).getTitle());
        textChild2.setText(mGroups.get(i).getChildren().get(i1).getTeacher());
        textChild3.setText(mGroups.get(i).getChildren().get(i1).getTime());
        textChild4.setText(mGroups.get(i).getChildren().get(i1).getType());
        textChild5.setText(mGroups.get(i).getChildren().get(i1).getRoom());
        return view;
    }
}
