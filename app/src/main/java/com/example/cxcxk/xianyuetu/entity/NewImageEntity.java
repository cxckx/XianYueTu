package com.example.cxcxk.xianyuetu.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cxcxk on 2016/7/22.
 */
public class NewImageEntity {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String galleryclass;
    private String id;
    private String img;
    private long time;
    private String title;

    private List<NewImageEntity> entitys = new ArrayList<>();

    public List<NewImageEntity> getEntitys() {
        return entitys;
    }

    public void setEntitys(List<NewImageEntity> entitys) {
        this.entitys = entitys;
    }

    public String getGalleryclass() {
        return galleryclass;
    }

    public void setGalleryclass(String galleryclass) {
        this.galleryclass = galleryclass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = "http://tnfs.tngou.net/image" + img;
    }

    public String getTime() {
        return format.format(new Date(time));
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
