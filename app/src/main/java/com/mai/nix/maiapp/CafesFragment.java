package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.widget.Toast;

import com.mai.nix.maiapp.model.StudentOrgModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Nix on 14.08.2017.
 */

public class CafesFragment extends SimpleListFragment {

    @Override
    public void releaseThread() {
        new MyThread().execute();
    }

    private class MyThread extends AsyncTask<Integer, Void, Integer> {
        private Document doc;
        private Element table;
        private Elements rows;

        public MyThread() {
        }

        @Override
        protected void onPostExecute(Integer s) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (s == 0) {
                if (getContext() != null) Toast.makeText(getContext(), R.string.error,
                        Toast.LENGTH_LONG).show();
            } else {
                mListView.setAdapter(mAdapter);
            }
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int size = 0;
            try {
                doc = Jsoup.connect("http://mai.ru/common/campus/cafeteria/").get();
                table = doc.select("table[class = table table-bordered]").first();
                rows = table.select("tr");
                if (table != null) mOrgs.clear();
                for (int i = 1; i < rows.size(); i++) {
                    Elements el = rows.get(i).select("td");
                    mOrgs.add(new StudentOrgModel(el.get(1).text(), el.get(2).text(), el.get(3).text()));
                }
                size = rows.size();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return 0;
            }
            return size;
        }
    }
}
