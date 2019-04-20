package com.mai.nix.maiapp.simple_list_fragments;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.mai.nix.maiapp.model.StudentOrgModel;

import java.util.List;

/**
 * Created by Nix on 14.08.2017.
 */

public class CafesFragment extends SimpleListFragment {

    @Override
    public void setObserve() {
        simpleListLiveData = mApplicationViewModel.getCafesLiveData();
        simpleListLiveData.observe(CafesFragment.this, new Observer<List<StudentOrgModel>>() {
            @Override
            public void onChanged(@Nullable List<StudentOrgModel> studentOrgModels) {
                mSwipeRefreshLayout.setRefreshing(false);
                mOrgModels.clear();
                mOrgModels.addAll(studentOrgModels);
            }
        });

    }

    @Override
    public void releaseThread() {
        mApplicationViewModel.loadCafesData();
    }
}
