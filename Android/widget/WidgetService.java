package com.screwattack.sgcapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by juanreyes on 5/3/14.
 */
//@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class WidgetService extends RemoteViewsService {
    /*
    * So pretty simple just defining the Adapter of the listview
    * here Adapter is ListProvider
    * */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        Intent initialUpdateIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        initialUpdateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        sendBroadcast(initialUpdateIntent);

        return (new ListProvider(this.getApplicationContext(), intent));
    }
}