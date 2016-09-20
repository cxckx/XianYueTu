package com.example.cxcxk.xianyuetu.utils;

import com.example.cxcxk.android_my_library.utils.NetDataOperater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by cxcxk on 2016/7/22.
 */
public class JsonNetDataOperator extends NetDataOperater<String> {
    @Override
    protected String onNetWork(Attribute attribute) {
        String urls = attribute.getUrl();
        urls += "?";
        for (String key : attribute.getParams().keySet()) {
            urls += key + "=" + attribute.getParams().get(key) + "&";
        }
        urls = urls.substring(0, urls.length() - 1);

        try {
            URL url = new URL(urls);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setUseCaches(false);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String json = reader.readLine();
                reader.close();
                return json;

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
