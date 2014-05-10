package com.screwattack.sgcapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import com.screwattack.sgcapp.BaseActivity;
import com.screwattack.sgcapp.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
//@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class SGCWidget extends AppWidgetProvider {

    private static int randomNumber;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
            //updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction() != null && intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), SGCWidget.class.getName());

            if (appWidgetManager != null) {
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);

                //Log.e("finally after a whole day", "working :");
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        randomNumber = 1;
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.sgcwidget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // embed extras so they don't get ignored
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        svcIntent.putExtra("random", Calendar.getInstance(Locale.US).toString());
        randomNumber++;
        Log.e("RANDOM NUMBER", String.valueOf(randomNumber));

        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(/*appWidgetId,*/ R.id.widget_listView, svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.widget_listView, R.id.empty_view);

        // set intent for item click (opens activity)
        Intent activityIntent = new Intent(context, BaseActivity.class);

        activityIntent.setAction(BaseActivity.ACTION_VIEW_OPEN_ACTIVITY);
        activityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        activityIntent.setData(Uri.parse(activityIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
        remoteViews.setPendingIntentTemplate(R.id.widget_listView, activityPendingIntent);

        // set intent for item click (opens app)
        Intent appIntent = new Intent(context, BaseActivity.class);

        appIntent.setAction(BaseActivity.ACTION_VIEW_OPEN_APP);
        appIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        appIntent.setData(Uri.parse(appIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.icon_imageView, appPendingIntent);

        // set intent for item click (opens profile)
        Intent profileIntent = new Intent(context, BaseActivity.class);

        profileIntent.setAction(BaseActivity.ACTION_VIEW_OPEN_PROFILE);
        profileIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        profileIntent.setData(Uri.parse(profileIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent profilePendingIntent = PendingIntent.getActivity(context, 0, profileIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_profile_button, profilePendingIntent);

        // set intent for item click (opens checkIn)
        Intent checkInIntent = new Intent(context, BaseActivity.class);

        checkInIntent.setAction(BaseActivity.ACTION_VIEW_CHECK_IN);
        checkInIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        checkInIntent.setData(Uri.parse(checkInIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent checkInPendingIntent = PendingIntent.getActivity(context, 0, checkInIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_checkin_button, checkInPendingIntent);

        return remoteViews;
    }

    /*static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //CharSequence widgetText = context.getString(R.string.appwidget_text);

        ArrayList<SGCActivity> currentActivities = DataHolder.getInstance().getSgcCurrentActivities();


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sgcwidget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        //views

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }*/
}


