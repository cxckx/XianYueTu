package com.example.cxcxk.xianyuetu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.cxcxk.xianyuetu.utils.ConnectUtils;
import com.example.cxcxk.xianyuetu.utils.NetWorkObserver;
import com.example.cxcxk.xianyuetu.utils.SelectSender;

/**
 * Created by cxcxk on 2016/8/2.
 */
public class NetWorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (ConnectUtils.isConnect()) {
                NetWorkObserver.getInstance().notifyRefresh();
            } else {
                Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
