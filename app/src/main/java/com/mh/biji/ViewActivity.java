package com.mh.biji;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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

    private MyApp app;

    private ImageView IV;
    private Bitmap baseBitmap;
    private Canvas cvs;
    private Paint linePaint;
    private int lineWidth;
    private int curColor;
    private int bgColor;

    private float X;
    private float Y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //初始化数据
        app = (MyApp)getApplication();
        Intent intent = getIntent();
        bjId = intent.getStringExtra("ID");
        bjPath = intent.getStringExtra("PATH");
        if (bjPath == null || bjPath == "") bjPath = app.getBjPath();
        getParam();

        linePaint = new Paint(Paint.DITHER_FLAG);
        linePaint.setAntiAlias(true);
        linePaint.setColor(curColor);
        linePaint.setDither(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(lineWidth);
        //毛笔浮雕风格
        MaskFilter maskFilter = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
        //铅笔模糊风格
        //MaskFilter maskFilter = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        linePaint.setMaskFilter(maskFilter);

        IV = (ImageView)findViewById(R.id.IV);
        IV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean  onTouch(View v, MotionEvent event) {
                touch(event);
                return true;
            }
        });

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
        } else {
            sql = SQLiteDatabase.openOrCreateDatabase(f, null);
        }

        openOrCreatePage();
    }

    //全局参数
    private void setParam() {
        app.setBgColor(bgColor);
        app.setLineColor(curColor);
        app.setLineWidth(lineWidth);
    }

    private void getParam() {
        bgColor = app.getBgColor();
        curColor = app.getLineColor();
        lineWidth = app.getLineWidth();
    }

    //菜单事件
    public void onButtonMethod(View v) {
        switch (v.getId()) {
            case R.id.home:
                closeWindow();
                break;
            case R.id.config:
                ViewConfig morePopWindow = new ViewConfig(ViewActivity.this);
                morePopWindow.showPopupWindow(v);
                break;
            case R.id.add:
                changePageNum(1);
                openOrCreatePage();
                break;
            case R.id.del:
                changePageNum(-1);
                openOrCreatePage();
                break;
            case R.id.first:
                bjPage = 1;
                openOrCreatePage();
                break;
            case R.id.left:
                if (bjPage > 1) bjPage--;
                openOrCreatePage();
                break;
            case R.id.right:
                bjPage++;
                openOrCreatePage();
                break;
            case R.id.last:
                openOrCreatePage(getMaxPage());
                break;
        }
    }

    //画点
    private void touch(MotionEvent event) {
       switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*if (baseBitmap == null) {
                    baseBitmap = Bitmap.createBitmap(IV.getWidth(), IV.getHeight(), Bitmap.Config.ARGB_8888);
                    cvs = new Canvas(baseBitmap);
                    cvs.drawColor(bgColor);
                }*/
                X = event.getX();
                Y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float X2 = event.getX();
                float Y2 = event.getY();
                cvs.drawLine(X, Y, X2, Y2, linePaint);
                saveLine(X, Y, X2, Y2);
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

    //保持画面到表
    private void saveLine(float x, float y, float x2, float y2) {
        int pagenum = getPageNum(bjPage);
        String s = "insert into page" + pagenum + "(x, y, x2, y2, c) values (?,?,?,?,?);";
        sql.execSQL(s, new Object[]{x, y, x2, y2, curColor});
    }

    private void newPage() {
        if (baseBitmap == null) {
            int srceenW =  this.getWindowManager().getDefaultDisplay().getWidth();
            int screenH = this.getWindowManager().getDefaultDisplay().getHeight();
            baseBitmap = Bitmap.createBitmap(srceenW, screenH - 30, Bitmap.Config.ARGB_8888);
            //baseBitmap = Bitmap.createBitmap(IV.getWidth(), IV.getHeight(), Bitmap.Config.ARGB_8888);
            cvs = new Canvas(baseBitmap);
        } else {
            //cvs.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            Paint p = new Paint();
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            cvs.drawPaint(p);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        }
        cvs.drawColor(bgColor);
        IV.setImageBitmap(baseBitmap);
    }

    //打开页
    private void openOrCreatePage(int page) {
        if(!isExistsPage(page)) {
            createPageTable(page);
        }

        newPage();

        int pagenum = getPageNum(page);
        String s = "select x, y, x2, y2, c from page" + pagenum + " order by id;";
        Cursor cur = sql.rawQuery(s, null);

        if (cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
                float x = cur.getFloat(0);
                float y = cur.getFloat(1);
                float x2 = cur.getFloat(2);
                float y2 = cur.getFloat(3);
                int c = cur.getInt(4);
                linePaint.setColor(c);
                cvs.drawLine(x, y, x2, y2, linePaint);
            }
            IV.setImageBitmap(baseBitmap);
            linePaint.setColor(curColor);
        }
    }

    private void openOrCreatePage() {
        openOrCreatePage(bjPage);
    }

    //改变页码
    private void changePageNum(int p) {
        if (p < 0) {
            String s = "update indexTable set status=0 where id >= ?;";
            sql.execSQL(s, new Object[]{bjPage});
        }

        String s = "update indexTable set id = id + ? where id >= ? and status=1;";
        sql.execSQL(s, new Object[]{p, bjPage});
    }

    //取最大页码
    private  int getMaxPage() {
        String s = "select max(id) from indexTable where status=1;";
        Cursor cur = sql.rawQuery(s, null);
        if (cur != null && cur.getCount() > 0) {
            cur.moveToNext();
            return cur.getInt(0);
        } else {
            return 1;
        }
    }

    //获取页码
    private int getPageNum(int page) {
        String s = "select num from indexTable where id=" + page + " and status=1;";
        Cursor cur = sql.rawQuery(s, null);
        if (cur != null && cur.getCount() > 0) {
            cur.moveToNext();
            return cur.getInt(0);
        } else {
            createPageTable(page);
            return getPageNum(page);
        }
    }

    //判断页面存在
    private boolean isExistsPage(int page) {
        String s = "select * from indexTable where id="+page+" and status=1;";
        Cursor cur = sql.rawQuery(s, null);
        return (cur != null && cur.getCount() > 0) ? true : false;
    }

    //创建主索引表
    private void createMasterTable() {
        String s = "create table indexTable(num integer primary key AUTOINCREMENT,id integer, createdate timestamp default (datetime('now', 'localtime')), status integer default 1);";
        sql.execSQL(s);
    }

    //创建页表
    private void createPageTable(int page) {
        if(isExistsPage(page)) return;

        String s = "insert into indexTable(id) values(" + page + ");";
        sql.execSQL(s);

        int pagenum = getPageNum(page);
        s = "create table page" + pagenum + "(id integer primary key AUTOINCREMENT, createdate timestamp default (datetime('now', 'localtime')), status integer default 1, x NUMERIC, y NUMERIC, x2 NUMERIC, y2 NUMERIC, c integer);";
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
