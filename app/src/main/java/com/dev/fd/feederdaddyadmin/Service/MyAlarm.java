package com.dev.fd.feederdaddyadmin.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dev.fd.feederdaddyadmin.CurrentOrdersActivity;
import com.dev.fd.feederdaddyadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//class extending the Broadcast Receiver
public class MyAlarm extends BroadcastReceiver {

    //the method will be fired when the alarm is triggerred
    @Override
    public void onReceive(final Context context, Intent intent) {

        //you can check the log that it is fired
        //Here we are actually not doing anything
        //but you can do any task here that you want to be done at a specific time everyday

        if(intent.getStringExtra("currentrequest")!=null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference("CurrentRequests").child(intent.getStringExtra("currentrequest"));

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String didresacc = dataSnapshot.child("didrestaurantaccepted").getValue().toString();
                    if (didresacc.equals("0")) {
                        databaseReference.child("orderstatus").setValue("-2");
                        String city = dataSnapshot.child("city").getValue().toString();
                        String zone = dataSnapshot.child("zone").getValue().toString();

                        databaseReference.child("city_zone_status").setValue(city + zone + "-2");

                        databaseReference.child("orderstatusmessage").setValue("We are manually calling restaurant to confirm your order!");

                        Intent inte = new Intent(context, CurrentOrdersActivity.class);
                        inte.putExtra("orderstatus", "oncbr");
                        inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, inte, PendingIntent.FLAG_ONE_SHOT);


                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("Order #" + dataSnapshot.getRef().getKey().substring(1, dataSnapshot.getRef().getKey().length()) + " still not confirmed by restaurant"))
                                .setContentText("Order #" + dataSnapshot.getRef().getKey().substring(1, dataSnapshot.getRef().getKey().length()) + " still not confirmed by restaurant")
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.drawable.feeder_daddy_logo)
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                        R.drawable.feeder_daddy_logo))
                                .setContentTitle("Order Not Confirmed By Restaurant!")
                                .setSound(defaultSoundUri);

                        NotificationManager noti = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        noti.notify(0, builder.build());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Log.d("MyAlarmBelal", "Alarm just fired");
        }
        else if(intent.getStringExtra("notititle")!=null)
        {
            String notititle =  intent.getStringExtra("notititle");


            Intent inte = new Intent(context, CurrentOrdersActivity.class);
            inte.putExtra("orderstatus","ao");
            inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,inte, PendingIntent.FLAG_ONE_SHOT);


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("This order is to be delivered 45 min later!"))
                    .setContentText("This order is to be delivered 45 min later!")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.feeder_daddy_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.feeder_daddy_logo))
                    .setContentTitle(notititle)
                    .setSound(defaultSoundUri);

            NotificationManager noti = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            noti.notify(0,builder.build());
        }

    }

}
