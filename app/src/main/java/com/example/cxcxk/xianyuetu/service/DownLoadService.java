package com.example.cxcxk.xianyuetu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.ImageNetDataOperator;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.xianyuetu.utils.SelectSender;

import java.util.HashMap;
import java.util.Map;

/**
 * 暂时没用到
 * Created by cxcxk on 2016/7/29.
 */
public class DownLoadService extends Service {

    private ImageNetDataOperator operator = new ImageNetDataOperator();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        final String id = intent.getStringExtra("id");
        NetDataOperater.Attribute attribute = new NetDataOperater.Attribute();
        attribute.setUrl(url);
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        attribute.setParams(map);

        operator.request(attribute, id);

        operator.setNetWorkListener(new NetDataOperater.INetWork<Boolean>() {
            @Override
            public void OnCompleted(Boolean aBoolean) {
                SelectSender.getInstance().notityDownloadFinish(id);
            }

            @Override
            public void OnError(String s) {
                Log.i("TAGGG", s);
            }

            @Override
            public void OnProgress(final int i) {
                SelectSender.getInstance().notityDownloadProgress(i, id);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        operator.cancleAllRequest();
    }
}
