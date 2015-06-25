package com.mh.biji;

import android.app.Application;
import android.graphics.Color;
import android.os.Environment;

/**
 * Created by MH on 2015-06-24.
 */
public class MyApp extends Application {
    private String bjPath; //路径
    private final String bjFolder = "bijiben"; //目录
    private final String bjDBName = "index.db"; //数据库名

    private int lineWidth = 2;
    private int LineColor = Color.parseColor("#000000");;
    private int bgColor = Color.WHITE;

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineColor() {
        return LineColor;
    }

    public void setLineColor(int lineColor) {
        LineColor = lineColor;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        //初始化
        bjPath = Environment.getExternalStorageDirectory().getPath();
    }

    public String getBjPath() {
        return bjPath;
    }

    public void setBjPath(String P) {
        bjPath = P;
    }

    public String getBjDBName() {
        return bjDBName;
    }

    public String getBjFolder() {
        return bjFolder;
    }
}
