package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.mai.nix.maiapp.model.StudentOrgModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nix on 14.08.2017.
 */

public class CafesFragment extends Fragment {
    private ListView mListView;
    private ProgressBar mProgressBar;
    private ArrayList<StudentOrgModel> mOrgs;
    private StudOrgAdapter mAdapter;
    private final String mLink = "http://mai.ru/common/campus/cafeteria/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_orgs_layout, container, false);
        mListView = (ListView)v.findViewById(R.id.stud_org_listview);
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        mOrgs = new ArrayList<>();
        mAdapter = new StudOrgAdapter(getContext(), mOrgs);
        new MyThread().execute();
        mListView.setAdapter(mAdapter);
        return v;
    }

    private class MyThread extends AsyncTask<String, Void, String>{
        private Document doc;
        private Element table;
        private Elements rows;
        public MyThread() {
        }

        @Override
        protected void onPostExecute(String s) {
            mListView.setAdapter(mAdapter);
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                doc = Jsoup.connect(mLink).get();
                table = doc.select("table[class = table]").first();
                rows = table.select("tr");
                mOrgs.clear();;
                for(int i = 1; i < rows.size(); i++){
                    Elements el = rows.get(i).select("td");
                    mOrgs.add(new StudentOrgModel(el.get(0).text(), el.get(1).text(), el.get(2).text()));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
