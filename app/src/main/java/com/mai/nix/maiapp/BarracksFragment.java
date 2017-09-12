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
import android.widget.Toast;
import com.mai.nix.maiapp.model.SportSectionsBodies;
import com.mai.nix.maiapp.model.SportSectionsHeaders;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nix on 13.08.2017.
 */

public class BarracksFragment extends Fragment {
    private ExpandableListView mExpandableListView;
    private ArrayList<SportSectionsHeaders> mHeaders;
    private SportSectionsExpListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final String mLink = "http://mai.ru/common/campus/dormitory.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exp_list_test, container, false);
        mExpandableListView = (ExpandableListView)v.findViewById(R.id.exp);
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

    private class MyThread extends AsyncTask<Integer, Void, Integer>{
        private Document doc;
        private Element table, stupid_header, header;
        private Elements rows;
        public MyThread() {

        }

        @Override
        protected void onPostExecute(Integer integer) {
            mSwipeRefreshLayout.setRefreshing(false);
            if(integer == 0){
                Toast.makeText(getContext(), R.string.error,
                        Toast.LENGTH_LONG).show();
            }else{
                mExpandableListView.setAdapter(mAdapter);
                for(int i = 0; i < mHeaders.size(); i++){
                    mExpandableListView.expandGroup(i);
                }
            }
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            try{
                doc = Jsoup.connect(mLink).get();
                table = doc.select("table[class=data-table]").first();
                stupid_header = doc.select("h2").first();
                rows = table.select("tr");
                header = table.select("th").first();
                mHeaders.clear();

                mHeaders.add(new SportSectionsHeaders(stupid_header.text()));
                mHeaders.add(new SportSectionsHeaders(header.text()));

                int j = 0;
                for(int i = 0; i < rows.size(); i++){
                    Elements el = rows.get(i).select("td");
                    if(!el.isEmpty()){
                        SportSectionsBodies body = new SportSectionsBodies(el.get(0).select("b").text(),
                                el.get(0).ownText(),
                                el.get(1).text());
                        mHeaders.get(j).addBody(body);
                    }else{
                        j++;
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }catch (NullPointerException n){
                return 0;
            }
            return rows.size();
        }
    }
}
