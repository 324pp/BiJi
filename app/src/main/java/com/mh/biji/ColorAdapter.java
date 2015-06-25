package com.mh.biji;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by MH on 2015-06-25.
 */
public class ColorAdapter extends BaseAdapter {
    private int mList[];
    private Context mContext;

    public ColorAdapter(Context pContext) {
        this.mContext = pContext;
        this.mList = new ColorItem().getList();
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int position) {
        return mList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.color_item, null);
        if(convertView!=null)
        {
            ImageView IV=(ImageView)convertView.findViewById(R.id.colorItem);
            IV.setBackgroundColor(mList[position]);
        }
        return convertView;
    }

}
