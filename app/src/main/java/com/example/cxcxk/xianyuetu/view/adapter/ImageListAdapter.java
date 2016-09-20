package com.example.cxcxk.xianyuetu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cxcxk.android_my_library.view.cell.TextBigCell;
import com.example.cxcxk.android_my_library.view.componts.ImageLauncher;
import com.example.cxcxk.xianyuetu.R;
import com.example.cxcxk.xianyuetu.entity.NewImageEntity;
import com.example.cxcxk.xianyuetu.view.componts.OnItemClick;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cxcxk on 2016/7/22.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    Context mContext;
    List<NewImageEntity> entities = new ArrayList<>();

    public ImageListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ImageListAdapter(Context mContext, List<NewImageEntity> entities) {
        this.mContext = mContext;
        this.entities = entities;
    }

    public void clearData() {
        this.entities.clear();
        notifyDataSetChanged();
    }

    public void bindData(List<NewImageEntity> entities) {

        this.entities.addAll(entities);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.image_item_layout, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewImageEntity entity = entities.get(position);
        holder.cell.setText(entity.getTitle(), false);
        //Fresco.getImagePipeline().evictFromCache(Uri.parse(entity.getImg()));
        holder.view.load(entity.getImg());

        holder.time.setText(entity.getTime());
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageLauncher view;
        TextBigCell cell;
        TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = (ImageLauncher) itemView.findViewById(R.id.image_launcher);
            view.initParam(R.drawable.ic_help_black_24dp, false);
            view.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            cell = (TextBigCell) itemView.findViewById(R.id.big_text);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}


