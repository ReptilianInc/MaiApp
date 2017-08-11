package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Created by Nix on 10.08.2017.
 */

public class StudentOrgsFragment extends Fragment {
    private ListView mListView;
    private ProgressBar mProgressBar;
    private ArrayList<StudentOrgModel> mOrgs;
    private StudOrgAdapter mAdapter;
    private final String mLink = "http://www.mai.ru/life/join/index.php";
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

    private class MyThread extends AsyncTask<String, Void, String> {
        private Document doc;
        private Element table;
        private Elements rows, cols;
        public MyThread() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                doc = Jsoup.connect(mLink).get();
                table = doc.select("table[class=data-table]").first();
                rows = table.select("th");
                cols = table.select("td");
                int i = 0;
                for(int j = 0; j < cols.size(); j+=3){
                    mOrgs.add(new StudentOrgModel(rows.get(i).text(), cols.get(j).text(), cols.get(j+1).text(),
                            cols.get(j+2).text()));
                    i++;
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
        }
    }

}
