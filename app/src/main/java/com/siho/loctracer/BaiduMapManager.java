package com.siho.loctracer;

import android.content.Context;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;

/**
 * 百度地图状态管理工具类
 * Created by hesh on 2017/9/5.
 */
public class BaiduMapManager {

    private static final String TAG="BaiduMapManager";

    public static final int SCAN_INTERVAL=2000;//默认扫描时间间隔
    public static final float ZOOM_LEVEL=16.0f;//默认放大level

    BaiduMap baiduMap;
    LocationClientOption locationClientOption;

    private Context context;

//    public BaiduMapManager(){
//    }

    public BaiduMapManager(Context context){
        this.context=context;
    }

    public void setBaiduMap(BaiduMap baiduMap, LocationClientOption locationClientOption){
        this.baiduMap=baiduMap;
        this.locationClientOption=locationClientOption;
    }









}
