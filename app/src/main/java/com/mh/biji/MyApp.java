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

    public String getConfigName() {
        return configName;
    }

    private final String configName = "config.prop"; //数据库名

    @Override
    public void onCreate(){
        super.onCreate();
        //初始化
        //bjPath = Environment.getExternalStorageDirectory().getPath();
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {     //如果SD卡存在，则获取跟目
            bjPath = Environment.getExternalStorageDirectory().getPath();
        } else {
            bjPath = getApplicationContext().getPackageResourcePath() + "/files";
        }
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
