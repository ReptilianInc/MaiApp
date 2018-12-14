package com.mai.nix.maiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mai.nix.maiapp.model.NewsModel;
import java.util.ArrayList;

/**
 * Created by Nix on 17.08.2017.
 */

public class NewsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NewsModel> mModels;
    public NewsAdapter(Context context, ArrayList<NewsModel> models) {
        mContext = context;
        mModels = models;
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.news_card, viewGroup, false);
        }
        String url = mModels.get(i).getLink();
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView1 = view.findViewById(R.id.date_textview);
        TextView textView2 = view.findViewById(R.id.header_textview);
        textView1.setText(mModels.get(i).getDate());
        textView2.setText(mModels.get(i).getText());
        Glide
                .with(mContext)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
        return view;
    }
}
