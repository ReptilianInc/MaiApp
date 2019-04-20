package com.mai.nix.maiapp.simple_list_fragments;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import com.mai.nix.maiapp.model.StudentOrgModel;
import java.util.List;

/**
 * Created by Nix on 10.08.2017.
 */

public class StudentOrganisationsFragment extends SimpleListFragment {
    @Override
    public void releaseThread() {
        mApplicationViewModel.loadStudentOrganisationsData();
    }

    @Override
    public void setObserve() {
        simpleListLiveData = mApplicationViewModel.getStudentOrganisationsLiveData();
        simpleListLiveData.observe(StudentOrganisationsFragment.this, new Observer<List<StudentOrgModel>>() {
            @Override
            public void onChanged(@Nullable List<StudentOrgModel> studentOrgModels) {
                mSwipeRefreshLayout.setRefreshing(false);
                mSimpleListAdapter.setData(studentOrgModels);
                mSimpleListAdapter.notifyItemRangeInserted(0, studentOrgModels.size());
            }
        });
    }
}
