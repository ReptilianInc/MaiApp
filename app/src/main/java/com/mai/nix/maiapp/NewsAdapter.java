package com.mai.nix.maiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mai.nix.maiapp.model.NewsModel;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

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

        CircleImageView imageView = (CircleImageView)view.findViewById(R.id.image);
        TextView textView1 = (TextView)view.findViewById(R.id.date_textview);
        TextView textView2 = (TextView)view.findViewById(R.id.header_textview);
        imageView.setImageBitmap(mModels.get(i).getBitmap());
        textView1.setText(mModels.get(i).getDate());
        textView2.setText(mModels.get(i).getText());
        return view;
    }
}
