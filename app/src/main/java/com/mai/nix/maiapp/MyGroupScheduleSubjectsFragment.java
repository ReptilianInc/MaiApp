package com.mai.nix.maiapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import com.mai.nix.maiapp.model.SubjectHeader;
import com.mai.nix.maiapp.viewmodels.ApplicationViewModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Nix on 02.08.2017.
 */

public class MyGroupScheduleSubjectsFragment extends Fragment {
    private ExpandableListView mListView;
    private ArrayList<SubjectHeader> mGroups;
    private SubjectsExpListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Spinner mSpinner;
    private String mCurrentGroup, mCurrentLink;
    private int mCurrentDay, mCurrentWeek;
    private Calendar mCalendar;
    private final String mLink = "https://mai.ru/education/schedule/detail.php?group=";
    private String mWeek = "1";
    private final String PLUS_WEEK = "&week=";

    private ApplicationViewModel mApplicationViewModel;
    private LiveData<List<SubjectHeader>> mLiveData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shedule_subjects_layout, container, false);
        View header = inflater.inflate(R.layout.spinner_header, null);
        mCalendar = new GregorianCalendar();
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCurrentWeek = mCalendar.get(Calendar.WEEK_OF_MONTH);
        UserSettings.initialize(getContext());
        mGroups = new ArrayList<>();
        mAdapter = new SubjectsExpListAdapter(getContext(), mGroups);
        mCurrentGroup = UserSettings.getGroup(getContext());
        mSpinner = header.findViewById(R.id.spinner);
        mSwipeRefreshLayout = v.findViewById(R.id.swiperefresh);

        mApplicationViewModel = ViewModelProviders.of(MyGroupScheduleSubjectsFragment.this)
                .get(ApplicationViewModel.class);
        mCurrentLink = mLink.concat(mCurrentGroup);
        mApplicationViewModel.initSubjectsRepository(mCurrentLink);
        mLiveData = mApplicationViewModel.getCachedSubjectsData();

        mLiveData.observe(MyGroupScheduleSubjectsFragment.this, new Observer<List<SubjectHeader>>() {
            @Override
            public void onChanged(@Nullable List<SubjectHeader> subjectHeaders) {
                mSwipeRefreshLayout.setRefreshing(false);
                mGroups.clear();
                mGroups.addAll(subjectHeaders);
                mListView.setAdapter(mAdapter);
                for (int j = 0; j < mGroups.size(); j++) {
                    mListView.expandGroup(j);
                }
                Log.d("poisondart ", "onChanged");
            }
        });


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!((MainActivity) getActivity()).subjectsNeedToUpdate) {
                    if (i != 0) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        mWeek = Integer.toString(i);
                        mCurrentLink = mLink.concat(mCurrentGroup).concat(PLUS_WEEK).concat(mWeek);
                        mApplicationViewModel.initSubjectsRepository(mCurrentLink);
                        mLiveData = mApplicationViewModel.getCachedSubjectsData();
                    } else if (UserSettings.getSubjectsUpdateFrequency(getContext()).equals(UserSettings.EVERY_DAY) &&
                            UserSettings.getDay(getContext()) != mCurrentDay) {
                        UserSettings.setDay(getContext(), mCurrentDay);
                        mCurrentLink = mLink.concat(mCurrentGroup);
                        mApplicationViewModel.initSubjectsRepository(mCurrentLink);
                        mLiveData = mApplicationViewModel.getData();
                    } else if (UserSettings.getSubjectsUpdateFrequency(getContext()).equals(UserSettings.EVERY_WEEK) &&
                            UserSettings.getWeek(getContext()) != mCurrentWeek) {
                        UserSettings.setWeek(getContext(), mCurrentWeek);
                        mCurrentLink = mLink.concat(mCurrentGroup);
                        mApplicationViewModel.initSubjectsRepository(mCurrentLink);
                        mLiveData = mApplicationViewModel.getData();
                    } else {
                        //TODO Не будет работать пока
                        mCurrentLink = mLink.concat(mCurrentGroup);
                        mApplicationViewModel.initSubjectsRepository(mCurrentLink);
                        mLiveData = mApplicationViewModel.getCachedSubjectsData();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mListView = v.findViewById(R.id.exp);
        mListView.addHeaderView(header);
        mListView.setAdapter(mAdapter);
        for (int i = 0; i < mGroups.size(); i++) {
            mListView.expandGroup(i);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);

                if (mSpinner.getSelectedItemPosition() != 0) {
                    mWeek = Integer.toString(mSpinner.getSelectedItemPosition());
                    mCurrentLink = mLink.concat(mCurrentGroup).concat(PLUS_WEEK).concat(mWeek);
                    mApplicationViewModel.initSubjectsRepository(mCurrentLink);
                    mLiveData = mApplicationViewModel.getData();
                } else {
                    mCurrentLink = mLink.concat(mCurrentGroup);
                    mApplicationViewModel.initSubjectsRepository(mCurrentLink);
                    mLiveData = mApplicationViewModel.getCachedSubjectsData();
                }

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((MainActivity) getActivity()).subjectsNeedToUpdate) {
            mCurrentGroup = UserSettings.getGroup(getContext());
            mCurrentLink = mLink.concat(mCurrentGroup);
            mApplicationViewModel.initSubjectsRepository(mCurrentLink);
            mLiveData = mApplicationViewModel.getCachedSubjectsData();
            ((MainActivity) getActivity()).subjectsNeedToUpdate = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_button) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, mCurrentLink);
            i.putExtra(Intent.EXTRA_SUBJECT, mCurrentGroup);
            startActivity(Intent.createChooser(i, getString(R.string.share_subjects_link)));
        } else if (item.getItemId() == R.id.browser_button) {
            if (UserSettings.getLinksPreference(getContext()).equals(UserSettings.ONLY_BROWSER)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrentLink));
                startActivity(intent);
            } else {
                Intent intent = WebViewActivity.newInstance(getContext(), Uri.parse(mCurrentLink));
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
