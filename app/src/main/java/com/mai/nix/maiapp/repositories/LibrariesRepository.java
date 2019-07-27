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

public class LibrariesRepository {
    public interface LoadLibrariesCallback {
        void loadLibraries(List<SportSectionsHeader> libraries);
    }

    public void loadData(LoadLibrariesCallback loadLibrariesCallback) {
        LoadLibrariesTask loadLibrariesTask = new LoadLibrariesTask(loadLibrariesCallback);
        loadLibrariesTask.execute();
    }

    static class LoadLibrariesTask extends AsyncTask<Integer, Void, List<SportSectionsHeader>> {
        private Document doc;
        private Element table;
        private Elements rows;
        private final LoadLibrariesCallback mLoadLibrariesCallback;

        public LoadLibrariesTask(LoadLibrariesCallback callback) {
            mLoadLibrariesCallback = callback;
        }

        @Override
        protected List<SportSectionsHeader> doInBackground(Integer... integers) {
            List<SportSectionsHeader> libraries = new ArrayList<>();
            try {
                doc = Jsoup.connect("http://mai.ru/common/campus/library/").get();
                table = doc.select("table[class = table table-bordered table-hover]").first();
                rows = doc.select("tr");
                if (table != null) libraries.clear();
                for (int i = 0; i < rows.size(); i++) {
                    if (rows.get(i).select("th").size() == 1) {
                        libraries.add(new SportSectionsHeader(rows.get(i).text()));
                    }
                }
                int j = 0;
                for (int i = 2; i < rows.size(); i++) {
                    Elements el = rows.get(i).select("td");
                    if (!el.isEmpty()) {
                        SportSectionsBodies body = new SportSectionsBodies(
                                el.get(0).text(),
                                el.get(1).html());
                        libraries.get(j).addBody(body);
                    } else if (rows.get(i).select("th").size() == 1) {
                        j++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return libraries;
            }
            return libraries;
        }

        @Override
        protected void onPostExecute(List<SportSectionsHeader> sportSectionsHeaders) {
            super.onPostExecute(sportSectionsHeaders);
            if (mLoadLibrariesCallback != null) mLoadLibrariesCallback.loadLibraries(sportSectionsHeaders);
        }
    }
}
