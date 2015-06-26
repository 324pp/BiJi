package com.mh.biji;

/**
 * Created by MH on 2015-06-25.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class ViewConfig extends PopupWindow {

    private View conentView;
    private ViewActivity VA;

    public ViewConfig(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.view_popup, null);

        VA = (ViewActivity)context;

        //int h = context.getWindowManager().getDefaultDisplay().getHeight();
        //int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(300);
        // 设置SelectPicPopupWindow弹出窗体的高
        //this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setHeight(150);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimationPreview);

        onInital();
    }

    public void onInital() {
        Spinner bgcolor = (Spinner)conentView.findViewById(R.id.bgcolor);
        Spinner linecolor = (Spinner)conentView.findViewById(R.id.linecolor);
        SeekBar linewidth = (SeekBar)conentView.findViewById(R.id.linewidth);
        //  建立Adapter绑定数据源
        ColorAdapter _MyAdapter=new ColorAdapter(conentView.getContext());
        //绑定Adapter
        bgcolor.setAdapter(_MyAdapter);
        int bgIndex = (new ColorItem()).indexOf(VA.getBgColor());
        if (bgIndex < 0) bgIndex = 0;
        bgcolor.setSelection(bgIndex);
        bgcolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                int position, long id) {
                int s = (int)parent.getItemAtPosition(position);
                VA.setBgColor(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        linecolor.setAdapter(_MyAdapter);
        int lineIndex = (new ColorItem()).indexOf(VA.getLineColor());
        if (lineIndex < 0) lineIndex = 0;
        linecolor.setSelection(lineIndex);
        linecolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int s = (int)parent.getItemAtPosition(position);
                VA.setLineColor(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        linewidth.setProgress(VA.getLineWidth());
        linewidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                VA.setLineWidth(progress);
            }
        });
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            //this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    public void onButtonMethod(View v) {
        //ViewConfig morePopWindow = new ViewConfig(ViewConfig.this);
        //morePopWindow.showPopupWindow(v);
    }

}
