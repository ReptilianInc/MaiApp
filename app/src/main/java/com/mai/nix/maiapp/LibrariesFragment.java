package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.widget.Toast;

import com.mai.nix.maiapp.model.SportSectionsBodies;
import com.mai.nix.maiapp.model.SportSectionsHeaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Nix on 13.08.2017.
 */

public class LibrariesFragment extends SimpleExpandableListFragment {
    @Override
    protected void releaseThread() {
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
                mExpandableListView.setAdapter(mAdapter);
                for (int i = 0; i < mHeaders.size(); i++) {
                    mExpandableListView.expandGroup(i);
                }
            }
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int size = 0;
            try {
                doc = Jsoup.connect("http://mai.ru/common/campus/library/").get();
                table = doc.select("table[class = table table-bordered table-hover]").first();
                rows = doc.select("tr");
                if (table != null) mHeaders.clear();
                for (int i = 0; i < rows.size(); i++) {
                    if (rows.get(i).select("th").size() == 1) {
                        mHeaders.add(new SportSectionsHeaders(rows.get(i).text()));
                    }
                }
                int j = 0;
                for (int i = 2; i < rows.size(); i++) {
                    Elements el = rows.get(i).select("td");
                    if (!el.isEmpty()) {
                        SportSectionsBodies body = new SportSectionsBodies(
                                el.get(0).text(),
                                el.get(1).html());
                        mHeaders.get(j).addBody(body);
                    } else if (rows.get(i).select("th").size() == 1) {
                        j++;
                    }
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
