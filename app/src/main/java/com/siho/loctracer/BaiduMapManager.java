package com.siho.loctracer;

import android.content.Context;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * 该类主要用于对百度地图状态进行管理，例如设置定位模式，设置开启GPS
 * 设置定位模式，添加点线面图层，坐标转换等
 */
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


    /**
     * GPS经纬度转百度经纬度
     * @param latLng
     * @return
     */
    private LatLng convertTheLatlng(LatLng latLng){
        CoordinateConverter converter=new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(latLng);
        LatLng desLatLng=converter.convert();
        return desLatLng;
    }

}
