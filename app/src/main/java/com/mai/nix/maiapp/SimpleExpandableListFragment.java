package com.mai.nix.maiapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.mai.nix.maiapp.model.SportSectionsHeaders;
import java.util.ArrayList;

/**
 * Created by Nix on 13.09.2017.
 */

public abstract class SimpleExpandableListFragment extends Fragment {
    protected abstract void releaseThread();
    protected ExpandableListView mExpandableListView;
    protected ArrayList<SportSectionsHeaders> mHeaders;
    protected SportSectionsExpListAdapter mAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exp_list_test, container, false);
        mExpandableListView = (ExpandableListView)v.findViewById(R.id.exp);
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mHeaders = new ArrayList<>();
        mAdapter = new SportSectionsExpListAdapter(getContext(), mHeaders);
        releaseThread();
        mExpandableListView.setAdapter(mAdapter);
        for(int i = 0; i < mHeaders.size(); i++){
            mExpandableListView.expandGroup(i);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                releaseThread();
            }
        });
        return v;
    }
}
