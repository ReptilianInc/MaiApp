package com.mai.nix.maiapp.repositories;

import android.os.AsyncTask;

import com.mai.nix.maiapp.model.StudentOrgModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CafesRepository {

    public interface LoadCafesCallback {
        void onLoad(List<StudentOrgModel> cafes);
    }

    public void loadData(LoadCafesCallback callback) {
        LoadCafesTask loadCafesTask = new LoadCafesTask(callback);
        loadCafesTask.execute();
    }

    static class LoadCafesTask extends AsyncTask<Integer, Void, List<StudentOrgModel>> {
        private Document doc;
        private Element table;
        private Elements rows;
        private final LoadCafesCallback callback;

        public LoadCafesTask(LoadCafesCallback clbck) {
            callback = clbck;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<StudentOrgModel> studentOrgModels) {
            super.onPostExecute(studentOrgModels);
            if (callback != null) callback.onLoad(studentOrgModels);
        }

        @Override
        protected List<StudentOrgModel> doInBackground(Integer... integers) {
            List<StudentOrgModel> cafes = new ArrayList<>();
            try {
                doc = Jsoup.connect("http://mai.ru/common/campus/cafeteria/").get();
                table = doc.select("table[class = table table-bordered]").first();
                rows = table.select("tr");
                if (table != null) cafes.clear();
                for (int i = 1; i < rows.size(); i++) {
                    Elements el = rows.get(i).select("td");
                    cafes.add(new StudentOrgModel(el.get(1).text(), "", el.get(2).text()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return cafes;
            }
            return cafes;
        }
    }
}
