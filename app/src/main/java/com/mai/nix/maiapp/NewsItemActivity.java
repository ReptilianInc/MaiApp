package com.mai.nix.maiapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsItemActivity extends AppCompatActivity {
    private static final String TYPE = "com.mai.nix.maiapp.type";
    private static final String ID = "com.mai.nix.maiapp.id";
    private final String MAIN_LINK = "http://mai.ru/press/";
    private final String ULTRA_LINK = "http://mai.ru";
    private String mType, mId;
    private TextView mDateView, mTitleView, mLongRead, mAuthorView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private String mDate, mTitle, mText, mAuthor;
    private Bitmap mBitmap;
    public static Intent newIntent(Context context, String type, String id){
        Intent intent = new Intent(context, NewsItemActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(ID, id);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        mId = getIntent().getStringExtra(ID);
        mType = getIntent().getStringExtra(TYPE);

        Log.d(mId, mType);

        mDateView = (TextView)findViewById(R.id.date_view);
        mTitleView = (TextView)findViewById(R.id.title_view);
        mLongRead = (TextView)findViewById(R.id.news_text);
        mAuthorView = (TextView)findViewById(R.id.author_view);
        mImageView = (ImageView)findViewById(R.id.image_view);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        new MyThread().execute();

    }
    private class MyThread extends AsyncTask<String, Void, String>{
        private Element date, title, text, author;
        private Document doc;
        private String image;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            mDateView.setText(mDate);
            mTitleView.setText(mTitle);
            mImageView.setImageBitmap(mBitmap);
            mLongRead.setText(mText);
            mAuthorView.setText(mAuthor);
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            try{
                if(mType.isEmpty()){
                    doc = Jsoup.connect(ULTRA_LINK.concat(mType).concat(mId)).get();
                }else{
                    doc = Jsoup.connect(MAIN_LINK.concat(mType).concat(mId)).get();
                }
                title = doc.select("div[class = col-md-12] > h3").first();
                date = doc.select("div[class = b-date]").first();
                text = doc.select("div[class = text text-lg]").first();
                author = doc.select("td[class = j-padd-bottom-xs]").first();
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
                URL url = new URL(ULTRA_LINK.concat(image));
                Log.d("image link", ULTRA_LINK.concat(image));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                mBitmap = BitmapFactory.decodeStream(inputStream);

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
