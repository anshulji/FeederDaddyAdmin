package com.dev.fd.feederdaddyadmin.Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


import com.dev.fd.feederdaddyadmin.CurrentOrdersActivity;
import com.dev.fd.feederdaddyadmin.MainActivity;
import com.dev.fd.feederdaddyadmin.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if(notification.getTitle().substring(0,6).equals("45 min"))
        {
            //set 45min timer before order delivery?

            Calendar calendar = Calendar.getInstance();
            long time;

            //getting the alarm manager
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            //creating a new intent specifying the broadcast receiver
            Intent inte = new Intent(this, MyAlarm.class);
            inte.putExtra("notititle",notification.getTitle());
            inte.putExtra("advalarm","1");

            //creating a pending intent using the intent
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, inte, 0);

            //setting the repeating alarm that will be fired

            //We need a calendar object to get the specified time in millis
            //as the alarm manager method takes time in millis to setup the alarm
            int hr,min;
            hr =  Integer.parseInt(notification.getBody().substring(0,2));
            min = Integer.parseInt(notification.getBody().substring(3,5));


            if(min>=45)
            {
                min-=45;
            }
            else
            {
                if(hr==0) {
                    hr = 23;
                }
                else
                {
                    hr-=1;
                }

                min = 60 - (45-min);
            }

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    hr,min,0);
            time = calendar.getTimeInMillis();
            am.set(AlarmManager.RTC, time, pi);


//am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);

        }
        else {
            Intent intent = new Intent(remoteMessage.getNotification().getClickAction());

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(notification.getBody()))
                    .setContentText(notification.getBody())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.feeder_daddy_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.drawable.feeder_daddy_logo))
                    .setContentTitle(notification.getTitle())
                    .setSound(defaultSoundUri);

            NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            noti.notify(0, builder.build());
        }

    }
}
