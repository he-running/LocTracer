package com.siho.loctracer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;

/**
 * Created by hesh on 2017/9/5.
 */

public class BaiduMapActivity extends AppCompatActivity implements BaiduMapView {

    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private LocationClientOption clientOption;
    private MyBDLocationListener mBDBdLocationListener;

    private ArrayList<String> permissionList=new ArrayList<String>();
    private boolean isFirstLoc=true;
    private long lastBackTime=0L;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        SDKInitializer.initialize(getApplicationContext());
        initBaiduMap();
        initLocOption();
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyBaiduMap();
    }

    @Override
    public void initBaiduMap() {
        mapView= (MapView) findViewById(R.id.id_mapView);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationClient=new LocationClient(getApplicationContext());
        mBDBdLocationListener=new MyBDLocationListener();
        locationClient.registerLocationListener(mBDBdLocationListener);
    }

    @Override
    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }else if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }else if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionList.size()>0){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else {
            startLoc();//权限通过，开始定位
        }
    }

    @Override
    public void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1&&grantResults.length>0){
            for (int result:grantResults){
                if (result!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"权限不通过",Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            startLoc();//权限通过，开始定位
        }
    }

    @Override
    public void initLocOption() {
        clientOption=new LocationClientOption();
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setScanSpan(BaiduMapManager.SCAN_INTERVAL);
        clientOption.setNeedDeviceDirect(true);
        clientOption.setCoorType("bd09ll");
        clientOption.setIsNeedAddress(true);
        clientOption.setOpenGps(true);
        locationClient.setLocOption(clientOption);
    }

    @Override
    public void startLoc() {
        locationClient.start();
    }

    @Override
    public void stopLoc() {
        locationClient.stop();
    }

    @Override
    public void navigateTo(BDLocation bdLocation) {
        LatLng latLng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
        if (isFirstLoc){
            MapStatus mapStatus=new MapStatus.Builder().zoom(BaiduMapManager.ZOOM_LEVEL).target(latLng).build();
            MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newMapStatus(mapStatus);
            baiduMap.animateMapStatus(mapStatusUpdate);
            isFirstLoc=false;
        }
        MyLocationData locationData=new MyLocationData.Builder().latitude(latLng.latitude).longitude(latLng.longitude).accuracy(bdLocation.getRadius()).build();
        baiduMap.setMyLocationData(locationData);
    }

    @Override
    public void destroyBaiduMap() {
        locationClient.stop();
        locationClient.unRegisterLocationListener(mBDBdLocationListener);
        mapView.onDestroy();
    }

    class MyBDLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            navigateTo(bdLocation);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

}
