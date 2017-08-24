package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.mai.nix.maiapp.model.SportSectionsBodies;
import com.mai.nix.maiapp.model.SportSectionsHeaders;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nix on 11.08.2017.
 */

public class SportSectionsFragment extends Fragment {
    private ExpandableListView mExpandableListView;
    //private ProgressBar mProgressBar;
    private ArrayList<SportSectionsHeaders> mHeaders;
    private SportSectionsExpListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final String mLink = "http://www.mai.ru/life/sport/sections.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exp_list_test, container, false);
        mExpandableListView = (ExpandableListView)v.findViewById(R.id.exp);
        //mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar_test);
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mHeaders = new ArrayList<>();
        mAdapter = new SportSectionsExpListAdapter(getContext(), mHeaders);
        new MyThread().execute();
        mExpandableListView.setAdapter(mAdapter);
        for(int i = 0; i < mHeaders.size(); i++){
            mExpandableListView.expandGroup(i);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHeaders.clear();
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(true);
                new MyThread().execute();
            }
        });
        return v;
    }
    private class MyThread extends AsyncTask<String, Void, String> {
        private Document doc;
        private Element table;
        private Elements rows, headers;
        public MyThread() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                doc = Jsoup.connect(mLink).get();
                table = doc.select("table[class=data-table]").first();
                rows = table.select("tr");
                headers = table.select("th");
                mHeaders.clear();
                for(int i = 0; i < headers.size(); i++){
                    mHeaders.add(new SportSectionsHeaders(headers.get(i).text()));
                }
                int j = 0;
                for(int i = 1; i < rows.size(); i++){
                    Elements el = rows.get(i).select("td");
                    if(!el.isEmpty()){
                        SportSectionsBodies body = new SportSectionsBodies(el.get(0).text(), el.get(1).text(), el.get(2).text());
                        mHeaders.get(j).addBody(body);
                    }else{
                        j++;
                    }
                }

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mExpandableListView.setAdapter(mAdapter);
            //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
            for(int i = 0; i < mHeaders.size(); i++){
                mExpandableListView.expandGroup(i);
            }
        }
    }
}
