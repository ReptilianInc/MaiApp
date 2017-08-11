package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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

public class ChooseWeekScheduleFragment extends Fragment {
    private ExpandableListView mListView;
    private ArrayList<SubjectHeaders> mGroups;
    private SubjectsExpListAdapter mAdapter;
    private RelativeLayout mHolder;
    private ProgressBar mProgressBar;
    private SeekBar mSeekBar;
    private Button mButton;
    private TextView mWeekView;
    private final String mLinkMain = "http://mai.ru/education/schedule/detail.php?group=3ВТИ-3ДБ-006&week=";
    private String ChosenWeek = "1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_week_schedule_layout, container, false);
        View holder = inflater.inflate(R.layout.choose_week_holder, null);
        mSeekBar = (SeekBar)holder.findViewById(R.id.seekbar);
        mButton = (Button)holder.findViewById(R.id.chosen_week_start);
        mListView = (ExpandableListView)v.findViewById(R.id.listview_chosen_week);
        mProgressBar = (ProgressBar)v.findViewById(R.id.chosen_week_progressbar);
        mWeekView =(TextView)holder.findViewById(R.id.chosen_week);
        mWeekView.setText("1 неделя");
        mGroups = new ArrayList<>();
        //Создаем адаптер и передаем context и список с данными
        mAdapter = new SubjectsExpListAdapter(getContext(), mGroups);
        mListView.addHeaderView(holder);
        mListView.setAdapter(mAdapter);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //nothing
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mWeekView.setText(Integer.toString(seekBar.getProgress() + 1).concat(" неделя"));
                ChosenWeek = Integer.toString(seekBar.getProgress()+1);
                Log.d("chosenweek", ChosenWeek);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                mGroups.clear();
                mAdapter.notifyDataSetChanged();
                new MyThread().execute();
                for(int i = 0; i < mGroups.size(); i++){
                    mListView.expandGroup(i);
                }
            }
        });
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
                doc = Jsoup.connect(mLinkMain.concat(ChosenWeek)).get();
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
