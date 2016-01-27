package com.spontaneous.android.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.spontaneous.android.R;
import com.spontaneous.android.activity.ActivityEventPage;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.util.GsonFactory;
import com.spontaneous.android.util.Logger;

/**
 * This class is a listener for GCM push notifications.
 */
public class SpontaneousGcmListenerService extends GcmListenerService {

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Logger.info(String.format("Received GCM message from '%s'. The message is '%s'",
                from, message));

        Event event = GsonFactory.getGson()
                .fromJson(data.getString("event"), Event.class);

        //Show a notification.
        sendNotification(message, event);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, Event event) {

        Intent intent = new Intent(this, ActivityEventPage.class);
        intent.putExtra(getString(R.string.event_card_intent_extras), event);
        intent.putExtra(getString(R.string.event_is_invitation), true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
