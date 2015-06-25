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
import android.widget.Spinner;
import android.widget.Toast;

public class ViewConfig extends PopupWindow {

    private View conentView;

    public int lineWidth;
    public int lineColor;
    public int bgColor;

    public ViewConfig(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.view_popup, null);

        //int h = context.getWindowManager().getDefaultDisplay().getHeight();
        //int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(250);
        // 设置SelectPicPopupWindow弹出窗体的高
        //this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setHeight(500);
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

        Spinner mSpinner = (Spinner)conentView.findViewById(R.id.spinner1);
        //  建立Adapter绑定数据源
        ColorAdapter _MyAdapter=new ColorAdapter(conentView.getContext());
        //绑定Adapter
        mSpinner.setAdapter(_MyAdapter);
        mSpinner.setSelection(3);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                int position, long id) {
                int str = (int)parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
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
