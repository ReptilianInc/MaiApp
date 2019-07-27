package com.mai.nix.maiapp.repositories;

import android.os.AsyncTask;

import com.mai.nix.maiapp.model.SportSectionsBodies;
import com.mai.nix.maiapp.model.SportSectionsHeader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SportSectionsRepository {
    public interface LoadSportSectionsCallback {
        void loadSportSections(List<SportSectionsHeader> sportSections);
    }

    public void loadData(LoadSportSectionsCallback sportSectionsCallback) {
        LoadSportSectionsTask loadSportSectionsTask = new LoadSportSectionsTask(sportSectionsCallback);
        loadSportSectionsTask.execute();
    }

    static class LoadSportSectionsTask extends AsyncTask<Integer, Void, List<SportSectionsHeader>> {
        private Document doc;
        private Element table;
        private Elements rows, headers;
        private final LoadSportSectionsCallback mLoadSportSectionsCallback;

        public LoadSportSectionsTask(LoadSportSectionsCallback callback) {
            mLoadSportSectionsCallback = callback;
        }

        @Override
        protected List<SportSectionsHeader> doInBackground(Integer... integers) {
            List<SportSectionsHeader> sportSections = new ArrayList<>();
            try {
                doc = Jsoup.connect("http://www.mai.ru/life/sport/sections.php").get();
                table = doc.select("table[class=data-table]").first();
                rows = table.select("tr");
                headers = table.select("th");
                if (table != null) sportSections.clear();
                for (int i = 0; i < headers.size(); i++) {
                    sportSections.add(new SportSectionsHeader(headers.get(i).text()));
                }
                int j = 0;
                for (int i = 1; i < rows.size(); i++) {
                    Elements el = rows.get(i).select("td");
                    if (!el.isEmpty()) {
                        if (el.size() > 2) {
                            SportSectionsBodies body = new SportSectionsBodies(el.get(0).text(), el.get(1).text(), el.get(2).text());
                            sportSections.get(j).addBody(body);
                        } else {
                            SportSectionsBodies body = new SportSectionsBodies(el.get(0).text(), el.get(1).text(), "");
                            sportSections.get(j).addBody(body);
                        }

                    } else {
                        j++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return sportSections;
            }
            return sportSections;
        }

        @Override
        protected void onPostExecute(List<SportSectionsHeader> sportSectionsHeaders) {
            super.onPostExecute(sportSectionsHeaders);
            if (mLoadSportSectionsCallback != null)
                mLoadSportSectionsCallback.loadSportSections(sportSectionsHeaders);
        }
    }
}
