package com.bts.adamcrm;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

public class MyApplication extends Application {
    public static Context context;
    public static ConnectivityManager mConnManager;

    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        mConnManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
