package com.mai.nix.maiapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;

public class NewsItemActivity extends AppCompatActivity {
    private static final String ID = "com.mai.nix.maiapp.id";
    private static final String DATE = "com.mai.nix.maiapp.date";
    private static final String TITLE = "com.mai.nix.maiapp.title";
    private static final String BITMAP = "com.mai.nix.maiapp.bitmap";
    private final String ULTRA_LINK = "https://mai.ru";
    private String mId, mTitleGet, mDateGet;
    private byte[] mBitmapGet;
    private TextView mTitleView, mLongRead, mAuthorView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private String mText, mAuthor;
    public static Intent newIntent(Context context, String id, String date, String title, byte[] bytes){
        Intent intent = new Intent(context, NewsItemActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(DATE, date);
        intent.putExtra(TITLE, title);
        intent.putExtra(BITMAP, bytes);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        mId = getIntent().getStringExtra(ID);
        mDateGet = getIntent().getStringExtra(DATE);
        mTitleGet = getIntent().getStringExtra(TITLE);
        mBitmapGet = getIntent().getByteArrayExtra(BITMAP);
        mTitleView = (TextView)findViewById(R.id.title_view);
        mLongRead = (TextView)findViewById(R.id.news_text);
        mAuthorView = (TextView)findViewById(R.id.author_view);
        mImageView = (ImageView)findViewById(R.id.image_view);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mToolbar = (Toolbar)findViewById(R.id.news_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mTitleView.setText(mTitleGet);
        getSupportActionBar().setTitle(mDateGet);
        if(mBitmapGet.length != 0){
            Bitmap bmp = BitmapFactory.decodeByteArray(mBitmapGet, 0, mBitmapGet.length);
            mImageView.setImageBitmap(bmp);
        }
        new MyThread().execute();

    }
    private class MyThread extends AsyncTask<String, Integer, String>{
        private Element text, author;
        private Document doc;
        private String image;

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.GONE);
            if(s == null){
                Toast.makeText(NewsItemActivity.this, R.string.error,
                        Toast.LENGTH_LONG).show();
            }else{
                mLongRead.setText(mText);
                mAuthorView.setText(mAuthor);
                if(mBitmapGet.length == 0){
                    Glide
                            .with(getApplicationContext())
                            .load(ULTRA_LINK.concat(image))
                            .into(mImageView);
                }
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            try{
                doc = Jsoup.connect(mId).get();
                text = doc.select("div[class = text text-lg]").first();
                author = doc.select("td[class = j-padd-bottom-xs]").first();;
                image = doc.select("img[class = img-responsive hidden-xs pull-left j-marg-right-lg j-marg-bottom]")
                        .attr("src");
                mText = text.text();
                if(author != null){
                    mAuthor = author.text();
                }else{
                    mAuthor = getResources().getString(R.string.place_holder);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return author.text();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.go_web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.go_web_in_frags:
                Uri uri = Uri.parse(mId);
                if(UserSettings.getLinksPreference(this).equals(UserSettings.ONLY_BROWSER)){
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else{
                    Intent intent = WebViewActivity.newInstance(this, uri);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
