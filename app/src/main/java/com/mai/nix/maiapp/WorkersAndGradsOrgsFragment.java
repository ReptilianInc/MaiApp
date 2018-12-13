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
 * Created by Nix on 11.08.2017.
 */

public class WorkersAndGradsOrgsFragment extends SimpleListFragment {

    @Override
    public void releaseThread() {
        new MyThread().execute();
    }

    private class MyThread extends AsyncTask<Integer, Void, Integer> {
        private Document doc;
        private Element table;
        private Elements rows, cols;
        public MyThread() {
            super();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int size = 0;
            try{
                doc = Jsoup.connect("http://www.mai.ru/life/associations/").get();
                table = doc.select("table[class=data-table]").first();
                rows = table.select("th");
                cols = table.select("td");
                if (table != null) mOrgs.clear();
                int i = 0;
                for(int j = 0; j < cols.size(); j+=3){
                    mOrgs.add(new StudentOrgModel(rows.get(i).text(), cols.get(j).text(), cols.get(j+1).text(),
                            cols.get(j+2).text()));
                    i++;
                }
                size = rows.size();
            }catch (IOException e){
                e.printStackTrace();
            }catch (NullPointerException n){
                return 0;
            }
            return size;
        }

        @Override
        protected void onPostExecute(Integer s) {
            mSwipeRefreshLayout.setRefreshing(false);
            if(s == 0){
                if (getContext() != null) Toast.makeText(getContext(), R.string.error,
                        Toast.LENGTH_LONG).show();
            }else {
                mListView.setAdapter(mAdapter);
            }
        }
    }
}
