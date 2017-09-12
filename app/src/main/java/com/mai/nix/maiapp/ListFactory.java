package com.mai.nix.maiapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.mai.nix.maiapp.model.SubjectBody;
import java.util.ArrayList;

/**
 * Created by Nix on 12.09.2017.
 */

public class ListFactory implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<SubjectBody> mData;
    Context mContext;
    int widgetID;

    ListFactory(Context ctx, Intent intent) {
        mContext = ctx;
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mData = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        mData.clear();
        for(int i = 0; i < 3; i++){
            mData.add(new SubjectBody("i" + i, "i" + i, "i" + i, "i" + i, "i" + i));
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rView = new RemoteViews(mContext.getPackageName(),
                R.layout.child_view);
        rView.setTextViewText(R.id.subject_time, mData.get(i).getTime());
        rView.setTextViewText(R.id.subject_type, mData.get(i).getType());
        rView.setTextViewText(R.id.subject_title, mData.get(i).getTitle());
        rView.setTextViewText(R.id.subject_teacher, mData.get(i).getTeacher());
        rView.setTextViewText(R.id.subject_room, mData.get(i).getRoom());
        return rView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
