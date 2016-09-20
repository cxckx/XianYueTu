package com.example.cxcxk.xianyuetu.view.activity;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.LayoutHelper;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;
import com.example.cxcxk.xianyuetu.R;

/**
 * Created by cxcxk on 2016/8/2.
 */
public class ImageFileShowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setFitsSystemWindows(true);

        ActionBar actionBar = new ActionBar(this);
        actionBar.setTitle("我的图片");
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                }
            }
        });
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        layout.addView(actionBar, LayoutHelper.createLinearLayout(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        ImageView imageView = new ImageView(this);
        String path = getIntent().getStringExtra("path");
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        layout.addView(imageView, LayoutHelper.createLinearLayout(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, 0, Gravity.CENTER, 0,
                0, 0, 0));
        setContentView(layout);


    }


}
