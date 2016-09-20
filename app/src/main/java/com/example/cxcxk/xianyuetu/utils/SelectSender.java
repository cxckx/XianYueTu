package com.example.cxcxk.xianyuetu.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxcxk on 2016/7/28.
 */
public class SelectSender {

    private List<SelectReceiver> receivers = new ArrayList<>();
    private static SelectSender sender;

    private SelectSender() {

    }

    public static SelectSender getInstance() {
        if (sender == null) {
            sender = new SelectSender();
        }

        return sender;
    }


    public void registReceiver(SelectReceiver receiver) throws Exception {
        if (receivers.contains(receiver)) {
            throw new Exception("已经注册");
        }

        receivers.add(receiver);
    }

    public void unRegistReceiver(SelectReceiver receiver) throws Exception {
        if (!receivers.contains(receiver)) {
            throw new Exception("不存在");
        }
        receivers.remove(receiver);
    }

    public void unRegistAll() {
        for (int i = 0; i < receivers.size(); i++) {
            receivers.remove(i);
        }

    }


    public void notityShow(boolean isShow) {
        for (SelectReceiver receiver : receivers) {
            receiver.notifyShow(isShow);
        }
    }

    public void notityDownload() {
        for (SelectReceiver receiver : receivers) {
            receiver.notifyDownload();
        }
    }


    public void notityDownloadFinish(String id) {
        for (SelectReceiver receiver : receivers) {
            receiver.notifyDownloadFinish(id);
        }
    }

    public void notityDownloadProgress(int progress, String id) {
        for (SelectReceiver receiver : receivers) {
            receiver.notifyDownloadProgress(progress, id);
        }
    }

    public void notityClose() {
        for (SelectReceiver receiver : receivers) {
            receiver.notifyColse();
        }
    }

    public void notityShowWhich(boolean isShow, String id) {
        for (SelectReceiver receiver : receivers) {
            receiver.notifyShowWhich(isShow, id);
        }
    }
}
