package com.mai.nix.maiapp.simple_list_fragments;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mai.nix.maiapp.R;
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
    protected LiveData<List<StudentOrgModel>> simpleListLiveData;
    protected List<StudentOrgModel> mOrgModels;
    protected ApplicationViewModel mApplicationViewModel;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected SimpleListAdapter mSimpleListAdapter;
    protected RecyclerView mSimpleListRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_orgs_layout, container, false);
        mSwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mSimpleListRecyclerView = v.findViewById(R.id.studListRecyclerView);
        mSwipeRefreshLayout.setRefreshing(true);
        mApplicationViewModel = ViewModelProviders.of(SimpleListFragment.this)
                .get(ApplicationViewModel.class);
        mOrgModels = new ArrayList<>();
        mSimpleListAdapter = new SimpleListAdapter();
        mSimpleListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mSimpleListRecyclerView.setAdapter(mSimpleListAdapter);
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
