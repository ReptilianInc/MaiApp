package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

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
    private ProgressBar mProgressBar;
    private ArrayList<SportSectionsHeaders> mHeaders;
    private SportSectionsExpListAdapter mAdapter;
    private final String mLink = "http://mai.ru/common/campus/dormitory.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exp_list_test, container, false);
        mExpandableListView = (ExpandableListView)v.findViewById(R.id.exp);
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar_test);
        mHeaders = new ArrayList<>();
        mAdapter = new SportSectionsExpListAdapter(getContext(), mHeaders);
        new MyThread().execute();
        mExpandableListView.setAdapter(mAdapter);
        for(int i = 0; i < mHeaders.size(); i++){
            mExpandableListView.expandGroup(i);
        }
        return v;
    }

    private class MyThread extends AsyncTask<String, Void, String>{
        private Document doc;
        private Element table, stupid_header, header;
        private Elements rows;
        public MyThread() {

        }

        @Override
        protected void onPostExecute(String s) {
            mExpandableListView.setAdapter(mAdapter);
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            for(int i = 0; i < mHeaders.size(); i++){
                mExpandableListView.expandGroup(i);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
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
            }
            return null;
        }
    }
}
