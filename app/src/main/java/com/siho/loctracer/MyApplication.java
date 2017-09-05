package com.siho.loctracer;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.xutils.x;

/**
 * Created by hesh on 2017/9/1.
 */

public class MyApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        x.Ext.init(this);//引入xutils
        TypefaceProvider.registerDefaultIconSets();//引入androidBootstrap
        SDKInitializer.initialize(this);
    }

    public static Context getAppContext(){
        return context;
    }
}



