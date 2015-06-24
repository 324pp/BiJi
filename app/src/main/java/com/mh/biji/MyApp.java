package com.mh.biji;

import android.app.Application;
import android.os.Environment;

/**
 * Created by MH on 2015-06-24.
 */
public class MyApp extends Application {
    private String bjPath; //路径
    private final String bjFolder = "bijiben"; //目录
    private final String bjDBName = "index.db"; //数据库名

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
