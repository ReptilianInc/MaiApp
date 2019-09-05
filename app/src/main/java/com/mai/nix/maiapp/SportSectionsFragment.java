package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.widget.Toast;

import com.mai.nix.maiapp.model.SportSectionsBodies;
import com.mai.nix.maiapp.model.SportSectionsHeaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Nix on 11.08.2017.
 */

public class SportSectionsFragment extends SimpleExpandableListFragment {

    @Override
    protected void releaseThread() {
        new MyThread().execute();
    }

    private class MyThread extends AsyncTask<Integer, Void, Integer> {
        private Document doc;
        private Element table;
        private Elements rows, headers;

        public MyThread() {
            super();
        }

        private String getExtractedText(Element element) {
            List<Node> nodes = element.childNodes();
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<Node> iterator = nodes.iterator();
            while (iterator.hasNext()) {
                Node n = iterator.next();
                if (n instanceof TextNode) {
                    stringBuilder.append(((TextNode) n).text());
                } else if (n instanceof Element) {
                    stringBuilder.append(n.attr("href"));
                }
            }
            return stringBuilder.toString();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int size = 0;
            try {
                doc = Jsoup.connect("http://www.mai.ru/life/sport/sections.php").get();
                table = doc.select("table[class=data-table]").first();
                rows = table.select("tr");
                headers = table.select("th");
                if (table != null) mHeaders.clear();
                for (int i = 0; i < headers.size(); i++) {
                    mHeaders.add(new SportSectionsHeaders(headers.get(i).text()));
                }
                int j = 0;
                for (int i = 1; i < rows.size(); i++) {
                    Elements el = rows.get(i).select("td");
                    if (!el.isEmpty()) {
                        if (el.size() > 2) {
                            SportSectionsBodies body = new SportSectionsBodies(el.get(0).text(), el.get(1).text(), getExtractedText(el.get(2)));
                            mHeaders.get(j).addBody(body);
                        } else {
                            SportSectionsBodies body = new SportSectionsBodies(el.get(0).text(), "", el.get(1).html());
                            mHeaders.get(j).addBody(body);
                        }

                    } else {
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
    }
}
