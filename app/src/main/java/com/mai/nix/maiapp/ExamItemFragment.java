package com.mai.nix.maiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mai.nix.maiapp.helpers.UserSettings;
import com.mai.nix.maiapp.model.ExamModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Nix on 01.08.2017.
 */

public class ExamItemFragment extends Fragment {
    private ListView mListView;
    private ArrayList<ExamModel> mExamModels;
    private ExamAdapter mAdapter;
    private int mCurrentDay, mCurrentWeek;
    private Calendar mCalendar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final String mLink = "http://mai.ru/education/schedule/session.php?group=";
    private String mCurrentGroup, mCurrentLink;
    private DataLab mDataLab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shedule_exams_layout, container, false);
        mCalendar = new GregorianCalendar();
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCurrentWeek = mCalendar.get(Calendar.WEEK_OF_MONTH);
        mCurrentGroup = UserSettings.getGroup(getContext());
        mCurrentLink = mLink.concat(mCurrentGroup);
        mDataLab = DataLab.get(getContext());
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mExamModels = new ArrayList<>();
        mListView = (ListView) v.findViewById(R.id.stud_org_listview);
        mAdapter = new ExamAdapter(getContext(), mExamModels);
        /*if (!((MainActivity) getActivity()).examsNeedToUpdate) {
            if (mDataLab.isExamsTableEmpty()) {
                new MyThread(true).execute();
            } else if (UserSettings.getExamsUpdateFrequency(getContext()).equals(UserSettings.EVERY_DAY) &&
                    UserSettings.getDay(getContext()) != mCurrentDay) {
                new MyThread(true).execute();
            }
            if (UserSettings.getExamsUpdateFrequency(getContext()).equals(UserSettings.EVERY_WEEK) &&
                    UserSettings.getWeek(getContext()) != mCurrentWeek) {
                new MyThread(true).execute();
            } else {
                mExamModels.addAll(mDataLab.getExams());
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }*/
        mListView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new MyThread(true).execute();
            }
        });
        return v;
    }

    private class MyThread extends AsyncTask<Integer, Void, Integer> {
        private Elements date, day, container;
        private Document doc;
        private boolean isCaching;

        public MyThread() {
            super();
        }

        public MyThread(boolean cache) {
            super();
            isCaching = cache;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int size = 0;
            try {
                doc = Jsoup.connect(mCurrentLink).get();
                date = doc.select("div[class=sc-table-col sc-day-header sc-gray]");
                day = doc.select("span[class=sc-day]");
                container = doc.select("div[class=sc-table-col sc-table-detail-container]");

                if (!day.isEmpty()) {
                    mExamModels.clear();
                    if (isCaching) mDataLab.clearExamsCache();
                }

                for (int i = 0; i < day.size(); i++) {
                    Elements time = container.get(i).select("div[class=sc-table-col sc-item-time]");
                    Elements title = container.get(i).select("span[class=sc-title]");
                    Elements teacher = container.get(i).select("div[class=sc-table-col sc-item-title]");
                    Elements room = container.get(i).select("div[class=sc-table-col sc-item-location]");
                    for (int k = 0; k < time.size(); k++) {
                        ExamModel model = new ExamModel(date.get(i).text(), day.get(i).text(), time.get(k).text(), title.get(k).text(),
                                teacher.get(k).select("span[class=sc-lecturer]").text(), room.get(k).text());
                        mExamModels.add(model);
                        if (isCaching) mDataLab.addExam(model);
                    }
                }
                size = day.size();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return 0;
            }
            return size;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (integer == 0) {
                if (getContext() != null) Toast.makeText(getContext(), R.string.error,
                        Toast.LENGTH_LONG).show();
            } else {
                mListView.setAdapter(mAdapter);
                if (isCaching)
                    Toast.makeText(getContext(), R.string.cache_updated_message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (((MainActivity) getActivity()).examsNeedToUpdate) {
            mCurrentGroup = UserSettings.getGroup(getContext());
            new MyThread(true).execute();
            ((MainActivity) getActivity()).examsNeedToUpdate = false;
        }*/
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
            startActivity(Intent.createChooser(i, getString(R.string.share_exam_link)));
        } else if (item.getItemId() == R.id.browser_button) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorText));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(mCurrentLink));
        }
        return super.onOptionsItemSelected(item);
    }
}
