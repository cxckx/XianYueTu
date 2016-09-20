package com.example.cxcxk.xianyuetu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;


import com.example.cxcxk.xianyuetu.AppApplication;

/**
 * Created by cxcxk on 2016/8/2.
 */
public class ConnectUtils {

    public static boolean isConnect() {
        ConnectivityManager connectivityManagerCompat = (ConnectivityManager) AppApplication.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivityManagerCompat.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }

        return false;
    }
}
