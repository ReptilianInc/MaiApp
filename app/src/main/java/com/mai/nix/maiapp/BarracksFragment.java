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

public class BarracksFragment extends SimpleExpandableListFragment {
    @Override
    protected void releaseThread() {
        new MyThread().execute();
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
                if (getContext() != null) Toast.makeText(getContext(), R.string.error,
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
            int size = 0;
            try{
                doc = Jsoup.connect("http://mai.ru/common/campus/dormitory.php").get();
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
                size = rows.size();
            }catch (IOException e){
                e.printStackTrace();
            }catch (NullPointerException n){
                return 0;
            }
            return size;
        }
    }
}
