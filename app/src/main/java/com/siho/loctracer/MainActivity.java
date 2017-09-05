package com.siho.loctracer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@ContentView(R.layout.main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.iv_circleView)
    private BootstrapCircleThumbnail iv_circleView;
    @ViewInject(R.id.id_mapView)
    private MapView mapView;
    private BaiduMap baiduMap;

    //-----------定位相关-------------
    private LocationClient locationClient;
    private LocationClientOption clientOption;
    private  boolean isFirstLoc=true;
    private MyLocationListener mLocListener;
    private LatLng latLng;

    private  long lastBackTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        initLocOption();
    }

    private void initLocOption(){
        locationClient=new LocationClient(getApplicationContext());
        clientOption=new LocationClientOption();
        mLocListener=new MyLocationListener();
        clientOption.setCoorType("bd09ll");
        clientOption.setNeedDeviceDirect(true);
        clientOption.setScanSpan(4000);
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClient.setLocOption(clientOption);
        locationClient.registerLocationListener(mLocListener);
        locationClient.start();
    }

    private void navToMyLoc(BDLocation bdLocation){
        latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        if (isFirstLoc) {
            MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(16.0f).build();
            MapStatusUpdate StatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            baiduMap.animateMapStatus(StatusUpdate);
            isFirstLoc=false;
        }
        Log.w("hesh", "navToMyLoc: "+bdLocation.getAddrStr());
        MyLocationData locData=new MyLocationData.Builder().latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude())
                .accuracy(bdLocation.getRadius()).build();
        baiduMap.setMyLocationData(locData);
    }

    class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            navToMyLoc(bdLocation);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Event(R.id.btn_circleView)
    private void onCircleViewClick(View v){
        View view= LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.circleview,null);
        view.setBackgroundResource(R.mipmap.hey2);
        BootstrapCircleThumbnail circleThumbnail2= (BootstrapCircleThumbnail) view.findViewById(R.id.cv_circleView);
        circleThumbnail2.setImageResource(R.mipmap.cluo);
        iv_circleView.setImageBitmap(getViewBitmap(view));
    }



    @Event(R.id.btn_display)
    private void onDisplayClick(View v){
        baiduMap.clear();

        View view= LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.circleview,null);
        View view2= LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.circleview,null);
        View view3= LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.circleview,null);

        view.setBackgroundResource(R.mipmap.hey2);
        view2.setBackgroundResource(R.mipmap.hey2);
        view3.setBackgroundResource(R.mipmap.hey2);

        BootstrapCircleThumbnail circleThumbnail= (BootstrapCircleThumbnail) view.findViewById(R.id.cv_circleView);
        BootstrapCircleThumbnail circleThumbnail2= (BootstrapCircleThumbnail) view2.findViewById(R.id.cv_circleView);
        BootstrapCircleThumbnail circleThumbnail3= (BootstrapCircleThumbnail) view3.findViewById(R.id.cv_circleView);

        circleThumbnail.setImageResource(R.mipmap.cluo);
        circleThumbnail2.setImageResource(R.mipmap.cluo);
        circleThumbnail3.setImageResource(R.mipmap.cluo);

        LatLng latLng2=new LatLng(latLng.latitude+0.05,latLng.longitude);
        LatLng latLng3=new LatLng(latLng.latitude,latLng.longitude+0.05);

        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromBitmap(getViewBitmap(view));
        BitmapDescriptor bitmapDescriptor2= BitmapDescriptorFactory.fromView(view2);
        BitmapDescriptor bitmapDescriptor3= BitmapDescriptorFactory.fromView(view3);

        MarkerOptions marker=new MarkerOptions()
                .draggable(true)
                .icon(bitmapDescriptor)
                .position(latLng);
        MarkerOptions marker2=new MarkerOptions()
                .draggable(true)
                .icon(bitmapDescriptor2)
                .position(latLng2);
        MarkerOptions marker3=new MarkerOptions()
                .draggable(true)
                .icon(bitmapDescriptor3)
                .position(latLng3);

        baiduMap.addOverlay(marker);
        baiduMap.addOverlay(marker2);
        baiduMap.addOverlay(marker3);
        Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.unRegisterLocationListener(mLocListener);
        locationClient.stop();
        mapView.onDestroy();
    }


    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if ((System.currentTimeMillis()-lastBackTime)>2000){
            lastBackTime=System.currentTimeMillis();
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            return;
        }else {
            finish();
        }
    }
}
