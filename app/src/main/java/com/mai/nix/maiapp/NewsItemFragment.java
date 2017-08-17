package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.mai.nix.maiapp.model.NewsModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nix on 23.04.2017.
 */

public class NewsItemFragment extends Fragment {
    private ListView mListView;
    private NewsAdapter mAdapter;
    private ArrayList<NewsModel> mModels;
    private ProgressBar mProgressBar;
    private int mResult;
    private String mLink;
    private static final String APP_FRAGMENT_ID = "fragment_id";
    public static final byte NEWS_CODE = 0, EVENTS_CODE = 1, ANNOUNCEMENTS_CODE = 2;
    public static NewsItemFragment newInstance(byte code){
        Bundle args = new Bundle();
        args.putSerializable(APP_FRAGMENT_ID, code);
        NewsItemFragment testFragment = new NewsItemFragment();
        testFragment.setArguments(args);
        return  testFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResult = (byte)getArguments().getSerializable(APP_FRAGMENT_ID);
        switch (mResult){
            case NEWS_CODE:
                mLink = "http://mai.ru/press/news/";
                break;
            case EVENTS_CODE:
                mLink = "http://mai.ru/press/events/";
                break;
            case ANNOUNCEMENTS_CODE:
                mLink = "http://mai.ru/press/board/";
                break;
            default:
                mLink = "http://mai.ru/press/news/";
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_orgs_layout, container, false);
        mModels = new ArrayList<>();
        mListView = (ListView) v.findViewById(R.id.stud_org_listview);
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        new MyThread().execute();
        mAdapter = new NewsAdapter(getContext(), mModels);
        mListView.setAdapter(mAdapter);
        return v;
    }

    private class MyThread extends AsyncTask<String, Void, String>{
        private Elements title1, title2, title3;
        private String imgsrc;
        private Document doc;

        @Override
        protected String doInBackground(String... strings) {
            try{
                doc = Jsoup.connect(mLink).get();
                title1 = doc.select("div[class = col-md-9] > h5");
                title2 = doc.select("div[class = col-md-9] > p[class = b-date]");
                title3 = doc.select("img[class = img-responsive]");
                imgsrc = title3.attr("src");
                mModels.clear();

                int kek = title1.size();
                for (int i = 0; i < kek; i++){
                    mModels.add(new NewsModel(title1.get(i).text(), title2.get(i).text(), "http://mai.ru"
                            + title3.get(i).attr("src")));
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            /*if(mMyAdapter == null){
                mMyAdapter = new MyAdapter(mNewsModels);
            }else{
                mMyAdapter.setNewsModelList(mNewsModels);
            }*/
            mListView.setAdapter(mAdapter);
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}
