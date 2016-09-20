package com.example.cxcxk.xianyuetu.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.ImageNetDataOperator;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.android_my_library.view.componts.ImageLauncher;
import com.example.cxcxk.xianyuetu.AppApplication;
import com.example.cxcxk.xianyuetu.R;
import com.example.cxcxk.xianyuetu.service.DownLoadService;
import com.example.cxcxk.xianyuetu.utils.SelectReceiver;
import com.example.cxcxk.xianyuetu.utils.SelectSender;
import com.facebook.drawee.drawable.ScalingUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxcxk on 2016/7/28.
 */
public class ImageDetailFragment extends Fragment implements SelectReceiver {


    private CheckBox mCheckBox;
    private ImageLauncher imageLauncher;
    private ImageView imageView;
    boolean isShow = false;
    View view;
    boolean isDownload = false;
    ProgressBar progressBar;
    private ImageNetDataOperator operator = new ImageNetDataOperator();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.download_image_layout,container,false);

        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
        changeStateAndLayout();
        progressBar = (ProgressBar) view.findViewById(R.id.download_progress);

        imageView = (ImageView) view.findViewById(R.id.refresh);
        //imageView.setVisibility(View.VISIBLE);
        imageView.setColorFilter(getResources().getColor(R.color.colorAccent));

        startAnimator();
        imageLauncher = (ImageLauncher) view.findViewById(R.id.image_launcher);
        imageLauncher.setLoadProgressListener(new ImageLauncher.LoadProgressListener() {
            @Override
            public void onFinal() {
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onCache() {
                imageView.setVisibility(View.GONE);
            }
        });
        imageLauncher.load(getArguments().getString("url"));
        imageLauncher.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (isDownload) progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageLauncher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SelectSender.getInstance().notityShow(true);
                return true;
            }
        });
        imageLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectSender.getInstance().notityShow(false);
            }
        });

    }

    private void changeStateAndLayout(){
        if (isShow){
            mCheckBox.setVisibility(View.VISIBLE);

            view.setPadding(AndroidUtils.dip2px(AppApplication.applicationContext,16),AndroidUtils.dip2px(AppApplication.applicationContext,16),AndroidUtils.dip2px(AppApplication.applicationContext,16),0);

        }else {
            mCheckBox.setVisibility(View.INVISIBLE);
            mCheckBox.setChecked(false);
            view.setPadding(0,0,0,0);

        }
    }

    private void startAnimator(){

        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"rotation",0,360);
        animator.setDuration(500);

        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(Animation.RESTART);
        animator.start();
    }

    public boolean isSelect(){
        return mCheckBox.isChecked();
    }

    @Override
    public void notifyShow(boolean isShow) {
        this.isShow = isShow;
        if(mCheckBox != null){

            changeStateAndLayout();
        }
       // mCheckBox.setVisibility(View.VISIBLE);


    }
    Intent intent;
    @Override
    public void notifyDownload() {
          //下载
        if(mCheckBox == null) return ;
        if(!mCheckBox.isChecked()) return;
        progressBar.setVisibility(View.VISIBLE);

        NetDataOperater.Attribute attribute = new NetDataOperater.Attribute();
        attribute.setUrl(getArguments().getString("url"));
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", getArguments().getString("id"));
        attribute.setParams(map);

        operator.request(attribute,getArguments().getString("id"));

        operator.setNetWorkListener(new NetDataOperater.INetWork<Boolean>() {
            @Override
            public void OnCompleted(Boolean aBoolean) {
                SelectSender.getInstance().notityDownloadFinish(getArguments().getString("id"));
            }

            @Override
            public void OnError(String s) {
                Log.i("TAGGG", s);
            }

            @Override
            public void OnProgress(final int i) {
                SelectSender.getInstance().notityDownloadProgress(i, getArguments().getString("id"));
            }
        });

        /*intent = new Intent(getActivity(), DownLoadService.class);
        intent.putExtra("url",getArguments().getString("url"));
        intent.putExtra("id",getArguments().getString("id"));
        getActivity().startService(intent);*/
    }

    @Override
    public void notifyDownloadFinish(String id) {
      if(id.equals(getArguments().getString("id"))){
          progressBar.setVisibility(View.GONE);
          isDownload = false;
          SelectSender.getInstance().notityShowWhich(false,id);

      }
    }

    @Override
    public void notifyDownloadProgress(final int progress, String id) {
        if(id.equals(getArguments().getString("id"))){
            progressBar.setProgress(progress);
            isDownload = true;

        }
    }

    @Override
    public void notifyColse() {
       // getActivity().stopService(intent);
    }

    @Override
    public void notifyShowWhich(boolean isShow, String id) {
          if(id.equals(getArguments().getString("id"))){
              this.isShow = isShow;
              if(mCheckBox != null){

                  changeStateAndLayout();
              }
          }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
