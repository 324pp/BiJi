package com.mh.biji;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity {

    private SQLiteDatabase sql;
    private int mIndex;
    private String mdata[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApp app = (MyApp)getApplication();

        String path = app.getBjPath() + "/" + app.getBjFolder();
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
                createIndexTable();
            }catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                showErrorDialog();
                return;
            }
        } else {
            sql = SQLiteDatabase.openOrCreateDatabase(f, null);
        }

        mIndex = 0;
        inital();
    }

    private void inital() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
        String s = "select id, name, createdate, tit from indexTable where status=1 order by id desc;";
        final Cursor cur = sql.rawQuery(s, null);
        if (cur != null && cur.getCount() > 0) {
            mdata = new String[cur.getCount()][2];
            int i = 0;
            while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
                int a = cur.getInt(0);
                String b = cur.getString(1);
                String c = cur.getString(2);
                String d = cur.getString(3);
                HashMap<String,Object> item = new HashMap<String,Object>();
                item.put("name", b);
                item.put("date", c);
                listItem.add(item);
                mIndex = Math.max(a, mIndex) ;
                mdata[i][0] = d;
                mdata[i][1] = b;
                i++;
            }
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.list_item, //每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[] {"name", "date"},
                new int[] {R.id.name,R.id.date}
        );

        final ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(mSimpleAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(TitleList.this,"您选择了标题：" + mListTitle[position] + "内容："+mListStr[position], Toast.LENGTH_LONG).show();
                //cur.moveToPosition(position + 1);
                String b = mdata[position][0];
                openBiJi(b);
            }
        });

        lv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "删除");
                menu.add(0, 1, 0, "重命名");
                /*menu.add(0, 2, 0, "对比");*/
            }
        });

        lv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*int position = lv.getSelectedItemPosition();
                cur.moveToPosition(position + 1);
                int a = cur.getInt(0);
                deleteItem(a);
                inital();*/
                lv.showContextMenu();
                return true;
            }
        });
    }

    // 长按菜单响应函数
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
        int position = info.position;
        String a = mdata[position][0];

        switch (item.getItemId()) {
            case 0:
                // 添加操作
                deleteItem(a);
                inital();
                break;
            case 1:
                renameItem(position);
                inital();
                break;
            case 2:
                // 删除ALL操作
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void createIndexTable() {
        String s = "create table indexTable(id integer primary key AUTOINCREMENT, name varchar(100), tit varchar(50), createdate timestamp default (datetime('now', 'localtime')), status integer default 1);";
        sql.execSQL(s);
    }

    private void insertItem(String id) {
        final String tmp = id;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.new_item, null);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请输入笔记名");
        builder.setView(textEntryView);
        EditText name0 = (EditText) textEntryView.findViewById(R.id.newitem);
        int n = mIndex + 1;
        name0.setText("笔记 " + n);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText name = (EditText) textEntryView.findViewById(R.id.newitem);

                String s = "insert into indexTable(name, tit) values(?,?);";
                sql.execSQL(s, new Object[]{name.getText(), tmp});
                inital();
                openBiJi(tmp);
                //((Activity)textEntryView.getContext()).finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.create().show();
        //AlertDialog d = builder.show();
        //d.dismiss();
    }

    private void deleteItem(String id) {
        String s = "update indexTable set status=0 where tit=?;";
        sql.execSQL(s, new Object[]{id});
    }

    private void renameItem(int p) {
        final String tmp = mdata[p][0];

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.new_item, null);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请输入笔记名");
        builder.setView(textEntryView);
        EditText name0 = (EditText) textEntryView.findViewById(R.id.newitem);
        name0.setText(mdata[p][1]);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText name = (EditText) textEntryView.findViewById(R.id.newitem);

                String s = "update indexTable set name=? where tit=?;";
                sql.execSQL(s, new Object[]{name.getText(), tmp});
                inital();
                //((Activity)textEntryView.getContext()).finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.create().show();
    }

    public void openBiJi(String id) {
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("ID", id);
        //intent.putExtra("ID", "test");
        startActivity(intent);
    }

    public void newBiJi() {
        String id = Fn.getGuid();
        insertItem(id);
        //openBiJi(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            newBiJi();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
