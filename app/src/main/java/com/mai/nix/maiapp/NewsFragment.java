package com.mai.nix.maiapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.mai.nix.maiapp.model.NewsModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nix on 23.04.2017.
 */

public class NewsFragment extends Fragment {
    private ListView mListView;
    private NewsAdapter mAdapter;
    private ArrayList<NewsModel> mModels;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mResult;
    private String mLink;
    private static final String APP_FRAGMENT_ID = "fragment_id";
    public static final byte NEWS_CODE = 0, EVENTS_CODE = 1, ANNOUNCEMENTS_CODE = 2;
    private final String mLinkMain = "https://mai.ru";
    public static NewsFragment newInstance(byte code){
        Bundle args = new Bundle();
        args.putSerializable(APP_FRAGMENT_ID, code);
        NewsFragment testFragment = new NewsFragment();
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
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mModels = new ArrayList<>();
        mListView = (ListView) v.findViewById(R.id.stud_org_listview);
        new MyThread().execute();
        mAdapter = new NewsAdapter(getContext(), mModels);
        mListView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new MyThread().execute();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final ImageView imageView = (ImageView) view.findViewById(R.id.image);

                Intent n = NewsItemActivity.newIntent(getContext(),
                        mModels.get(i).getId(),
                        mModels.get(i).getDate(),
                        mModels.get(i).getText(),
                        getByteArrayFromImageView(imageView));
                startActivity(n);
            }
        });
        return v;
    }

    private class MyThread extends AsyncTask<Integer, Void, Integer>{
        private Elements title1, title2, title3, links;
        private Document doc;

        @Override
        protected Integer doInBackground(Integer... integers) {
            int size = 0;
            try{
                doc = Jsoup.connect(mLink).get();
                title1 = doc.select("div[class = col-md-9] > h5");
                links = doc.select("div[class = b-thumbnail]").select("a");
                title2 = doc.select("div[class = col-md-9] > p[class = b-date]");
                title3 = doc.select("img[class = img-responsive]");
                mModels.clear();
                for (int i = 0; i < title1.size(); i++){
                    mModels.add(new NewsModel(title1.get(i).text(), title2.get(i).text(),
                            mLinkMain.concat(title3.get(i).attr("src")), links.get(i).attr("abs:href")));
                    Log.d("suka blyat = ", links.get(i).attr("a[href]"));
                }
                size = title1.size();
            }catch(IOException e){
                e.printStackTrace();
            }catch (NullPointerException n){
                return 0;
            }
            return size;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mSwipeRefreshLayout.setRefreshing(false);
            if(integer == 0){
                if (getContext() != null) Toast.makeText(getContext(), R.string.error,
                        Toast.LENGTH_LONG).show();
            }else {
                mListView.setAdapter(mAdapter);
            }
        }
    }

    public static  byte[] getByteArrayFromImageView(ImageView imageView)
    {
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
        Bitmap bitmap;
        if(bitmapDrawable==null){
            imageView.buildDrawingCache();
            bitmap = imageView.getDrawingCache();
            imageView.buildDrawingCache(false);
        }else
        {
            bitmap = bitmapDrawable .getBitmap();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

}
