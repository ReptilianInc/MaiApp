package com.mai.nix.maiapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.mai.nix.maiapp.model.SubjectHeader;
import com.mai.nix.maiapp.repositories.SubjectsRepository;
import java.util.List;

public class SubjectsViewModel extends ViewModel {
    private MutableLiveData<List<SubjectHeader>> mSubjects = new MutableLiveData<>();
    private SubjectsRepository mSubjectsRepository;

    public void initRepository(String link) {
        if (mSubjectsRepository == null) {
            mSubjectsRepository = new SubjectsRepository(link);
        } else {
            mSubjectsRepository.setLink(link);
        }
    }

    public LiveData<List<SubjectHeader>> getData() {
        loadData();
        return mSubjects;
    }

    private void loadData() {
        mSubjectsRepository.loadData(new SubjectsRepository.LoadSubjectsCallback() {
            @Override
            public void onLoad(List<SubjectHeader> subjects) {
                mSubjects.postValue(subjects);
            }
        });
    }
}
