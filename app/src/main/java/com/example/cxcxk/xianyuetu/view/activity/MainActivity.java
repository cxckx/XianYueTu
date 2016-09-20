package com.example.cxcxk.xianyuetu.view.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.example.cxcxk.android_my_library.utils.LayoutHelper;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.TabLayout.TabLayout;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;
import com.example.cxcxk.android_my_library.view.adapter.FragmentViewPagerAdapter;
import com.example.cxcxk.xianyuetu.R;
import com.example.cxcxk.xianyuetu.utils.NetWorkObserver;
import com.example.cxcxk.xianyuetu.view.componts.PopWindowItemView;
import com.example.cxcxk.xianyuetu.view.fragment.ImageMyFragment;
import com.example.cxcxk.xianyuetu.view.fragment.NewImageFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends BaseActivity {

    TabLayout mTablayout;
    String[] labels = {"最新图库", "图库列表", "我的图库"};
    ViewPager pager;
    private PopupWindow popupWindow;
    NewImageFragment newImagesFragment;
    NewImageFragment newImageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setBackGroundColor(getResources().getColor(com.example.cxcxk.android_my_library.R.color.colorPrimary));
        actionBar.setTitle("闲阅图");
        actionBar.addMenu(1, R.drawable.ic_apps_white_24dp);
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == 1) {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 0.7f;
                    getWindow().setAttributes(params);
                    popupWindow.showAtLocation(mTablayout, Gravity.BOTTOM, 0, 0);
                }
            }
        });
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mTablayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        for (int i = 0; i < labels.length; i++) {
            mTablayout.addTab()
                    .setText(labels[i])
                    .setTextColor(Color.parseColor("#e0e0e0"))
                    .setTextColorSelect(Color.parseColor("#bcaaa4"))
                    .setImage(R.drawable.ic_add_white_24dp).setting();


        }


        pager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fs = new ArrayList<>();
        newImageFragment = new NewImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", 0);
        bundle.putString("url", "http://www.tngou.net/tnfs/api/news");
        newImageFragment.setType(NewImageFragment.NEW);
        newImageFragment.setArguments(bundle);


        newImagesFragment = new NewImageFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("index", 1);
        bundle1.putString("url", "http://www.tngou.net/tnfs/api/list");
        newImagesFragment.setType(NewImageFragment.LIST);
        newImagesFragment.setArguments(bundle1);

        fs.add(newImageFragment);
        fs.add(newImagesFragment);
        fs.add(new ImageMyFragment());
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fs);
        pager.setAdapter(adapter);

        try {
            NetWorkObserver.getInstance().registReceiver(newImagesFragment);
            NetWorkObserver.getInstance().registReceiver(newImageFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pager.setOffscreenPageLimit(3);
        mTablayout.bindViewPager(pager);
        mTablayout.setSelect(new TabLayout.OnTabSelect() {
            @Override
            public void onTabSelect(int i) {
                if (i == 2) {
                    actionBar.setTitle("");

                } else {
                    actionBar.setTitle("闲阅图");
                }
            }
        });

        initPopuptWindow();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean isBack = false;
            if (pager.getCurrentItem() == 0) {
                isBack = newImageFragment.getRefresh();
            } else if (pager.getCurrentItem() == 1) {
                isBack = newImagesFragment.getRefresh();
            }
            if (isBack) {
                NetWorkObserver.getInstance().notifyCancleRefresh(pager.getCurrentItem(), isBack);
                return isBack;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            NetWorkObserver.getInstance().unRegistReceiver(newImageFragment);
            NetWorkObserver.getInstance().unRegistReceiver(newImagesFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow() {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        final View popupWindow_view = getLayoutInflater().inflate(R.layout.popwindow_layout, null,
                false);
        initMap();
        GridView gridView = (GridView) popupWindow_view.findViewById(R.id.grid_view);
        gridView.setAdapter(new Pop_GridViewAdapter());
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, true);


        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 点击其他地方消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        newImagesFragment.getArguments().putString("id", (position + 1) + "");

                        break;
                    case 1:
                        newImagesFragment.getArguments().putString("id", (position + 1) + "");
                        break;
                    case 2:
                        newImagesFragment.getArguments().putString("id", (position + 1) + "");
                        break;
                    case 3:
                        newImagesFragment.getArguments().putString("id", (position + 1) + "");

                        break;
                    case 4:
                        newImagesFragment.getArguments().putString("id", (position + 1) + "");

                        break;
                    case 5:
                        newImagesFragment.getArguments().putString("id", (position + 1) + "");

                        break;
                    case 6:
                        newImagesFragment.getArguments().putString("id", (position + 1) + "");

                        break;
                    case 7:
                        newImagesFragment.getArguments().putString("id", null);

                        break;
                }
                newImagesFragment.page = 1;
                newImagesFragment.mEntities.clear();
                newImagesFragment.setType(NewImageFragment.LIST_POP);
                newImagesFragment.getEntity();
                pager.setCurrentItem(1);
                popupWindow.dismiss();
            }
        });
    }

    class Pop_GridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new PopWindowItemView(MainActivity.this);
            }
            PopWindowItemView view = (PopWindowItemView) convertView;

            view.setText(map.get((position + 1) + ""));
            if (position % 2 == 0) {
                view.setColor(getResources().getColor(android.R.color.holo_blue_bright));
            } else {
                view.setColor(getResources().getColor(android.R.color.holo_green_light));
            }

            return convertView;
        }
    }

    Map<String, String> map = new TreeMap<>();

    private void initMap() {
        map.put("1", "性感美女");
        map.put("2", "韩日美女");
        map.put("3", "丝袜美腿");
        map.put("4", "美女照片");
        map.put("5", "美女写真");
        map.put("6", "清纯美女");
        map.put("7", "性感车模");
        map.put("8", "全部");
    }
}
