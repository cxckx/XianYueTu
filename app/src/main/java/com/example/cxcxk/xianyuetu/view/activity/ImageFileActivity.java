package com.example.cxcxk.xianyuetu.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.LayoutHelper;
import com.example.cxcxk.android_my_library.view.BaseActivity;
import com.example.cxcxk.android_my_library.view.actionbar.ActionBar;
import com.example.cxcxk.android_my_library.view.componts.ImageLauncher;
import com.example.cxcxk.xianyuetu.R;
import com.example.cxcxk.xianyuetu.view.componts.ProblemLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by cxcxk on 2016/8/2.
 */
public class ImageFileActivity extends BaseActivity {

    RecyclerView gridView;
    File[] files;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final File file = new File(AndroidUtils.createFilePath());
        files = file.listFiles();
        if (files.length > 0) {
            setContentView(R.layout.file_layout);
            actionBar = (ActionBar) findViewById(R.id.action_bar);


            gridView = (RecyclerView) findViewById(R.id.grid_image_view);
            gridView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            gridView.setItemAnimator(new DefaultItemAnimator());


            GridViewAdapter adapter = new GridViewAdapter();
            gridView.setAdapter(adapter);

            /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ImageFileActivity.this, ImageFileShowActivity.class);
                    intent.putExtra("path", files[position].getAbsolutePath());
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_zoomin, R.anim.activity_zoomout);
                }
            });*/
        } else {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setFitsSystemWindows(true);
            actionBar = new ActionBar(this);
            layout.addView(actionBar, LayoutHelper.createLinearLayout(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
            ProblemLayout layout1 = new ProblemLayout(this);
            layout1.setText("没有图片");
            layout.addView(layout1, LayoutHelper.createLinearLayout(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
            setContentView(layout);
        }

        actionBar.setTitle("我的图片");
        actionBar.setBackGroundColor(getResources().getColor(R.color.colorPrimary));
        actionBar.setNavigationView(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setOnMenuItemClick(new ActionBar.OnMenuItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    onBackPressed();
                }
            }
        });
    }

    class GridViewAdapter extends RecyclerView.Adapter<MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView view = (CardView) LayoutInflater.from(ImageFileActivity.this)
                    .inflate(R.layout.card_layout, null);

            ImageView imageView = new ImageView(ImageFileActivity.this);
            view.addView(imageView);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    Intent intent = new Intent(ImageFileActivity.this, ImageFileShowActivity.class);
                    intent.putExtra("path", files[index].getAbsolutePath());
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_zoomin, R.anim.activity_zoomout);
                    /*ImageView imageView = new ImageView(ImageFileActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(files[index].getAbsolutePath()));
                    AlertDialog dialog = new AlertDialog.Builder(ImageFileActivity.this)
                            .setView(imageView)
                            .create();
                    dialog.show();*/

                }
            });
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            CardView view = (CardView) holder.itemView;
            ImageView imageView = (ImageView) view.getChildAt(0);
            view.setTag(position);
            File file = files[position];
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(ImageFileActivity.this)
                    .load(file)
                    .into(imageView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

        }

        @Override
        public int getItemCount() {
            return files.length;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
