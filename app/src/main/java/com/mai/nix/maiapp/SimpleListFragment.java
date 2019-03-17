package com.mai.nix.maiapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.mai.nix.maiapp.model.StudentOrgModel;
import com.mai.nix.maiapp.viewmodels.ApplicationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nix on 13.09.2017.
 */

public abstract class SimpleListFragment extends Fragment {
    public abstract void releaseThread();
    public abstract void setObserve();
    protected ListView mListView;
    protected LiveData<List<StudentOrgModel>> simpleListLiveData;
    protected List<StudentOrgModel> mOrgModels;
    protected ApplicationViewModel mApplicationViewModel;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected StudOrgAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_orgs_layout, container, false);
        mSwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mListView = v.findViewById(R.id.stud_org_listview);
        mApplicationViewModel = ViewModelProviders.of(SimpleListFragment.this)
                .get(ApplicationViewModel.class);
        mOrgModels = new ArrayList<>();
        mAdapter = new StudOrgAdapter(getContext(), mOrgModels);
        setObserve();
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
