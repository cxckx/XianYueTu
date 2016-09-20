package com.example.cxcxk.xianyuetu.utils;

/**
 * Created by cxcxk on 2016/7/28.
 */
public interface SelectReceiver {

    void notifyShow(boolean isShow);

    void notifyDownload();

    void notifyDownloadFinish(String id);

    void notifyDownloadProgress(int progress, String id);

    void notifyColse();

    void notifyShowWhich(boolean isShow, String id);
}
