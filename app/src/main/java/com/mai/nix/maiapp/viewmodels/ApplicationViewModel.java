package com.mai.nix.maiapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mai.nix.maiapp.model.StudentOrgModel;
import com.mai.nix.maiapp.model.SubjectHeader;
import com.mai.nix.maiapp.repositories.CafesRepository;
import com.mai.nix.maiapp.repositories.StudentOrgsRepository;
import com.mai.nix.maiapp.repositories.SubjectsRepository;
import com.mai.nix.maiapp.repositories.WorkersAndGradsRepository;

import java.util.List;

public class ApplicationViewModel extends ViewModel {
    private MutableLiveData<List<SubjectHeader>> mSubjects = new MutableLiveData<>();
    private MutableLiveData<List<StudentOrgModel>> mStudentOrganizations = new MutableLiveData<>();
    private MutableLiveData<List<StudentOrgModel>> mWorkersAndGradsOrganizations = new MutableLiveData<>();
    private MutableLiveData<List<StudentOrgModel>> mCafes = new MutableLiveData<>();

    private SubjectsRepository mSubjectsRepository;
    private StudentOrgsRepository mStudentOrganisationsRepository;
    private WorkersAndGradsRepository mWorkersAndGradsRepository;
    private CafesRepository mCafesRepository;

    public void initSubjectsRepository(String link) {
        if (mSubjectsRepository == null) {
            mSubjectsRepository = new SubjectsRepository(link);
        } else {
            mSubjectsRepository.setLink(link);
        }
    }

    public LiveData<List<StudentOrgModel>> getStudentOrganisationsLiveData() {
        if (mStudentOrganisationsRepository == null) {
            mStudentOrganisationsRepository = new StudentOrgsRepository();
            loadStudentOrganisationsData();
        }
        return mStudentOrganizations;
    }

    public LiveData<List<StudentOrgModel>> getWorkersAndGradsLiveData() {
        if (mWorkersAndGradsRepository == null) {
            mWorkersAndGradsRepository = new WorkersAndGradsRepository();
            loadWorkersAndOrganisationsData();
        }
        return mWorkersAndGradsOrganizations;
    }

    public LiveData<List<StudentOrgModel>> getCafesLiveData() {
        if (mCafesRepository == null) {
            mCafesRepository = new CafesRepository();
            loadCafesData();
        }
        return mCafes;
    }

    public void loadStudentOrganisationsData() {
        mStudentOrganisationsRepository.loadData(new StudentOrgsRepository.LoadOrgsCallback() {
            @Override
            public void onLoad(List<StudentOrgModel> subjects) {
                mStudentOrganizations.postValue(subjects);
            }
        });
    }

    public void loadWorkersAndOrganisationsData() {
        mWorkersAndGradsRepository.loadData(new WorkersAndGradsRepository.LoadOrgsCallback() {
            @Override
            public void onLoad(List<StudentOrgModel> subjects) {
                mWorkersAndGradsOrganizations.postValue(subjects);
            }
        });
    }

    public void loadCafesData() {
        mCafesRepository.loadData(new CafesRepository.LoadCafesCallback() {
            @Override
            public void onLoad(List<StudentOrgModel> cafes) {
                mCafes.postValue(cafes);
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
