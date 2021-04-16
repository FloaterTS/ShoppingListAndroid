package com.example.shoppinglist;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent goToIntent = new Intent(context, MainActivity.class);
        goToIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingGoToIntent = PendingIntent.getActivity(context, 0, goToIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyRemind")
                .setSmallIcon(R.drawable.ic_baseline_local_grocery_store)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingGoToIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200, builder.build());
    }
}
