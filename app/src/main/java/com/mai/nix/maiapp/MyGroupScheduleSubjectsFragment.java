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
import android.widget.Toast;

import com.mai.nix.maiapp.helpers.UserSettings;
import com.mai.nix.maiapp.model.SubjectBody;
import com.mai.nix.maiapp.model.SubjectHeader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Nix on 02.08.2017.
 */

public class MyGroupScheduleSubjectsFragment extends Fragment {
    private ExpandableListView mListView;
    private ArrayList<SubjectHeader> mGroups;
    private SubjectsExpListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Spinner mSpinner;
    private DataLab mDataLab;
    private String mCurrentGroup, mCurrentLink;
    private int mCurrentDay, mCurrentWeek;
    private Calendar mCalendar;
    private final String mLink = "https://mai.ru/education/schedule/detail.php?group=";
    private String mWeek = "1";
    private final String PLUS_WEEK = "&week=";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shedule_subjects_layout, container, false);
        View header = inflater.inflate(R.layout.spinner_header, null);
        mCalendar = new GregorianCalendar();
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCurrentWeek = mCalendar.get(Calendar.WEEK_OF_MONTH);
        mDataLab = DataLab.get(getContext());
        mGroups = new ArrayList<>();
        mAdapter = new SubjectsExpListAdapter(getContext(), mGroups);
        mCurrentGroup = UserSettings.getGroup(getContext());
        mSpinner = header.findViewById(R.id.spinner);
        mSwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!((MainActivity) getActivity()).subjectsNeedToUpdate) {
                    if (i != 0) {
                        mWeek = Integer.toString(i);
                        mCurrentLink = mLink.concat(mCurrentGroup).concat(PLUS_WEEK).concat(mWeek);
                        new MyThread(mCurrentLink, false).execute();
                    } else if (mDataLab.isSubjectsTablesEmpty()) {
                        mCurrentLink = mLink.concat(mCurrentGroup);
                        new MyThread(mCurrentLink, true).execute();
                    } else if (UserSettings.getSubjectsUpdateFrequency(getContext()).equals(UserSettings.EVERY_DAY) &&
                            UserSettings.getDay(getContext()) != mCurrentDay) {
                        UserSettings.setDay(getContext(), mCurrentDay);
                        mCurrentLink = mLink.concat(mCurrentGroup);
                        new MyThread(mCurrentLink, true).execute();
                    } else if (UserSettings.getSubjectsUpdateFrequency(getContext()).equals(UserSettings.EVERY_WEEK) &&
                            UserSettings.getWeek(getContext()) != mCurrentWeek) {
                        UserSettings.setWeek(getContext(), mCurrentWeek);
                        mCurrentLink = mLink.concat(mCurrentGroup);
                        new MyThread(mCurrentLink, true).execute();
                    } else {
                        mGroups.clear();
                        mCurrentLink = mLink.concat(mCurrentGroup);
                        ArrayList<SubjectHeader> headers = new ArrayList<>();
                        headers.addAll(mDataLab.getHeaders());
                        for (SubjectHeader header : headers) {
                            header.setChildren(mDataLab.getBodies(header.getUuid()));
                        }
                        mGroups.addAll(headers);
                        for (int j = 0; j < mGroups.size(); j++) {
                            mListView.expandGroup(j);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mListView = v.findViewById(R.id.exp);
        mListView.addHeaderView(header);
        mListView.setAdapter(mAdapter);
        for (int i = 0; i < mGroups.size(); i++) {
            mListView.expandGroup(i);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);

                if (mSpinner.getSelectedItemPosition() != 0) {
                    mWeek = Integer.toString(mSpinner.getSelectedItemPosition());
                    mCurrentLink = mLink.concat(mCurrentGroup).concat(PLUS_WEEK).concat(mWeek);
                    new MyThread(mCurrentLink, false).execute();
                } else {
                    mCurrentLink = mLink.concat(mCurrentGroup);
                    new MyThread(mCurrentLink, true).execute();
                }

            }
        });
        return v;
    }

    private class MyThread extends AsyncTask<Integer, Void, Integer> {
        private Document doc;
        private Elements primaries;
        private String final_link;
        private boolean isCaching;

        public MyThread() {
            super();
        }

        public MyThread(String link, boolean cache) {
            final_link = link;
            isCaching = cache;
        }

        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Integer doInBackground(Integer... ints) {
            int size = 0;
            try {
                doc = Jsoup.connect(final_link).get();
                primaries = doc.select("div[class=sc-table sc-table-day]");
                Log.d("link", final_link);
                if (!primaries.isEmpty()) {
                    mGroups.clear();
                    if (isCaching) mDataLab.clearSubjectsCache();
                }
                for (Element prim : primaries) {
                    String date = prim.select("div[class=sc-table-col sc-day-header sc-gray]").text();
                    if (date.isEmpty()) {
                        date = prim.select("div[class=sc-table-col sc-day-header sc-blue]").text();
                    }
                    String day = prim.select("span[class=sc-day]").text();
                    SubjectHeader header = new SubjectHeader(date, day);
                    ArrayList<SubjectBody> bodies = new ArrayList<>();
                    Elements times = prim.select("div[class=sc-table-col sc-item-time]");
                    Elements types = prim.select("div[class=sc-table-col sc-item-type]");
                    Elements titles = prim.select("span[class=sc-title]");
                    Elements teachers = prim.select("div[class=sc-table-col sc-item-title]");
                    Elements rooms = prim.select("div[class=sc-table-col sc-item-location]");
                    for (int i = 0; i < times.size(); i++) {
                        SubjectBody body = new SubjectBody(titles.get(i).text(),
                                teachers.get(i).select("span[class=sc-lecturer]").text(),
                                types.get(i).text(), times.get(i).text(), rooms.get(i).text());
                        body.setUuid(header.getUuid());
                        bodies.add(body);
                    }
                    header.setChildren(bodies);
                    mGroups.add(header);
                    if (isCaching) {
                        mDataLab.addBodies(bodies);
                        mDataLab.addHeader(header);
                    }
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
        protected void onPostExecute(Integer i) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (i == 0) {
                if (getContext() != null) Toast.makeText(getContext(), R.string.error,
                        Toast.LENGTH_LONG).show();
            } else {
                mListView.setAdapter(mAdapter);
                for (int j = 0; j < mGroups.size(); j++) {
                    mListView.expandGroup(j);
                }
                if (isCaching)
                    Toast.makeText(getContext(), R.string.cache_updated_message, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((MainActivity) getActivity()).subjectsNeedToUpdate) {
            mCurrentGroup = UserSettings.getGroup(getContext());
            mCurrentLink = mLink.concat(mCurrentGroup);
            new MyThread(mCurrentLink, true).execute();
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
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorText));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(mCurrentLink));
        }
        return super.onOptionsItemSelected(item);
    }
}
