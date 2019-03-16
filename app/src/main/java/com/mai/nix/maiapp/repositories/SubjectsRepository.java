package com.mai.nix.maiapp.repositories;

import android.os.AsyncTask;
import android.util.Log;

import com.mai.nix.maiapp.MaiApp;
import com.mai.nix.maiapp.model.SubjectBody;
import com.mai.nix.maiapp.model.SubjectHeader;
import com.mai.nix.maiapp.room.AppDatabase;
import com.mai.nix.maiapp.room.SubjectsDao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectsRepository {
    private String mLink;

    public SubjectsRepository(String link) {
        mLink = link;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public interface LoadSubjectsCallback {
        void onLoad(List<SubjectHeader> subjects);
    }

    public void loadCachedDataOrLoad(LoadSubjectsCallback callback) {
        AppDatabase appDatabase = MaiApp.getInstance().getDatabase();
        SubjectsDao subjectsDao = appDatabase.subjectsDao();
        List<SubjectHeader> subjectHeaders = subjectsDao.getAllSubjectHeaders();
        if (!subjectHeaders.isEmpty()) {
            for (SubjectHeader subjectHeader : subjectHeaders) {
                subjectHeader.setChildren(subjectsDao.getById(subjectHeader.getId()));
            }
            callback.onLoad(subjectHeaders);
        } else {
            LoadSubjectsTask loadSubjectsTask = new LoadSubjectsTask(mLink, true, callback);
            loadSubjectsTask.execute();
        }
    }

    public void loadData(LoadSubjectsCallback callback) {
        LoadSubjectsTask loadSubjectsTask = new LoadSubjectsTask(mLink, false, callback);
        loadSubjectsTask.execute();
    }

    static class LoadSubjectsTask extends AsyncTask<Integer, Void, List<SubjectHeader>> {
        private Document doc;
        private Elements primaries;
        private String finalLink;
        private boolean isCaching;
        private final LoadSubjectsCallback callback;

        public LoadSubjectsTask(String link, boolean isC, LoadSubjectsCallback clbck) {
            finalLink = link;
            isCaching = isC;
            callback = clbck;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<SubjectHeader> subjectHeaders) {
            super.onPostExecute(subjectHeaders);
            if (callback != null) {
                callback.onLoad(subjectHeaders);
            }
        }

        @Override
        protected List<SubjectHeader> doInBackground(Integer... integers) {
            List<SubjectHeader> subjects = new ArrayList<>();

            try {
                doc = Jsoup.connect(finalLink).get();
                primaries = doc.select("div[class=sc-table sc-table-day]");
                Log.d("link", finalLink);
                for (Element prim : primaries) {
                    String date = prim.select("div[class=sc-table-col sc-day-header sc-gray]").text();
                    if (date.isEmpty()) {
                        date = prim.select("div[class=sc-table-col sc-day-header sc-blue]").text();
                    }
                    String day = prim.select("span[class=sc-day]").text();
                    SubjectHeader header = new SubjectHeader(date, day);
                    ArrayList<SubjectBody> bodies = new ArrayList<>();
                    Elements times = prim.select("div[class=sc-table-col sc-item-time]");
                    Elements types = prim.select("div[class=sc-table-col sc-item-type]");
                    Elements titles = prim.select("span[class=sc-title]");
                    Elements teachers = prim.select("div[class=sc-table-col sc-item-title]");
                    Elements rooms = prim.select("div[class=sc-table-col sc-item-location]");
                    for (int i = 0; i < times.size(); i++) {
                        SubjectBody body = new SubjectBody(titles.get(i).text(),
                                teachers.get(i).select("span[class=sc-lecturer]").text(),
                                types.get(i).text(), times.get(i).text(), rooms.get(i).text());
                        bodies.add(body);
                    }
                    header.setChildren(bodies);
                    subjects.add(header);
                }

                if (isCaching && !subjects.isEmpty()) {
                    AppDatabase appDatabase = MaiApp.getInstance().getDatabase();
                    SubjectsDao subjectsDao = appDatabase.subjectsDao();
                    subjectsDao.clearSubjectHeaders(subjectsDao.getAllSubjectHeaders());
                    subjectsDao.clearSubjectBodies(subjectsDao.getAllSubjectBodies());
                    for (SubjectHeader header : subjects) {
                        long id = subjectsDao.insertSubjectHeader(header);
                        for (SubjectBody subjectBody : header.getChildren()) {
                            subjectBody.setSubjectId(id);
                            subjectsDao.insertSubjectBody(subjectBody);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                return null;
            }
            return subjects;
        }
    }
}
