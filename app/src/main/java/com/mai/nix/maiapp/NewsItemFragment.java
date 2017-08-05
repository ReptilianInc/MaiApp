package com.mai.nix.maiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.mai.nix.maiapp.model.NewsModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nix on 23.04.2017.
 */

public class NewsItemFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private List<NewsModel> mNewsModels;
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
        View v = inflater.inflate(R.layout.news_layout, container, false);
        mNewsModels = new ArrayList<>();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list);
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        new MyThread().execute();
        mMyAdapter = new MyAdapter(mNewsModels);
        mRecyclerView.setAdapter(mMyAdapter);
        return v;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mDateTextView, mHeaderTextView;
        private ImageView mImageView;
        private NewsModel mModel;
        public MyViewHolder(View view){
            super(view);
            mDateTextView = (TextView)view.findViewById(R.id.date_textview);
            mHeaderTextView = (TextView)view.findViewById(R.id.header_textview);
            mImageView = (ImageView)view.findViewById(R.id.image);
        }
        public void bindSunject(NewsModel model){
            mModel = model;
            mDateTextView.setText(mModel.getDate());
            mHeaderTextView.setText(mModel.getText());
            Glide.with(getContext())
                    .load(model.getImagePath())
                    .centerCrop()
                    .placeholder(R.drawable.example)
                    .crossFade()
                    .into(mImageView);
        }

    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<NewsModel> mNewsModelList;
        public MyAdapter(List<NewsModel> models){
            this.mNewsModelList = models;
        }
        public void setNewsModelList(List<NewsModel> models){
            mNewsModelList = models;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            NewsModel model = mNewsModelList.get(position);
            holder.bindSunject(model);
        }

        @Override
        public int getItemCount() {
            return mNewsModelList.size();
        }
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
                mNewsModels.clear();

                int kek = title1.size();
                for (int i = 0; i < kek; i++){
                    mNewsModels.add(new NewsModel(title1.get(i).text(), title2.get(i).text(), "http://mai.ru"
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
            mRecyclerView.setAdapter(mMyAdapter);
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}
