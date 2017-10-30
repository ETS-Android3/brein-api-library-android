package com.brein.api;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class BreinNotficationService extends FirebaseMessagingService {

    private static final String TAG = "BreinNotficationService";

    /**
     * Invoked in case of ...
     * @param remoteMessage  RemoteMessage
     */
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        final Map<String, String> dataMap = remoteMessage.getData();
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            Log.d(TAG, "Key : " + entry.getKey() + " Value : " + entry.getValue());
        }

        sendNotification(remoteMessage);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage RemoteMessage FCM message
     */
    private void sendNotification(final RemoteMessage remoteMessage) {

        if (remoteMessage == null) {
            Log.d(TAG, "remote notification: message not set!  -> no notification shown.");
            return;
        }

        final String title = remoteMessage.getData().get("title");
        final String message = remoteMessage.getData().get("message");

        if (title == null) {
            Log.d(TAG, "remote notification: title not set! -> no notification shown.");
            return;
        }

        if (message == null) {
            Log.d(TAG, "remote notification: message not set! -> no notification shown.");
            return;
        }

        final Intent intent = new Intent(this, BreinifyManager.getInstance().getMainActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        final Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.color.transparent)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}