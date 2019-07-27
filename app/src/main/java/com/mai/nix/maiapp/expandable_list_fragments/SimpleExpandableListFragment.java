package com.mai.nix.maiapp.expandable_list_fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mai.nix.maiapp.R;
import com.mai.nix.maiapp.model.SportSectionsHeader;
import com.mai.nix.maiapp.viewmodels.ApplicationViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nix on 13.09.2017.
 */

public abstract class SimpleExpandableListFragment extends Fragment {
    protected abstract void releaseThread();
    protected abstract void setObserve();
    protected RecyclerView mExpandableRecyclerView;
    protected ApplicationViewModel mApplicationViewModel;
    protected LiveData<List<SportSectionsHeader>> mSportSectionsHeadersLiveData;
    protected ArrayList<SportSectionsHeader> mHeaders;
    protected ExpandableListAdapter mAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exp_list_test, container, false);
        mExpandableRecyclerView = v.findViewById(R.id.exp);
        mSwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mHeaders = new ArrayList<>();
        mAdapter = new ExpandableListAdapter();
        mAdapter.setData(mHeaders);
        mApplicationViewModel = ViewModelProviders.of(SimpleExpandableListFragment.this).get(ApplicationViewModel.class);
        mExpandableRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mExpandableRecyclerView.setAdapter(mAdapter);
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
