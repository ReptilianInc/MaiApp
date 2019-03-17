package com.mai.nix.maiapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mai.nix.maiapp.model.StudentOrgModel;
import com.mai.nix.maiapp.model.SubjectHeader;
import com.mai.nix.maiapp.repositories.StudentOrgsRepository;
import com.mai.nix.maiapp.repositories.SubjectsRepository;
import java.util.List;

public class SubjectsViewModel extends ViewModel {
    private MutableLiveData<List<SubjectHeader>> mSubjects = new MutableLiveData<>();
    private MutableLiveData<List<StudentOrgModel>> mStudentOrgs = new MutableLiveData<>();
    private SubjectsRepository mSubjectsRepository;
    private StudentOrgsRepository mStudentOrgsRepository;

    public void initSubjectsRepository(String link) {
        if (mSubjectsRepository == null) {
            mSubjectsRepository = new SubjectsRepository(link);
        } else {
            mSubjectsRepository.setLink(link);
        }
    }

    public LiveData<List<StudentOrgModel>> getStudentOrgsLiveData() {
        if (mStudentOrgsRepository == null) {
            mStudentOrgsRepository = new StudentOrgsRepository();
            loadStudentOrgsData();
        }
        return mStudentOrgs;
    }

    public void loadStudentOrgsData() {
        mStudentOrgsRepository.loadData(new StudentOrgsRepository.LoadOrgsCallback() {
            @Override
            public void onLoad(List<StudentOrgModel> subjects) {
                mStudentOrgs.postValue(subjects);
            }
        });
    }

    public LiveData<List<SubjectHeader>> getData() {
        mSubjectsRepository.loadData(new SubjectsRepository.LoadSubjectsCallback() {
            @Override
            public void onLoad(List<SubjectHeader> subjects) {
                mSubjects.postValue(subjects);
            }
        });
        return mSubjects;
    }

    public LiveData<List<SubjectHeader>> getCachedSubjectsData() {
        mSubjectsRepository.loadCachedDataOrLoad(new SubjectsRepository.LoadSubjectsCallback() {
            @Override
            public void onLoad(List<SubjectHeader> subjects) {
                mSubjects.postValue(subjects);
            }
        });
        return mSubjects;
    }
}
