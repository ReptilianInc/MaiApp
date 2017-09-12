package com.mai.nix.maiapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Nix on 12.09.2017.
 */

public class TestWidgetClass extends AppWidgetProvider {
    private DataLab mDataLab;

    public static String GO_BACK = "gb";
    public static String GO_FORWARD = "gf";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        /*mDataLab = DataLab.get(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.testwidget_layout);

        Intent active = new Intent(context, TestWidgetClass.class);
        active.setAction(GO_BACK);
        active.putExtra("msg", "Back pressed");

        Intent active1 = new Intent(context, TestWidgetClass.class);
        active1.setAction(GO_FORWARD);
        active1.putExtra("msg", "Forward pressed");

        //создаем наше событие
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        PendingIntent actionPendingIntent1 = PendingIntent.getBroadcast(context, 0, active1, 0);
        //регистрируем наше событие
        remoteViews.setOnClickPendingIntent(R.id.go_back, actionPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.go_further, actionPendingIntent1);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);*/
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //Ловим наш Broadcast, проверяем и выводим сообщение
        /*final String action = intent.getAction();
        if (action.equals(GO_BACK)) {
            String msg = "null";
            try {
                msg = intent.getStringExtra("msg");
            } catch (NullPointerException e) {
                Log.e("Error", "msg = null");
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }else{
            String msg = "null";
            try {
                msg = intent.getStringExtra("msg");
            } catch (NullPointerException e) {
                Log.e("Error", "msg = null");
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }*/
        super.onReceive(context, intent);
    }
    void setList(RemoteViews rv, Context context, int appWidgetId){
        Intent adapter = new Intent(context, WidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.widget_listview, adapter);
    }
    void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.testwidget_layout);
        setUpdateTV(rv, context, appWidgetId);
        setList(rv, context, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }
    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId){
        rv.setTextViewText(R.id.date_text, "kek");
        Intent updIntent = new Intent(context, TestWidgetClass.class);
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId });
        PendingIntent updPIntent = PendingIntent.getBroadcast(context, appWidgetId, updIntent, 0);
        rv.setOnClickPendingIntent(R.id.date_text, updPIntent);
    }
}
