package com.siho.loctracer;

import android.support.annotation.NonNull;

import com.baidu.location.BDLocation;

/**
 * 自定义百度地图定位公共接口类
 * Created by hesh on 2017/9/5.
 */

public interface BaiduMapView {

    /**
     * 实现类中需要声明的对象
     */
//    MapView mapView;
//    BaiduMap baiduMap;
//    LocationClient locationClient;
//    LocationClientOption locationClientOption;
//    BDLocationListener mBDLocationListener;

    /**
     * 初始化百度地图
     * 包括注册sdk、获取baiudMap、locationClient
     * 注册定位监听，允许定位等
     */
    void initBaiduMap();

    /**
     * 检查APP权限,适用于targetAPI>=23
     */
    void checkPermission();

    /**
     * 权限检查结果
     */
    void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    /**
     * 初始化定位配置
     * 包括定位坐标、类型，扫描间隔等
     */
    void initLocOption();

    /**
     * 开始请求定位
     */
    void startLoc();

    /**
     * 停止定位
     */
    void stopLoc();

    /**
     * 定位到具体位置
     */
    void navigateTo(BDLocation bdLocation);

    /**
     * 销毁百度地图相关
     * 包括销毁mapview，停止定位，注销监听等
     */
    void destroyBaiduMap();
}
