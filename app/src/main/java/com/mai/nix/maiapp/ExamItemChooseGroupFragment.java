package com.mai.nix.maiapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mai.nix.maiapp.model.ExamModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nix on 17.08.2017.
 */

public class ExamItemChooseGroupFragment extends Fragment {
    private ListView mListView;
    private ArrayList<ExamModel> mExamModels;
    private ExamAdapter mAdapter;
    private TextView mButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int REQUEST_CODE_GROUP = 0;
    private final String mLink = "http://mai.ru/education/schedule/session.php?group=";
    private String mSelectedGroup;
    private TextView mChoosenGroupTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shedule_exams_layout, container, false);
        View header = inflater.inflate(R.layout.choose_group_ex_header, null);
        mButton = (TextView) header.findViewById(R.id.choose_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mChoosenGroupTextView = (TextView) header.findViewById(R.id.group_view);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChooseGroupActivity.newIntent(getContext(), false);
                startActivityForResult(intent, REQUEST_CODE_GROUP);
            }
        });
        mExamModels = new ArrayList<>();
        mListView = (ListView) v.findViewById(R.id.stud_org_listview);
        mAdapter = new ExamAdapter(getContext(), mExamModels);
        mListView.addHeaderView(header);
        mListView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSelectedGroup != null) {
                    new MyThread().execute();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return v;
    }

    private class MyThread extends AsyncTask<Integer, Void, Integer> {
        private Elements date, day, time, title, teacher, room;
        private Document doc;

        public MyThread() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int size = 0;
            try {
                doc = Jsoup.connect(mLink.concat(mSelectedGroup)).get();
                date = doc.select("div[class=sc-table-col sc-day-header sc-gray]");
                day = doc.select("span[class=sc-day]");
                time = doc.select("div[class=sc-table-col sc-item-time]");
                title = doc.select("span[class=sc-title]");
                teacher = doc.select("div[class=sc-table-col sc-item-title]");
                room = doc.select("div[class=sc-table-col sc-item-location]");
                if (!title.isEmpty()) mExamModels.clear();
                for (int i = 0; i < title.size(); i++) {
                    mExamModels.add(new ExamModel(date.get(i).text(), day.get(i).text(), time.get(i).text(), title.get(i).text(),
                            teacher.get(i).select("span[class=sc-lecturer]").text(), room.get(i).text()));
                }
                size = title.size();
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
            mExamModels.clear();
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(true);
            new MyThread().execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mSelectedGroup == null) {
            Toast.makeText(getContext(), R.string.exception_group_null, Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }

        if (item.getItemId() == R.id.share_button) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, mLink.concat(mSelectedGroup));
            i.putExtra(Intent.EXTRA_SUBJECT, mSelectedGroup);
            startActivity(Intent.createChooser(i, getString(R.string.share_exam_link)));
        } else if (item.getItemId() == R.id.browser_button) {
            if (UserSettings.getLinksPreference(getContext()).equals(UserSettings.ONLY_BROWSER)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLink.concat(mSelectedGroup)));
                startActivity(intent);
            } else {
                Intent intent = WebViewActivity.newInstance(getContext(), Uri.parse(mLink.concat(mSelectedGroup)));
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
