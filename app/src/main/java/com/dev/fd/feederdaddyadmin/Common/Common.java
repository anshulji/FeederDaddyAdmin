package com.dev.fd.feederdaddyadmin.Common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dev.fd.feederdaddyadmin.Remote.APIService;
import com.dev.fd.feederdaddyadmin.Remote.FCMRetrofitClient;

import java.util.Calendar;
import java.util.Locale;

public class Common {

    public static String currentfragment="ordermeal";
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String SETASBANNER = "Set As Banner";
    public static final String HIDEORSHOW = "Hide OR Show";
    private static final String  fcmUrl = "https://fcm.googleapis.com/";


    public static APIService getFCMClient()
    {
        return FCMRetrofitClient.getClient(fcmUrl).create(APIService.class);
    }

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On my way";
        else
            return "shipped";

    }

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static String getDate(long time)
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(android.text.format.DateFormat.format("dd MMM yyyy, hh:mm a",calendar).toString());
        return date.toString();
    }

}
