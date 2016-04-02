package com.spontaneous.android.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.spontaneous.android.R;
import com.spontaneous.android.activity.ActivityMain;
import com.spontaneous.android.util.DateTimeFormatter;

import org.joda.time.DateTime;

import java.util.Locale;

/**
 * This class is a receiver for receiving reminders of events.
 */
public class AlarmReceiver extends BroadcastReceiver {

    //The action of the notification
    public static final String REMINDER_ACTION = "com.spontaneous.android.reminder";

    //Keys for the notification data
    public static final String EVENT_TITLE = "event_title";
    public static final String EVENT_DATE = "event_date";

    @Override
    public void onReceive(Context context, Intent intent) {

        //Continue only if the action equals our custom action.
        if(!intent.getAction().equals(REMINDER_ACTION)) {
            return;
        }

        //Get the notification details.
        final String eventTitle = intent.getExtras().getString(EVENT_TITLE);
        final DateTime eventDate = (DateTime) intent.getExtras().get(EVENT_DATE);

        final String message = String.format(Locale.getDefault(), "Remember that are invited to the event at %s!", DateTimeFormatter.format(eventDate));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Show the notification.
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(eventTitle)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, ActivityMain.class), 0))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .build();

        notificationManager.notify(0, notification);
    }
}
