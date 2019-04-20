package com.mai.nix.maiapp.repositories;

import android.os.AsyncTask;

import com.mai.nix.maiapp.model.SportSectionsBodies;
import com.mai.nix.maiapp.model.SportSectionsHeaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BarracksRepository {

    public interface LoadBarracksCallback {
        void loadBarracks(List<SportSectionsHeaders> barracks);
    }

    public void loadData(LoadBarracksCallback loadBarracksCallback){
        LoadBarracksTask loadBarracksTask = new LoadBarracksTask(loadBarracksCallback);
        loadBarracksTask.execute();
    }

    static class LoadBarracksTask extends AsyncTask<Integer, Void, List<SportSectionsHeaders>> {
        private Document doc;
        private Element table, stupid_header, header;
        private Elements rows;
        private LoadBarracksCallback mLoadBarracksCallback;

        public LoadBarracksTask(LoadBarracksCallback callback) {
            mLoadBarracksCallback = callback;
        }

        @Override
        protected List<SportSectionsHeaders> doInBackground(Integer... integers) {
            List<SportSectionsHeaders> sportSectionsHeaders = new ArrayList<>();
            try {
                doc = Jsoup.connect("http://mai.ru/common/campus/dormitory.php").get();
                table = doc.select("table[class=data-table]").first();
                stupid_header = doc.select("h2").first();
                rows = table.select("tr");
                header = table.select("th").first();
                if (table != null) sportSectionsHeaders.clear();

                sportSectionsHeaders.add(new SportSectionsHeaders(stupid_header.text()));
                sportSectionsHeaders.add(new SportSectionsHeaders(header.text()));

                int j = 0;
                for (int i = 0; i < rows.size(); i++) {
                    Elements el = rows.get(i).select("td");
                    if (!el.isEmpty()) {
                        SportSectionsBodies body = new SportSectionsBodies(el.get(0).select("b").text(),
                                el.get(0).ownText(),
                                el.get(1).text());
                        sportSectionsHeaders.get(j).addBody(body);
                    } else {
                        j++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return sportSectionsHeaders;
            }
            return sportSectionsHeaders;
        }

        @Override
        protected void onPostExecute(List<SportSectionsHeaders> sportSectionsHeaders) {
            super.onPostExecute(sportSectionsHeaders);
            if (mLoadBarracksCallback != null) mLoadBarracksCallback.loadBarracks(sportSectionsHeaders);
        }
    }
}
