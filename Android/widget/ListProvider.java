package com.screwattack.sgcapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.screwattack.sgcapp.BaseActivity;
import com.screwattack.sgcapp.R;
import com.screwattack.sgcapp.objects.SGCActivity;
import com.screwattack.sgcapp.persistence.DataHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by juanreyes on 5/3/14.
 */
//@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<SGCActivity> listItemList = new ArrayList<SGCActivity>();
    private Context context = null;
    private int appWidgetId;
    private SimpleDateFormat formater = new SimpleDateFormat("hh:mm a", Locale.US);

    public ListProvider(Context context, Intent intent) {
        this.context = context;

        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        //listItemList = getSgcCurrentActivities();
    }

    public ArrayList<SGCActivity> getSgcCurrentActivities() {
        ArrayList<SGCActivity> sgcCurrentActivities = new ArrayList<SGCActivity>();

        Calendar currentDate = new GregorianCalendar();
        currentDate.setTimeInMillis(System.currentTimeMillis());

        currentDate.set(Calendar.YEAR, 2013);
        currentDate.set(Calendar.MONTH, Calendar.JUNE);
        currentDate.set(Calendar.DAY_OF_MONTH, 22);
        //currentDate.set(Calendar.HOUR_OF_DAY, 00);
        //currentDate.set(Calendar.MINUTE, 15);

        //Log.e("set date", java.text.DateFormat.getDateTimeInstance().format(currentDate.getTime()));

        boolean nextAdded = false;
        boolean isFirst = true;
        boolean conventionStarted = false;

        ArrayList<SGCActivity> sgcActivities = DataHolder.getInstance().getSgcActivities();

        for (SGCActivity activity : sgcActivities) {
            if (!conventionStarted && currentDate.compareTo(activity.getStartDate()) > 0) // > activityStartMillis)
                conventionStarted = true;

            if (currentDate.compareTo(activity.getStartDate()) > 0 && currentDate.compareTo(activity.getEndDate()) < 0) {//(timeInMillis > activityStartMillis && timeInMillis < activityEndMillis) {
                sgcCurrentActivities.add(activity);
                activity.setCurrent(true);
            } else if (!nextAdded && !isFirst && currentDate.compareTo(activity.getStartDate()) < 0) {//timeInMillis < activityStartMillis) {
                nextAdded = true;
                sgcCurrentActivities.add(activity);
            }

            isFirst = false;
        }

        if (sgcCurrentActivities.size() == 0) {
            SGCActivity placeHolderActivity = new SGCActivity();

            if (!conventionStarted) {
                placeHolderActivity.setActivityName(context.getString(R.string.activity_placeholder_before_name));
                placeHolderActivity.setActivityDesc(context.getString(R.string.activity_placeholder_before_description));
                placeHolderActivity.setImageURL(context.getString(R.string.activity_placeholder_before_image));
            } else {
                placeHolderActivity.setActivityName(context.getString(R.string.activity_placeholder_after_name));
                placeHolderActivity.setActivityDesc(context.getString(R.string.activity_placeholder_after_description));
                placeHolderActivity.setImageURL(context.getString(R.string.activity_placeholder_after_image));
            }
        }

        return sgcCurrentActivities;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        listItemList = getSgcCurrentActivities();
    }

    @Override
    public void onDestroy() {
        listItemList.clear();
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        //long id = Long.parseLong(listItemList.get(position).getActivityID());

        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    *
    */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.sgcwidget_row);

        SGCActivity activity = listItemList.get(position);

        remoteView.setTextViewText(R.id.activity_name_textView, activity.getActivityName());
        remoteView.setTextViewText(R.id.activity_location_textView, activity.getLocation());

        String activityTime =
                formater.format(activity.getStartDate().getTime()) +
                " - " +
                formater.format(activity.getEndDate().getTime());

        remoteView.setTextViewText(R.id.activity_time_textView, activityTime);

        boolean hasStream = false;

        if (activity.getTwitch() != null && activity.getTwitch().length() > 0)
            hasStream = true;

        remoteView.setViewVisibility(R.id.stream_imageView, hasStream ? View.VISIBLE : View.GONE);

        // store the buzz ID in the extras so the main activity can use it
        Bundle extras = new Bundle();

        extras.putString(BaseActivity.EXTRA_ID, activity.getActivityID());

        Intent fillInIntent = new Intent();

        fillInIntent.putExtras(extras);

        remoteView.setOnClickFillInIntent(R.id.sgcwidget_row_layout, fillInIntent);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}