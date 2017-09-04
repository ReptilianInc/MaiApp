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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.mai.nix.maiapp.model.SubjectBodies;
import com.mai.nix.maiapp.model.SubjectHeaders;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Nix on 02.08.2017.
 */

public class ThisWeekFragment extends Fragment {
    private ExpandableListView mListView;
    private ArrayList<SubjectHeaders> mGroups;
    private SubjectsExpListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SharedPreferences mSharedPreferences;
    private Spinner mSpinner;
    private String mCurrentGroup;
    private final String mLink = "https://mai.ru/education/schedule/detail.php?group=";
    private String mWeek = "1";
    private final String PLUS_WEEK = "&week=";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shedule_subjects_layout, container, false);
        View header = inflater.inflate(R.layout.spinner_header, null);
        mSharedPreferences = getActivity().getSharedPreferences("suka", Context.MODE_PRIVATE);
        mGroups = new ArrayList<>();
        //Создаем адаптер и передаем context и список с данными
        mAdapter = new SubjectsExpListAdapter(getContext(), mGroups);
        mCurrentGroup = mSharedPreferences.getString(getString(R.string.pref_group), "");
        //Toast.makeText(getContext(), mCurrentGroup, Toast.LENGTH_SHORT).show();
        mSpinner = (Spinner)header.findViewById(R.id.spinner);
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    //mGroups.clear();
                    //mAdapter.notifyDataSetChanged();
                    //mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    mWeek = Integer.toString(i);
                    new MyThread(mLink.concat(mCurrentGroup).concat(PLUS_WEEK).concat(mWeek), false).execute();
                }else{
                    /*mGroups.clear();
                    mAdapter.notifyDataSetChanged();*/
                    new MyThread(mLink.concat(mCurrentGroup), false).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mListView = (ExpandableListView)v.findViewById(R.id.exp);
        mSwipeRefreshLayout.setRefreshing(true);

        //new MyThread().execute();
        mListView.addHeaderView(header);
        mListView.setAdapter(mAdapter);
        for(int i = 0; i < mGroups.size(); i++){
            mListView.expandGroup(i);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mGroups.clear();
                //mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(true);

                if(mSpinner.getSelectedItemPosition() != 0){
                    mWeek = Integer.toString(mSpinner.getSelectedItemPosition());
                    new MyThread(mLink.concat(mCurrentGroup).concat(PLUS_WEEK).concat(mWeek), false).execute();
                }else{
                    new MyThread(mLink.concat(mCurrentGroup), true).execute();
                }

            }
        });
        return v;
    }
    private class MyThread extends AsyncTask<String, Void, String> {
        private Document doc;
        private Elements primaries;
        private String final_link;
        private boolean isCaching;
        public MyThread() {
            super();
        }
        public MyThread(String link, boolean cache){
            final_link = link;
            isCaching = cache;
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                //String group = URLEncoder.encode(mCurrentGroup, "UTF-8");
                doc = Jsoup.connect(final_link).get();
                primaries = doc.select("div[class=sc-table sc-table-day]");
                Log.d("link", final_link);
                mGroups.clear();
                for(Element prim : primaries){
                    String date = prim.select("div[class=sc-table-col sc-day-header sc-gray]").text();
                    if(date.isEmpty()){
                        date = prim.select("div[class=sc-table-col sc-day-header sc-blue]").text();
                    }
                    String day = prim.select("span[class=sc-day]").text();
                    SubjectHeaders header = new SubjectHeaders(date, day);
                    ArrayList<SubjectBodies> bodies = new ArrayList<>();
                    Elements times = prim.select("div[class=sc-table-col sc-item-time]");
                    Elements types = prim.select("div[class=sc-table-col sc-item-type]");
                    Elements titles = prim.select("span[class=sc-title]");
                    Elements teachers = prim.select("div[class=sc-table-col sc-item-title]");
                    Elements rooms = prim.select("div[class=sc-table-col sc-item-location]");
                    for (int i = 0; i < times.size(); i++){
                        SubjectBodies body = new SubjectBodies(titles.get(i).text(),
                                teachers.get(i).select("span[class=sc-lecturer]").text(),
                                types.get(i).text(), times.get(i).text(), rooms.get(i).text());
                        bodies.add(body);
                    }
                    header.setChildren(bodies);
                    mGroups.add(header);
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
            for(int i = 0; i < mGroups.size(); i++){
                mListView.expandGroup(i);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentGroup = mSharedPreferences.getString(getString(R.string.pref_group), "");
    }
}
