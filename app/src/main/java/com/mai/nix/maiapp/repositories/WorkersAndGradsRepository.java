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

public class WorkersAndGradsRepository {

    public interface LoadOrgsCallback {
        void onLoad(List<StudentOrgModel> subjects);
    }

    public void loadData(LoadOrgsCallback callback) {
        WorkersAndGradsLoadTask workersAndGradsLoadTask = new WorkersAndGradsLoadTask(callback);
        workersAndGradsLoadTask.execute();
    }

    static class WorkersAndGradsLoadTask extends AsyncTask<Integer, Void, List<StudentOrgModel>> {
        private Document doc;
        private Element table;
        private Elements rows, cols;
        private LoadOrgsCallback callback;

        public WorkersAndGradsLoadTask(LoadOrgsCallback clbck) {
            callback = clbck;
        }

        @Override
        protected List<StudentOrgModel> doInBackground(Integer... integers) {
            List<StudentOrgModel> studentOrgs = new ArrayList<>();
            try {
                doc = Jsoup.connect("http://www.mai.ru/life/associations/").get();
                table = doc.select("table[class=data-table]").first();
                rows = table.select("th");
                cols = table.select("td");
                if (table != null) studentOrgs.clear();
                int i = 0;
                for (int j = 0; j < cols.size(); j += 3) {
                    studentOrgs.add(new StudentOrgModel(rows.get(i).text(), cols.get(j).text(), cols.get(j + 1).text(),
                            cols.get(j + 2).text()));
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return studentOrgs;
            }
            return studentOrgs;
        }

        @Override
        protected void onPostExecute(List<StudentOrgModel> studentOrgModels) {
            if (callback != null) callback.onLoad(studentOrgModels);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
