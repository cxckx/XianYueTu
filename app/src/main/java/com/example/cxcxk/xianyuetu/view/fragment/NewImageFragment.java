package com.example.cxcxk.xianyuetu.view.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cxcxk.android_my_library.utils.NetDataOperater;
import com.example.cxcxk.xianyuetu.R;
import com.example.cxcxk.xianyuetu.entity.NewImageEntity;
import com.example.cxcxk.xianyuetu.utils.ConnectUtils;
import com.example.cxcxk.xianyuetu.utils.JsonNetDataOperator;
import com.example.cxcxk.xianyuetu.utils.NetWorkReceiver;
import com.example.cxcxk.xianyuetu.view.activity.ImageDetailActivity;
import com.example.cxcxk.xianyuetu.view.adapter.ImageListAdapter;
import com.example.cxcxk.xianyuetu.view.componts.AutoSwipeRefreshLayout;
import com.example.cxcxk.xianyuetu.view.componts.LoadMoreRecycleView;
import com.example.cxcxk.xianyuetu.view.componts.OnItemClick;
import com.example.cxcxk.xianyuetu.view.componts.ProblemLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxcxk on 2016/7/22.
 */
public class NewImageFragment extends Fragment implements NetWorkReceiver{

    LoadMoreRecycleView listView;
    AutoSwipeRefreshLayout refreshLayout;
    public int page = 0;
    int rows = 10;
    ImageListAdapter adapter;
    public static final int NEW = 0;
    public static final int LIST = 1;
    public static final int LIST_POP = 2;
    private int type = NEW;
    ProblemLayout problemLayout;
    ImageView refreshImage;
    NetDataOperater<String> dataOperater = new JsonNetDataOperator();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(type == NEW){
            page = 0;
        }else if (type == LIST || type == LIST_POP){
            page = 1;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.new_image_f_layout,container,false);
         listView = (LoadMoreRecycleView) view.findViewById(R.id.recycle_view);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setItemAnimator(new DefaultItemAnimator());
        refreshLayout = (AutoSwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light
                , android.R.color.holo_orange_light, android.R.color.holo_green_light);
        problemLayout = (ProblemLayout) view.findViewById(R.id.problem_layout);
        refreshImage = (ImageView) view.findViewById(R.id.refresh_image);
        refreshImage.setColorFilter(getResources().getColor(android.R.color.holo_blue_bright));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(ConnectUtils.isConnect()){
            if(mEntities.size() == 0) {
                refreshLayout.autoRefresh();
            }else {
                adapter = new ImageListAdapter(getActivity(),mEntities);
                listView.setAdapter(adapter);
            }
            listView.setAutoLoadMoreEnable(true);
        }else {
            problemLayout.setText("网络不可用");
            problemLayout.setVisibility(View.VISIBLE);
        }

        //getEntity();

    }

    private void startAnimator(){

        ObjectAnimator animator = ObjectAnimator.ofFloat(refreshImage,"rotation",0,360);
        animator.setDuration(500);

        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(Animation.RESTART);
        animator.start();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        problemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectUtils.isConnect()) {
                    problemLayout.setVisibility(View.GONE);
                    refreshImage.setVisibility(View.VISIBLE);
                    startAnimator();
                    refreshLayout.autoRefresh();
                }

            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getEntity();
                if (type == LIST || type == LIST_POP) {
                    page++;
                }

            }
        });
        listView.setLoadMoreListener(new LoadMoreRecycleView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                //refreshLayout.setRefreshing(false);

                getEntity();
                if (type == LIST || type == LIST_POP) {
                    page++;
                }

            }
        });
        listView.setListener(new OnItemClick() {
            @Override
            public void onItemClick(int position) {
                NewImageEntity entity = mEntities.get(position);
                String id = entity.getId();
                Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", Integer.parseInt(id));
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_zoomin, R.anim.activity_zoomout);
            }
        });
    }
    public  List<NewImageEntity> mEntities = new ArrayList<>();



    public void getEntity(){


        NetDataOperater.Attribute attribute = new NetDataOperater.Attribute();
        attribute.setUrl(getArguments().getString("url"));
        Map<String,String> map = new HashMap<>();
        if(type == NEW){
            map.put("id", page + "");
        }else {
            map.put("page", page + "");
        }

        map.put("rows", rows + "");
        if(getArguments().getString("id") != null) map.put("id",getArguments().getString("id"));
        attribute.setParams(map);
        dataOperater.request(attribute, "1");
        dataOperater.setNetWorkListener(new NetDataOperater.INetWork<String>() {
            @Override
            public void OnCompleted(String s) {

                JSONObject object = JSON.parseObject(s);
                int count = object.getIntValue("total");
                listView.setLoadingMore(false);

                if (type == NEW) {
                    if (page + rows >= count) {
                        listView.notifyMoreFinish(false);
                    }
                } else if (type == LIST || type == LIST_POP) {
                    if (page * rows >= count) {
                        listView.notifyMoreFinish(false);
                    }
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager) listView.getLayoutManager();
                listView.scrollToPosition(layoutManager.findLastCompletelyVisibleItemPosition());
                JSONArray array = object.getJSONArray("tngou");
                List<NewImageEntity> entities = new ArrayList<NewImageEntity>();
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    NewImageEntity entity = new NewImageEntity();
                    entity.setId(object1.getString("id"));
                    entity.setGalleryclass(object1.getString("galleryclass"));
                    entity.setImg(object1.getString("img"));
                    entity.setTime(object1.getLong("time"));
                    entity.setTitle(object1.getString("title"));
                    entities.add(entity);
                }
                if (type == NEW) {
                    page = Integer.parseInt(entities.get(entities.size() - 1).getId());
                }
                if (adapter == null) {
                    adapter = new ImageListAdapter(getActivity(), entities);
                } else if (type == LIST_POP) {
                    adapter.clearData();
                    adapter.bindData(entities);
                    type = LIST;
                } else {
                    adapter.bindData(entities);

                }
                mEntities.addAll(entities);
                listView.setAdapter(adapter);
                refreshImage.setVisibility(View.GONE);
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void OnError(String s) {
                Log.i("TAGG", s);
                if (getRefresh()) {
                    refreshImage.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    problemLayout.setText("没有结果,点击重新加载");
                    problemLayout.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void OnProgress(int i) {
                listView.setLoadingMore(true);
            }
        });


    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public void notifyRefresh() {
        if(refreshLayout != null)
             refreshLayout.autoRefresh();
    }

    @Override
    public void notifyNoNetWork() {
        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
            problemLayout.setText("网络不可用");
            problemLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void notifyCancleRefresh(int index, boolean isBack) {
        if(index == getArguments().getInt("index")){

            Log.i("TAGG",isBack+" ");
            if(isBack){
                refreshLayout.setRefreshing(false);
                refreshImage.setVisibility(View.GONE);
                problemLayout.setText("点击重新加载");
                problemLayout.setVisibility(View.VISIBLE);
                dataOperater.cancleAllRequest();
           }
        }


    }

    public boolean getRefresh(){
        int visible = refreshImage.getVisibility();
        boolean refresh = visible == View.VISIBLE || refreshLayout.isRefreshing();
        return refresh;
    }
}
