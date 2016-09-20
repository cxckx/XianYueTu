package com.example.cxcxk.xianyuetu.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cxcxk.android_my_library.utils.ImageDownLoadUtils;
import com.example.cxcxk.android_my_library.utils.ImageNetDataOperator;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;
import com.example.cxcxk.android_my_library.view.adapter.FragmentViewPagerAdapter;
import com.example.cxcxk.android_my_library.view.cell.TextBigCell;
import com.example.cxcxk.android_my_library.view.componts.ImageLauncher;
import com.example.cxcxk.xianyuetu.R;
import com.example.cxcxk.xianyuetu.entity.NewImageEntity;
import com.example.cxcxk.xianyuetu.utils.JsonNetDataOperator;
import com.example.cxcxk.xianyuetu.utils.SelectReceiver;
import com.example.cxcxk.xianyuetu.utils.SelectSender;
import com.example.cxcxk.xianyuetu.view.adapter.ImageListAdapter;
import com.example.cxcxk.xianyuetu.view.fragment.ImageDetailFragment;
import com.facebook.drawee.drawable.ScalingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.common.util.StringUtils;

/**
 * Created by cxcxk on 2016/7/27.
 */
public class ImageDetailActivity extends BaseActivity implements SelectReceiver {

    NetDataOperater<String> dataOperater = new JsonNetDataOperator();
    ViewPager pager;
    TextView timeView;
    TextBigCell titleView;
    NewImageEntity entity;
    FragmentViewPagerAdapter adapter;
    List<Fragment> fragments = new ArrayList<>();
    boolean isDownload = false;
    boolean isShow = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_detail_layout);
        final int id = getIntent().getExtras().getInt("id");
        final ActionBar actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setBackGroundColor(getResources().getColor(com.example.cxcxk.android_my_library.R.color.colorPrimary));
        actionBar.setTitle("闲阅图");
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.addMenu(0, R.drawable.ic_file_download_white_24dp);
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                } else if (i == 0) {
                    if (isShow) {
                        SelectSender.getInstance().notityDownload();
                    } else {
                        Toast.makeText(ImageDetailActivity.this, "请长按图片选择", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        pager = (ViewPager) findViewById(R.id.pager_detail);
        timeView = (TextView) findViewById(R.id.time);
        titleView = (TextBigCell) findViewById(R.id.title);

        try {
            SelectSender.getInstance().registReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NetDataOperater.Attribute attribute = new NetDataOperater.Attribute();
        attribute.setUrl("http://www.tngou.net/tnfs/api/show");
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");

        attribute.setParams(map);
        dataOperater.request(attribute, "1");
        dataOperater.setNetWorkListener(new NetDataOperater.INetWork<String>() {
            @Override
            public void OnCompleted(String s) {

                JSONObject object = JSON.parseObject(s);
                int count = object.getIntValue("size");
                JSONArray array = object.getJSONArray("list");
                entity = new NewImageEntity();
                entity.setTime(object.getLong("time"));
                entity.setTitle(object.getString("title"));

                for (int i = 0; i < array.size(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    NewImageEntity entity1 = new NewImageEntity();

                    entity1.setId(object1.getString("id"));
                    entity1.setGalleryclass(object1.getString("gallery"));
                    entity1.setImg(object1.getString("src"));

                    entity.getEntitys().add(entity1);
                    ImageDetailFragment fragment = new ImageDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", entity1.getImg());
                    bundle.putString("id", entity1.getId());
                    fragment.setArguments(bundle);
                    try {
                        SelectSender.getInstance().registReceiver(fragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fragments.add(fragment);
                }

                if (adapter == null) {
                    adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
                }

                pager.setAdapter(adapter);
                timeView.setText(entity.getTime());
                titleView.setText(entity.getTitle(), false);
                actionBar.setSubTitle("(" + 1 + "/" + pager.getAdapter().getCount() + ")");
            }

            @Override
            public void OnError(String s) {
                Log.i("TAGG", s);
            }

            @Override
            public void OnProgress(int i) {

            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSubTitle("(" + (position + 1) + "/" + pager.getAdapter().getCount() + ")");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pager.setOffscreenPageLimit(100);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isDownload){
                final boolean[] isExit = {false};
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("还有图片正在下载，是否退出?")
                        .setTitle("提示")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isExit[0] = true;
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();

                return
            }
        }*/
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dataOperater.cancleAllRequest();
        SelectSender.getInstance().notityClose();
        SelectSender.getInstance().unRegistAll();
    }

    @Override
    public void notifyShow(boolean isShow) {
        this.isShow = isShow;
    }

    @Override
    public void notifyDownload() {
        //不做处理
    }

    @Override
    public void notifyDownloadFinish(String id) {
        //不做处理

        Toast.makeText(this, id + "下载成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyDownloadProgress(int progress, String id) {

    }

    @Override
    public void notifyColse() {
        //不作处理
    }

    @Override
    public void notifyShowWhich(boolean isShow, String id) {

    }

}
