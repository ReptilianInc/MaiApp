package com.mai.nix.maiapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mai.nix.maiapp.model.SportSectionsHeader;
import com.mai.nix.maiapp.model.StudentOrgModel;
import com.mai.nix.maiapp.model.SubjectHeader;
import com.mai.nix.maiapp.repositories.BarracksRepository;
import com.mai.nix.maiapp.repositories.CafesRepository;
import com.mai.nix.maiapp.repositories.LibrariesRepository;
import com.mai.nix.maiapp.repositories.SportSectionsRepository;
import com.mai.nix.maiapp.repositories.StudentOrgsRepository;
import com.mai.nix.maiapp.repositories.SubjectsRepository;
import com.mai.nix.maiapp.repositories.WorkersAndGradsRepository;

import java.util.List;

public class ApplicationViewModel extends ViewModel {
    private MutableLiveData<List<SubjectHeader>> mSubjects = new MutableLiveData<>();
    private MutableLiveData<List<StudentOrgModel>> mStudentOrganizations = new MutableLiveData<>();
    private MutableLiveData<List<StudentOrgModel>> mWorkersAndGradsOrganizations = new MutableLiveData<>();
    private MutableLiveData<List<StudentOrgModel>> mCafes = new MutableLiveData<>();
    private MutableLiveData<List<SportSectionsHeader>> mBarracks = new MutableLiveData<>();
    private MutableLiveData<List<SportSectionsHeader>> mLibraries = new MutableLiveData<>();
    private MutableLiveData<List<SportSectionsHeader>> mSportSections = new MutableLiveData<>();

    private SubjectsRepository mSubjectsRepository;
    private StudentOrgsRepository mStudentOrganisationsRepository;
    private WorkersAndGradsRepository mWorkersAndGradsRepository;
    private CafesRepository mCafesRepository;
    private BarracksRepository mBarracksRepository;
    private LibrariesRepository mLibrariesRepository;
    private SportSectionsRepository mSportSectionsRepository;

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

    public LiveData<List<SportSectionsHeader>> getBarracksLiveData() {
        if (mBarracksRepository == null) {
            mBarracksRepository = new BarracksRepository();
            loadBarracksData();
        }
        return mBarracks;
    }

    public LiveData<List<SportSectionsHeader>> getLibrariesLiveData() {
        if (mLibrariesRepository == null) {
            mLibrariesRepository = new LibrariesRepository();
            loadLibrariesData();
        }
        return mLibraries;
    }

    public LiveData<List<SportSectionsHeader>> getSportSectionsLiveData() {
        if (mSportSectionsRepository == null) {
            mSportSectionsRepository = new SportSectionsRepository();
            loadSportSectionsData();
        }
        return mSportSections;
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

    public void loadBarracksData() {
        mBarracksRepository.loadData(new BarracksRepository.LoadBarracksCallback() {
            @Override
            public void loadBarracks(List<SportSectionsHeader> barracks) {
                mBarracks.postValue(barracks);
            }
        });
    }

    public void loadSportSectionsData() {
        mSportSectionsRepository.loadData(new SportSectionsRepository.LoadSportSectionsCallback() {
            @Override
            public void loadSportSections(List<SportSectionsHeader> sportSections) {
                mSportSections.postValue(sportSections);
            }
        });
    }

    public void loadLibrariesData() {
        mLibrariesRepository.loadData(new LibrariesRepository.LoadLibrariesCallback() {
            @Override
            public void loadLibraries(List<SportSectionsHeader> libraries) {
                mLibraries.postValue(libraries);
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
