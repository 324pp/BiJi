package com.mh.biji;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class ViewActivity extends Activity {
    private String bjId;
    private String bjPath;
    private int bjPage = 1;

    private SQLiteDatabase sql;

    private AreaView AV;

    private int curColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        AV = (AreaView)findViewById(R.id.AV);
        AV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean  onTouch(View v, MotionEvent event) {
                huaDot(event);
                return true;
            }
        });

        curColor = Color.parseColor("#000000");

        //初始化数据
        MyApp app = (MyApp)getApplication();
        Intent intent = getIntent();
        bjId = intent.getStringExtra("ID");
        bjPath = intent.getStringExtra("PATH");
        if (bjPath == null || bjPath == "") bjPath = app.getBjPath();
        String path = bjPath + "/" + app.getBjFolder() + "/" + bjId;
        File p = new File(path);
        File f = new File(path + "/" + app.getBjDBName());
        if(!p.exists()) {
            p.mkdirs();
        }
        if(!p.exists()) {
            showErrorDialog();
            return;
        }
        if(!f.exists()) {
            try {
                f.createNewFile();
                sql = SQLiteDatabase.openOrCreateDatabase(f, null);
                createMasterTable();
                createPageTable(1);
            }catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
                showErrorDialog();
                return;
			}
        }

        openOrCreatePage(1);
    }

    //画点
    private void huaDot(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                touchIng(x, y);
                AV.reDraw();
                break;
            case MotionEvent.ACTION_MOVE:
                touchIng(x, y);
                AV.reDraw();
                break;
            case MotionEvent.ACTION_UP:
                touchEnd(x, y);
                AV.reDraw();
                break;
        }

        AV.addDot(x, y, curColor);
    }

    private void touchStart(int x, int y) {

    }

    private void touchIng(int x, int y) {

    }

    private void touchEnd(int x, int y) {

    }

    private void huaDot(int x, int y) {
        AV.addDot(x, y, curColor);
    }

    //打开页
    private void openOrCreatePage(int page) {
        if(!isExistsPage(page)) createPageTable(page);

        String s = "select x, y, c from page" + page + " order by id;";
        Cursor cur = sql.rawQuery(s, null);

        if (cur != null) {
            while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
                int x = cur.getInt(0);
                int y = cur.getInt(1);
                int c = cur.getInt(2);
                AV.toDot(x, y, c);
            }
            AV.reDraw();
        }
    }

    //判断页面存在
    private boolean isExistsPage(int page) {
        String s = "select * from indexTable where id="+page+";";
        Cursor cur = sql.rawQuery(s, null);
        return (cur.getCount() == 0) ? false : true;
    }

    //创建主索引表
    private void createMasterTable() {
        String s = "create table indexTable(id integer primary key, createdate timestamp default (datetime('now', 'localtime')), status integer default 1);";
        sql.execSQL(s);
    }

    //创建页表
    private void createPageTable(int page) {
        String s = "insert into indexTable(id) values(" + page + ");";
        sql.execSQL(s);
        s = "create table page" + page + "(id integer primary key AUTOINCREMENT, createdate timestamp default (datetime('now', 'localtime')), status integer default 1, x integer, y integer, c integer);";
        sql.execSQL(s);
    }

    //创建文件出错
    private void showErrorDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("创建文件出错,请重新设置文件目录");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                closeWindow();
            }
        });
        dialog.create().show();
    }

    //关闭窗口
    private void closeWindow() {
        finish();
    }

}
