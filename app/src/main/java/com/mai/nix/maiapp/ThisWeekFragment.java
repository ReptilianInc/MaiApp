package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.mai.nix.maiapp.model.SubjectBodies;
import com.mai.nix.maiapp.model.SubjectHeaders;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nix on 02.08.2017.
 */

public class ThisWeekFragment extends Fragment {
    private ExpandableListView mListView;
    private ArrayList<SubjectHeaders> mGroups;
    private ProgressBar mProgressBar;
    private SubjectsExpListAdapter mAdapter;
    private Spinner mSpinner;
    private final String mLink = "http://mai.ru/education/schedule/detail.php?group=3ВТИ-3ДБ-006&week=";
    private String mWeek = "1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exp_list_test, container, false);
        View header = inflater.inflate(R.layout.spinner_header, null);
        mSpinner = (Spinner)header.findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // показываем позиция нажатого элемента
                Toast.makeText(getContext(), "Position = " + i, Toast.LENGTH_SHORT).show();
                if(i != 0){
                    mGroups.clear();
                    mAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    mWeek = Integer.toString(i);
                    new MyThread().execute();
                }else{
                    mGroups.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mListView = (ExpandableListView)v.findViewById(R.id.exp);
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar_test);
        mGroups = new ArrayList<>();
        //Создаем адаптер и передаем context и список с данными
        mAdapter = new SubjectsExpListAdapter(getContext(), mGroups);
        new MyThread().execute();
        mListView.addHeaderView(header);
        mListView.setAdapter(mAdapter);
        for(int i = 0; i < mGroups.size(); i++){
            mListView.expandGroup(i);
        }
        return v;
    }
    private class MyThread extends AsyncTask<String, Void, String> {
        private Document doc;
        private Elements primaries;
        public MyThread() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                doc = Jsoup.connect(mLink.concat(mWeek)).get();
                primaries = doc.select("div[class=sc-table sc-table-day]");
                mGroups.clear();
                for(Element prim : primaries){
                    String date = prim.select("div[class=sc-table-col sc-day-header sc-gray]").text();
                    String day = prim.select("span[class=sc-day]").text();
                    SubjectHeaders header = new SubjectHeaders(date, day);
                    ArrayList<SubjectBodies> bodies = new ArrayList<>();
                    Elements times = prim.select("div[class=sc-table-col sc-item-time]");
                    Elements types = prim.select("div[class=sc-table-col sc-item-type]");
                    Elements titles = prim.select("span[class=sc-title]");
                    Elements teachers = prim.select("div[class=sc-table-col sc-item-title]");
                    Elements rooms = prim.select("div[class=sc-table-col sc-item-location]");
                    Log.d("date and titles", date + " " + Integer.toString(titles.size()));
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
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            for(int i = 0; i < mGroups.size(); i++){
                mListView.expandGroup(i);
            }
        }
    }
}
