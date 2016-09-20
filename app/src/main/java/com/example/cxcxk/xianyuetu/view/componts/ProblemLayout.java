package com.example.cxcxk.xianyuetu.view.componts;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.android_my_library.utils.LayoutHelper;

/**
 * Created by cxcxk on 2016/8/2.
 */
public class ProblemLayout extends RelativeLayout {
    LinearLayout layout;
    TextView textView;
    TextView textViewImage;

    public ProblemLayout(Context context) {
        super(context);
        init(context);
    }

    public ProblemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProblemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        textViewImage = new TextView(context);
        textViewImage.setTextColor(Color.RED);
        textViewImage.setText("!");
        textViewImage.setTextSize(AndroidUtils.dip2px(context, 20));

        textView = new TextView(context);
        textView.setTextColor(Color.parseColor("#e0e0e0"));

        layout.addView(textViewImage, LayoutHelper.createLinearLayout(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, Gravity.CENTER, 0, 0, 0, 0));
        layout.addView(textView, LayoutHelper.createLinearLayout(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, AndroidUtils.dip2px(context, 10), 0, 0));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        addView(layout, params);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }
}
