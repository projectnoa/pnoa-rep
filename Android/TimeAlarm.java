package com.screwattack.sgcapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.screwattack.sgcapp.R;
import com.screwattack.sgcapp.objects.SGCActivity;

/**
 * Created by juanreyes on 4/23/14.
 */
// The class has to extend the BroadcastReceiver to get the notification from the system
public class TimeAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent paramIntent) {
        try {
            if (paramIntent != null && paramIntent.getExtras() != null && paramIntent.hasExtra("SGCActivity")) {
                SGCActivity activity = paramIntent.getParcelableExtra("SGCActivity");

                if (activity != null) {
                    CharSequence title = activity.getActivityName();
                    CharSequence message = activity.getLocation() + " - " + activity.getActivityDesc();

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.sgc_icon)
                            .setAutoCancel(true)
                            .setContentTitle(title);

                    //mBuilder.setSound(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.sound));
                    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                    mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                    mBuilder.setContentText(message);
                    mBuilder.setTicker(message);
                    mBuilder.setWhen(System.currentTimeMillis());

                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    PendingIntent pendingIntent =
                            PendingIntent.getActivity(
                                    context,
                                    Integer.parseInt(activity.getActivityID()),
                                    (Intent) paramIntent.getParcelableExtra("intent"),
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                    mBuilder.setContentIntent(pendingIntent);

                    // Fire the notification
                    notificationManager.notify(
                            "SGC_ACTIVITY_" + activity.getActivityID(),
                            Integer.parseInt(activity.getActivityID()),
                            mBuilder.build());

                    context.getSharedPreferences("SGC_ALERT", Context.MODE_PRIVATE).edit().remove(activity.getActivityID()).commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
