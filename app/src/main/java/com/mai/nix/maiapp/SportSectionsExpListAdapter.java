package com.mai.nix.maiapp;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mai.nix.maiapp.model.SportSectionsHeaders;

import java.util.ArrayList;

/**
 * Created by Nix on 11.08.2017.
 */

public class SportSectionsExpListAdapter extends BaseExpandableListAdapter {
    private ArrayList<SportSectionsHeaders> mHeaders;
    private Context mContext;

    public SportSectionsExpListAdapter(Context context, ArrayList<SportSectionsHeaders> headers) {
        mHeaders = headers;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mHeaders.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mHeaders.get(i).getBodies().size();
    }

    @Override
    public Object getGroup(int i) {
        return mHeaders.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mHeaders.get(i).getBodies().get(i1);
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sport_section_header, viewGroup, false);
        }
        TextView textView = (TextView)view.findViewById(R.id.title);
        textView.setText(mHeaders.get(i).getTitle());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sport_section_body, viewGroup, false);
        }
        TextView textChild1 = (TextView) view.findViewById(R.id.title);
        TextView textChild2 = (TextView) view.findViewById(R.id.owner);
        TextView textChild3 = (TextView) view.findViewById(R.id.phone);
        textChild1.setText(mHeaders.get(i).getBodies().get(i1).getTitle());
        textChild2.setText(Html.fromHtml(mHeaders.get(i).getBodies().get(i1).getOwner()));
        if(mHeaders.get(i).getBodies().get(i1).getPhoneEtc() != null){
            textChild3.setText(mHeaders.get(i).getBodies().get(i1).getPhoneEtc());
        }else{
            textChild3.setHeight(0);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
