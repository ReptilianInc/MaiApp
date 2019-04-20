package com.mai.nix.maiapp.simple_list_fragments;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import com.mai.nix.maiapp.model.StudentOrgModel;

import java.util.List;

/**
 * Created by Nix on 10.08.2017.
 */

public class StudentOrgsFragment extends SimpleListFragment {
    @Override
    public void releaseThread() {
        mApplicationViewModel.loadStudentOrganisationsData();
    }

    @Override
    public void setObserve() {
        simpleListLiveData = mApplicationViewModel.getStudentOrganisationsLiveData();
        simpleListLiveData.observe(StudentOrgsFragment.this, new Observer<List<StudentOrgModel>>() {
            @Override
            public void onChanged(@Nullable List<StudentOrgModel> studentOrgModels) {
                mSwipeRefreshLayout.setRefreshing(false);
                mOrgModels.clear();
                mOrgModels.addAll(studentOrgModels);
                mListView.setAdapter(mAdapter);
            }
        });
    }
}
