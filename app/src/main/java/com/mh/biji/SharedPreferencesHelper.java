package com.mh.biji;

/**
 * Created by MH on 2015-06-29.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Context context;

    public SharedPreferencesHelper(Context c,String name){
        context = c;
        sp = context.getSharedPreferences(name, 0);
        editor = sp.edit();
    }

    public void putValue(String key, String value){
        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key){
        return sp.getString(key, null);
    }

    public int getInt(String key, int d){
        return sp.getInt(key, d);
    }

    public int getInt(String key){
        return sp.getInt(key, 0);
    }

    public void setInt(String key, int d){
        editor.putInt(key, d);
        editor.commit();
    }
}