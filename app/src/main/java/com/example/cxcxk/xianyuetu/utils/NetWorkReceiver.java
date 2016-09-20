package com.example.cxcxk.xianyuetu.utils;

/**
 * Created by cxcxk on 2016/8/3.
 */
public interface NetWorkReceiver {

    void notifyRefresh();

    void notifyNoNetWork();

    void notifyCancleRefresh(int index, boolean isBack);

}
