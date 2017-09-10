package com.mai.nix.maiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
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
        UserSettings.initialize(getContext());
        mCurrentGroup = UserSettings.getGroup(getContext());
        mCurrentLink = mLink.concat(mCurrentGroup);
        mDataLab = DataLab.get(getContext());
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mExamModels = new ArrayList<>();
        mListView = (ListView) v.findViewById(R.id.stud_org_listview);
        mAdapter = new ExamAdapter(getContext(), mExamModels);
        if(mDataLab.isExamsTableEmpty()){
            new MyThread(true).execute();
        }else if(UserSettings.getExamsUpdateFrequency(getContext()).equals(UserSettings.EVERY_DAY) &&
                UserSettings.getDay(getContext()) != mCurrentDay){
            new MyThread(true).execute();
        }else if(UserSettings.getExamsUpdateFrequency(getContext()).equals(UserSettings.EVERY_WEEK) &&
                UserSettings.getWeek(getContext()) != mCurrentWeek){
            new MyThread(true).execute();
        }else{
            mExamModels.addAll(mDataLab.getExams());
            mAdapter.notifyDataSetChanged();
        }
        mListView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataLab.clearExamsCache();
                mSwipeRefreshLayout.setRefreshing(true);
                new MyThread(true).execute();
            }
        });
        return v;
    }
    private class MyThread extends AsyncTask<String, Void, String>{
        private Elements date, day, time, title, teacher, room;
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
        protected String doInBackground(String... strings) {
            try {
                doc = Jsoup.connect(mCurrentLink).get();
                date = doc.select("div[class=sc-table-col sc-day-header sc-gray]");
                day = doc.select("span[class=sc-day]");
                time = doc.select("div[class=sc-table-col sc-item-time]");
                title = doc.select("span[class=sc-title]");
                teacher = doc.select("div[class=sc-table-col sc-item-title]");
                room = doc.select("div[class=sc-table-col sc-item-location]");
                mExamModels.clear();
                int kek = teacher.size();
                Log.d("teacher.size = ", Integer.toString(kek));
                for (int i = 0; i < kek; i++){
                    ExamModel model = new ExamModel(date.get(i).text(), day.get(i).text(), time.get(i).text(), title.get(i).text(),
                            teacher.get(i).select("span[class=sc-lecturer]").text(), room.get(i).text());
                    mExamModels.add(model);
                    if(isCaching)mDataLab.addExam(model);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mListView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
            if (isCaching) Toast.makeText(getContext(), R.string.cache_updated_message, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mCurrentGroup = UserSettings.getGroup(getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.go_web_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.go_web_in_frags) {
            Uri uri = Uri.parse(mCurrentLink);
            if(UserSettings.getLinksPreference(getContext()).equals(UserSettings.ONLY_BROWSER)){
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }else{
                Intent intent = WebViewActivity.newInstance(getContext(), uri);
                startActivity(intent);
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
