package com.mai.nix.maiapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mai.nix.maiapp.model.SubjectBody;
import com.mai.nix.maiapp.model.SubjectHeader;

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
    private ArrayList<SubjectHeader> mGroups;
    private SubjectsExpListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Spinner mSpinner;
    private TextView mButton;
    private final String mLinkMain = "http://mai.ru/education/schedule/detail.php?group=";
    private String ChosenWeek = "1";
    private final String PLUS_WEEK = "&week=";
    private static final int REQUEST_CODE_GROUP = 0;
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
        View v = inflater.inflate(R.layout.shedule_subjects_layout, container, false);
        View header = inflater.inflate(R.layout.choose_group_header, null);
        mSpinner = (Spinner) header.findViewById(R.id.spinner);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mChoosenGroupTextView = (TextView) header.findViewById(R.id.group_view);
        mButton = (TextView) header.findViewById(R.id.choose_view);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChooseGroupActivity.newIntent(getContext(), false);
                startActivityForResult(intent, REQUEST_CODE_GROUP);
            }
        });
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSelectedGroup != null) {
                    ChosenWeek = Integer.toString(i + 1);
                    mSwipeRefreshLayout.setRefreshing(true);
                    new MyThread().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mListView = (ExpandableListView) v.findViewById(R.id.exp);
        mGroups = new ArrayList<>();
        mAdapter = new SubjectsExpListAdapter(getContext(), mGroups);
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
        private Document doc;
        private Elements primaries;

        public MyThread() {
            super();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int size = 0;
            try {
                doc = Jsoup.connect(mLinkMain.concat(mSelectedGroup).concat(PLUS_WEEK).concat(ChosenWeek)).get();
                primaries = doc.select("div[class=sc-table sc-table-day]");
                if (!primaries.isEmpty()) mGroups.clear();
                for (Element prim : primaries) {
                    String date = prim.select("div[class=sc-table-col sc-day-header sc-gray]").text();
                    String day = prim.select("span[class=sc-day]").text();
                    SubjectHeader header = new SubjectHeader(date, day);
                    ArrayList<SubjectBody> bodies = new ArrayList<>();
                    Elements times = prim.select("div[class=sc-table-col sc-item-time]");
                    Elements types = prim.select("div[class=sc-table-col sc-item-type]");
                    Elements titles = prim.select("span[class=sc-title]");
                    Elements teachers = prim.select("div[class=sc-table-col sc-item-title]");
                    Elements rooms = prim.select("div[class=sc-table-col sc-item-location]");
                    Log.d("date and titles", date + " " + Integer.toString(titles.size()));
                    for (int i = 0; i < times.size(); i++) {
                        SubjectBody body = new SubjectBody(titles.get(i).text(),
                                teachers.get(i).select("span[class=sc-lecturer]").text(),
                                types.get(i).text(), times.get(i).text(), rooms.get(i).text());
                        bodies.add(body);
                    }
                    header.setChildren(bodies);
                    mGroups.add(header);
                }
                size = primaries.size();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return 0;
            }
            return size;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (integer == 0) {
                if (getContext() != null) Toast.makeText(getContext(), R.string.error,
                        Toast.LENGTH_LONG).show();
            } else {
                mListView.setAdapter(mAdapter);
                for (int i = 0; i < mGroups.size(); i++) {
                    mListView.expandGroup(i);
                }
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
            i.putExtra(Intent.EXTRA_TEXT, mLinkMain.concat(mSelectedGroup).concat(PLUS_WEEK).concat(ChosenWeek));
            i.putExtra(Intent.EXTRA_SUBJECT, mSelectedGroup);
            startActivity(Intent.createChooser(i, getString(R.string.share_subjects_link)));
        } else if (item.getItemId() == R.id.browser_button) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorText));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(mLinkMain.concat(mSelectedGroup).concat(PLUS_WEEK).concat(ChosenWeek)));
        }
        return super.onOptionsItemSelected(item);
    }
}
