package com.spontaneous.android.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.spontaneous.android.R;
import com.spontaneous.android.activity.ActivityEventInvitation;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.Item;
import com.spontaneous.android.util.GsonFactory;
import com.spontaneous.android.util.Logger;

/**
 * This class is a listener for GCM push notifications.
 */
public class SpontaneousGcmListenerService extends GcmListenerService {

    /**
     * Called when message is received.
     *
     * @param from Id of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");

        Logger.info(String.format("Received GCM message from '%s'. The message is '%s'.",
                from, message));

        //Show a notification.
        showNotification(data);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param data The notification data.
     */
    private void showNotification(Bundle data) {

        //Get the notification details.
        NotificationType notificationType = NotificationType.valueOf(data.getString("type"));
        String title = data.getString("title");
        String content = data.getString("content");

        //Notification configuration.
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_event_white)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        //Build the notification based on the notification type.
        //Notification that the user was invited to a new event.
        if (notificationType == NotificationType.INVITATION) {

            //Get event details.
            Event event = GsonFactory.getGson()
                    .fromJson(data.getString("data"), Event.class);

            //Set the intent.
            Intent intent = new Intent(this, ActivityEventInvitation.class);

            //The action to perform when clicking the notification.
            intent.putExtra(getString(R.string.event_invitation), event);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            notificationBuilder.setContentIntent(pendingIntent);

            //Notification that the user was assigned to bring an item to an event.
        } else if (notificationType == NotificationType.ASSIGN_ITEM) {

            Item item = GsonFactory.getGson()
                    .fromJson(data.getString("data"), Item.class);

            //Initialize the call to the broadcast receiver.
            //Intent for confirmation.
            Intent confirm = new Intent(this, ItemAssignmentBroadcastReceiver.class);
            confirm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            confirm.putExtra(getString(R.string.assigned_item_data), item);
            confirm.putExtra(getString(R.string.is_bringing_item), true);

            PendingIntent pConfirm = PendingIntent.getBroadcast(this, 1, confirm, PendingIntent.FLAG_UPDATE_CURRENT);

            //Intent for declining.
            Intent decline = new Intent(this, ItemAssignmentBroadcastReceiver.class);
            decline.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            decline.putExtra(getString(R.string.assigned_item_data), item);
            decline.putExtra(getString(R.string.is_bringing_item), false);

            PendingIntent pDecline = PendingIntent.getBroadcast(this, 2, decline, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder
                    .addAction(R.drawable.ic_done_black, "Yes", pConfirm)
                    .addAction(R.drawable.ic_close_black, "No", pDecline);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * This class represents the type of notificationType sent to the user's device.
     */
    enum NotificationType {

        /**
         * Invitation to a created event.
         */
        INVITATION,

        /**
         * An assignment to bring an item to the event.
         */
        ASSIGN_ITEM,

        /**
         * The event host can send a broadcast message to his guests.
         */
        BROADCAST
    }
}
