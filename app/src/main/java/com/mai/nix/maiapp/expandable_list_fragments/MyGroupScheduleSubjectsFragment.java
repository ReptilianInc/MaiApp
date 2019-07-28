package com.mai.nix.maiapp.expandable_list_fragments;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mai.nix.maiapp.MainActivity;
import com.mai.nix.maiapp.PeekWeekController;
import com.mai.nix.maiapp.R;
import com.mai.nix.maiapp.UserSettings;
import com.mai.nix.maiapp.WebViewActivity;
import com.mai.nix.maiapp.model.SubjectHeader;
import com.mai.nix.maiapp.viewmodels.ApplicationViewModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Nix on 02.08.2017.
 */

public class MyGroupScheduleSubjectsFragment extends Fragment implements PeekWeekController.WeekButtonClickListener {
    private ArrayList<SubjectHeader> mGroups;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private BottomSheetBehavior mBottomSheetBehaviour;
    private ConstraintLayout mBottomSheetLayout;
    private ImageView mToggleButton;
    private TextView mChosenWeekTextView;
    private ScheduleListAdapter mScheduleListAdapter;
    private PeekWeekController mPeekWeekController;
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(getContext(), R.color.colorText));
        }
        mApplicationViewModel = ViewModelProviders.of(MyGroupScheduleSubjectsFragment.this)
                .get(ApplicationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shedule_subjects_layout, container, false);
        mCalendar = new GregorianCalendar();
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCurrentWeek = mCalendar.get(Calendar.WEEK_OF_MONTH);
        UserSettings.initialize(getContext());
        mChosenWeekTextView = v.findViewById(R.id.weekTextView);
        mGroups = new ArrayList<>();
        mCurrentGroup = UserSettings.getGroup(getContext());
        mSwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mToggleButton = v.findViewById(R.id.toggleButton);
        mRecyclerView = v.findViewById(R.id.scheduleRecyclerView);
        mBottomSheetLayout = v.findViewById(R.id.mainBottomSheet);
        mBottomSheetBehaviour = BottomSheetBehavior.from(mBottomSheetLayout);
        mScheduleListAdapter = new ScheduleListAdapter();
        mRecyclerView.setAdapter(mScheduleListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPeekWeekController = new PeekWeekController(v, R.id.weekChooseLayout, this);
        mPeekWeekController.initFirstClickedItem();
        mCurrentLink = mLink.concat(mCurrentGroup);
        mApplicationViewModel.initSubjectsRepository(mCurrentLink);
        mLiveData = mApplicationViewModel.getCachedSubjectsData();
        mLiveData.observe(MyGroupScheduleSubjectsFragment.this, new Observer<List<SubjectHeader>>() {
            @Override
            public void onChanged(@Nullable List<SubjectHeader> subjectHeaders) {
                mSwipeRefreshLayout.setRefreshing(false);
                mGroups.clear();
                mGroups.addAll(subjectHeaders);
                mScheduleListAdapter.setData(mGroups);
                mScheduleListAdapter.notifyDataSetChanged();
                Log.d("poisondart ", "onChanged");
            }
        });


        /*mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                if (mPeekWeekController.getLastChosenWeek() != 0) {
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
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    animateToggleButton(0.0f, -180.0f);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    animateToggleButton(-180.0f, 0.0f);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });
        return v;
    }

    @Override
    public void weekButtonClicked(int chosenWeek) {
        if (chosenWeek != 0) {
            mChosenWeekTextView.setText(chosenWeek + " неделя");
        } else {
            mChosenWeekTextView.setText("Текущая неделя");
        }
        mSwipeRefreshLayout.setRefreshing(true);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mApplicationViewModel.initSubjectsRepository(mLink.concat(mCurrentGroup).concat(PLUS_WEEK).concat(Integer.toString(chosenWeek)));
        mLiveData = mApplicationViewModel.getData();
    }

    private void animateToggleButton(float fromDegrees, float toDegrees) {
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(fromDegrees, toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(500);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);

        mToggleButton.startAnimation(animSet);
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
