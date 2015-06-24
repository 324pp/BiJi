package com.mh.biji;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class ViewActivity extends Activity {
    private String bjId;
    private String bjPath;
    private int bjPage = 1;

    private String path;
    private SQLiteDatabase sql;

    private ImageView IV;
    private Bitmap baseBitmap;
    private Canvas cvs;
    private Paint paint;
    private int curColor;

    private float X;
    private float Y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        curColor = Color.parseColor("#000000");
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(curColor);

        baseBitmap = Bitmap.createBitmap(IV.getWidth(), IV.getHeight(), Bitmap.Config.ARGB_8888);
        cvs = new Canvas(baseBitmap);
        cvs.drawColor(Color.WHITE);

        IV = (ImageView)findViewById(R.id.IV);
        IV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean  onTouch(View v, MotionEvent event) {
                touch(event);
                return true;
            }
        });

        //初始化数据
        MyApp app = (MyApp)getApplication();
        Intent intent = getIntent();
        bjId = intent.getStringExtra("ID");
        bjPath = intent.getStringExtra("PATH");
        if (bjPath == null || bjPath == "") bjPath = app.getBjPath();
        path = bjPath + "/" + app.getBjFolder() + "/" + bjId;
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
    private void touch(MotionEvent event) {
       switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                X = event.getX();
                Y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float X2 = event.getX();
                float Y2 = event.getY();
                cvs.drawLine(X, Y, X2, Y2, paint);
                X = X2;
                Y = Y2;
                IV.setImageBitmap(baseBitmap);
                break;
            case MotionEvent.ACTION_UP:
                break;
           default:
               break;
        }
    }

    //打开页
    private void openOrCreatePage(int page) {
        if(!isExistsPage(page)) createPageTable(page);

        String s = "select x, y, c from page" + page + " order by id;";
        Cursor cur = sql.rawQuery(s, null);


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
        s = "create table page" + page + "(id integer primary key AUTOINCREMENT, createdate timestamp default (datetime('now', 'localtime')), status integer default 1, x NUMERIC, y NUMERIC, c integer);";
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
