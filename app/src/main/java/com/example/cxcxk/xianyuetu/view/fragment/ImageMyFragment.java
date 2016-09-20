package com.example.cxcxk.xianyuetu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.cxcxk.android_my_library.view.cell.TextBigCell;
import com.example.cxcxk.android_my_library.view.componts.DefaultHeadPortraitView;
import com.example.cxcxk.android_my_library.view.componts.LeadHeadView;
import com.example.cxcxk.xianyuetu.R;
import com.example.cxcxk.xianyuetu.view.activity.ImageFileActivity;
import com.example.cxcxk.xianyuetu.view.componts.PopWindowItemView;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cxcxk on 2016/7/22.
 */
public class ImageMyFragment extends Fragment {

    ListView listView;

    ImageClassifyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listView = new ListView(getContext());
        listView.setDivider(null);
        listView.setDividerHeight(0);

        return listView;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ImageClassifyAdapter();
        listView.setAdapter(adapter);
        update();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Intent intent = new Intent(getActivity(), ImageFileActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_zoomin, R.anim.activity_zoomout);
                }
            }
        });

    }

    private int count;
    private int headRow;
    private int myImageRow;

    private void update() {
        count = 0;
        headRow = count++;
        myImageRow = count++;
        adapter.notifyDataSetChanged();
    }

    class ImageClassifyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return count;
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
        public boolean isEnabled(int position) {
            return position != headRow;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == headRow) {
                return 0;
            } else if (position == myImageRow) {
                return 1;
            }

            return 2;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int viewType = getItemViewType(position);
            if (viewType == 0) {
                if (convertView == null) {
                    convertView = new LeadHeadView(getContext());
                }

                LeadHeadView view = (LeadHeadView) convertView;
                view.setImage(R.drawable.img_head);
            } else if (viewType == 1) {
                if (convertView == null) {
                    convertView = new TextBigCell(getContext());
                }

                TextBigCell cell = (TextBigCell) convertView;
                cell.setText("我的图片", true);
            }

            return convertView;
        }
    }
}
