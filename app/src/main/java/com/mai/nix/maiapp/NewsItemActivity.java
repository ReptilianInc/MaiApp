package com.mai.nix.maiapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;

public class NewsItemActivity extends AppCompatActivity {
    private static final String ID = "com.mai.nix.maiapp.id";
    private final String ULTRA_LINK = "https://mai.ru";
    private String mId;
    private TextView mTitleView, mLongRead, mAuthorView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private int mProgressStatus = 0;
    private String mDate, mTitle, mText, mAuthor;
    public static Intent newIntent(Context context, String id){
        Intent intent = new Intent(context, NewsItemActivity.class);
        //intent.putExtra(TYPE, type);
        intent.putExtra(ID, id);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        mId = getIntent().getStringExtra(ID);
        mTitleView = (TextView)findViewById(R.id.title_view);
        mLongRead = (TextView)findViewById(R.id.news_text);
        mAuthorView = (TextView)findViewById(R.id.author_view);
        mImageView = (ImageView)findViewById(R.id.image_view);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(mProgressStatus);
        mProgressBar.setIndeterminate(false);
        mToolbar = (Toolbar)findViewById(R.id.news_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        new MyThread().execute();

    }
    private class MyThread extends AsyncTask<String, Integer, String>{
        private Element date, title, text, author;
        private Document doc;
        private String image;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
            mProgressBar.invalidate();
        }

        @Override
        protected void onPostExecute(String s) {
            mTitleView.setText(mTitle);
            mLongRead.setText(mText);
            mAuthorView.setText(mAuthor);
            mToolbar.setTitle(mDate);
            Glide
                    .with(getApplicationContext())
                    .load(ULTRA_LINK.concat(image))
                    .into(mImageView);
            mProgressBar.setVisibility(View.GONE);
        }
        @Override
        protected String doInBackground(String... strings) {
            try{
                mProgressStatus += 10;
                publishProgress(mProgressStatus);
                doc = Jsoup.connect(mId).get();
                title = doc.select("div[class = col-md-12] > h3").first();
                mProgressStatus += 10;
                publishProgress(mProgressStatus);
                date = doc.select("div[class = b-date]").first();
                mProgressStatus += 10;
                publishProgress(mProgressStatus);
                text = doc.select("div[class = text text-lg]").first();
                mProgressStatus += 10;
                publishProgress(mProgressStatus);
                author = doc.select("td[class = j-padd-bottom-xs]").first();
                mProgressStatus += 10;
                publishProgress(mProgressStatus);
                image = doc.select("img[class = img-responsive hidden-xs pull-left j-marg-right-lg j-marg-bottom]")
                        .attr("src");

                mTitle = title.text();
                if(date != null){
                    mDate = date.text();
                }else{
                    mDate = getResources().getString(R.string.place_holder);
                }

                mText = text.text();
                if(author != null){
                    mAuthor = author.text();
                }else{
                    mAuthor = getResources().getString(R.string.place_holder);
                }
                mProgressStatus += 50;
                publishProgress(mProgressStatus);
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
