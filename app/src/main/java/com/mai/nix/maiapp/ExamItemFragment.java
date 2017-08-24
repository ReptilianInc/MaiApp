package com.mai.nix.maiapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.mai.nix.maiapp.model.ExamModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nix on 01.08.2017.
 */

public class ExamItemFragment extends Fragment {
    private ListView mListView;
    private ArrayList<ExamModel> mExamModels;
    private ExamAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    //private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final String mLink = "http://mai.ru/education/schedule/session.php?group=";
    private String mCurrentGroup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shedule_exams_layout, container, false);
        mSharedPreferences = getActivity().getSharedPreferences("suka", Context.MODE_PRIVATE);
        mCurrentGroup = mSharedPreferences.getString(getString(R.string.pref_group), "");
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mExamModels = new ArrayList<>();
        //mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        mListView = (ListView) v.findViewById(R.id.stud_org_listview);
        mAdapter = new ExamAdapter(getContext(), mExamModels);
        new MyThread().execute();
        mListView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mExamModels.clear();
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(true);
                new MyThread().execute();
            }
        });
        return v;
    }
    private class MyThread extends AsyncTask<String, Void, String>{
        private Elements date, day, time, title, teacher, room;
        private Document doc;
        public MyThread() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                doc = Jsoup.connect(mLink.concat(mCurrentGroup)).get();
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
                    mExamModels.add(new ExamModel(date.get(i).text(), day.get(i).text(), time.get(i).text(), title.get(i).text(),
                            teacher.get(i).select("span[class=sc-lecturer]").text(), room.get(i).text()));
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
            //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mCurrentGroup = mSharedPreferences.getString(getString(R.string.pref_group), "");
    }
}
