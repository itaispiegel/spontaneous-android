package com.spontaneous.android.gcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.activity.ActivityMain;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Item;
import com.spontaneous.android.util.Logger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This broadcast receiver is called when the user receives a request to bring an item, and he confirms/declines it.
 * The broadcast receiver will send a request back to the server with the user's respond.
 */
public class ItemAssignmentBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        Logger.info("Received broadcast");

        String actionTag = context.getString(R.string.is_bringing_item);
        String itemDataTag = context.getString(R.string.assigned_item_data);

        boolean action = intent.getExtras().getBoolean(actionTag);
        Item item = (Item) intent.getExtras().getSerializable(itemDataTag);

        //Dismiss the notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        if (item != null) {
            Logger.info("Did user confirm: " + action);
            item.setBringing(action);

            SpontaneousApplication.getInstance()
                    .getService(EventService.class)
                    .updateItem(item.getId(), action, new Callback<BaseResponse>() {
                        @Override
                        public void success(BaseResponse baseResponse, Response response) {
                            Logger.info("Item updated successfully.");

                            Toast.makeText(context, "Item update succeeded", Toast.LENGTH_SHORT).show();

                            Intent mainActivity = new Intent(context, ActivityMain.class);
                            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(mainActivity);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Logger.error("Error while updating item: \n");
                            Logger.error(error.getMessage());

                            Toast.makeText(context, "Item update failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
