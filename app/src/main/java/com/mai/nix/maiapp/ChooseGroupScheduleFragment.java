package com.mai.nix.maiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
 * Created by Nix on 01.08.2017.
 */

public class ChooseGroupScheduleFragment extends Fragment {
    private ExpandableListView mListView;
    private ArrayList<SubjectHeaders> mGroups;
    private SubjectsExpListAdapter mAdapter;
    private ProgressBar mProgressBar;
    private Spinner mSpinner;
    private TextView mButton;
    private final String mLinkMain = "http://mai.ru/education/schedule/detail.php?group=";
    private String ChosenWeek = "1";
    private final String PLUS_WEEK = "&week=";
    private static final int REQUEST_CODE_GROUP = 0;
    private String mSelectedGroup;
    private TextView mChoosenGroupTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_week_schedule_layout, container, false);
        View header = inflater.inflate(R.layout.choose_group_header, null);
        mSpinner = (Spinner)header.findViewById(R.id.spinner);
        mChoosenGroupTextView = (TextView)header.findViewById(R.id.group_view);
        mButton = (TextView) header.findViewById(R.id.choose_view);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChooseGroupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_GROUP);
            }
        });
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), "Position = " + i, Toast.LENGTH_SHORT).show();
                if(mSelectedGroup != null){
                    ChosenWeek = Integer.toString(i+1);
                    mGroups.clear();
                    mAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    new MyThread().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mListView = (ExpandableListView)v.findViewById(R.id.listview_chosen_week);
        mProgressBar = (ProgressBar)v.findViewById(R.id.chosen_week_progressbar);
        mGroups = new ArrayList<>();
        //Создаем адаптер и передаем context и список с данными
        mAdapter = new SubjectsExpListAdapter(getContext(), mGroups);
        mListView.addHeaderView(header);
        mListView.setAdapter(mAdapter);
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
                doc = Jsoup.connect(mLinkMain.concat(mSelectedGroup).concat(PLUS_WEEK).concat(ChosenWeek)).get();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_GROUP) {
            if (data == null) {
                return;
            }
            mSelectedGroup = data.getStringExtra(ChooseGroupActivity.EXTRA_GROUP);
            mChoosenGroupTextView.setText(mSelectedGroup);
            mGroups.clear();
            mAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            new MyThread().execute();
        }
    }
}
