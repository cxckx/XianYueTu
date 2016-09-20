package com.example.cxcxk.xianyuetu.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxcxk on 2016/8/3.
 */
public class NetWorkObserver {

    private List<NetWorkReceiver> receivers = new ArrayList<>();
    private static NetWorkObserver sender;

    private NetWorkObserver() {

    }

    public static NetWorkObserver getInstance() {
        if (sender == null) {
            sender = new NetWorkObserver();
        }

        return sender;
    }


    public void registReceiver(NetWorkReceiver receiver) throws Exception {
        if (receivers.contains(receiver)) {
            throw new Exception("已经注册");
        }

        receivers.add(receiver);
    }

    public void unRegistReceiver(NetWorkReceiver receiver) throws Exception {
        if (!receivers.contains(receiver)) {
            throw new Exception("不存在");
        }
        receivers.remove(receiver);
    }

    public void notifyRefresh() {
        for (NetWorkReceiver receiver : receivers) {
            receiver.notifyRefresh();
        }
    }

    public void notifyNoNetWork() {
        for (NetWorkReceiver receiver : receivers) {
            receiver.notifyNoNetWork();
        }
    }

    public void notifyCancleRefresh(int index, boolean isBack) {
        for (NetWorkReceiver receiver : receivers) {
            receiver.notifyCancleRefresh(index, isBack);
        }
    }
}
