package com.siho.loctracer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.siho.loctracer.clusterutil.MarkerManager;
import com.siho.loctracer.clusterutil.clustering.Cluster;
import com.siho.loctracer.clusterutil.clustering.ClusterManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.main)
public class MainActivity extends AppCompatActivity implements BaiduMapView{

    @ViewInject(R.id.iv_circleView)
    private BootstrapCircleThumbnail iv_circleView;
    @ViewInject(R.id.id_mapView)
    private MapView mapView;

    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private LocationClientOption clientOption;
    private MyBDLocationListener mBDBdLocationListener;
    private MyOnMarkerClickListener mOnMarkerClickListener;

    private ArrayList<String> permissionList=new ArrayList<String>();
    private boolean isFirstLoc=true;
    private LatLng latLng;
    private  long lastBackTime;

    //----------------百度地图点聚合--------------------//
    private ClusterManager<MyClusterItem> clusterManager;

    private MarkerManager markerManager;
    private MarkerManager.Collection  markerCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

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

    @Event(R.id.btn_circleView)
    private void onCircleViewClick(View v){
        View view= LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.circleview,null);
        RelativeLayout rl_circlieView= (RelativeLayout) view.findViewById(R.id.rl_circleView);
        BootstrapCircleThumbnail circleThumbnail2= (BootstrapCircleThumbnail) view.findViewById(R.id.cv_circleView);

        rl_circlieView.setBackgroundResource(R.mipmap.bg_solid);
        circleThumbnail2.setImageResource(R.mipmap.cluo);
        iv_circleView.setImageBitmap(getViewBitmap(view));
    }

    @Event(R.id.btn_display)
    private void onDisplayClick(View v){
      addMarkers();
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        // 添加Marker点
        baiduMap.clear();

        MapRoundHeadView headView1=new MapRoundHeadView(getApplicationContext(),R.mipmap.bg_solid_white,R.mipmap.cluo,"18825157045");
        MapRoundHeadView headView2=new MapRoundHeadView(getApplicationContext(),R.mipmap.bg_solid_white,R.mipmap.cluo,"18825157043");
        MapRoundHeadView headView3=new MapRoundHeadView(getApplicationContext(),R.mipmap.bg_solid,R.mipmap.cluo,"18825157042");
        MapRoundHeadView headView4=new MapRoundHeadView(getApplicationContext(),R.mipmap.bg_solid,R.mipmap.cluo,"18825157041");

        LatLng latLng2=new LatLng(latLng.latitude+0.05,latLng.longitude);
        LatLng latLng3=new LatLng(latLng.latitude,latLng.longitude+0.05);
        LatLng latLng4=new LatLng(latLng.latitude+0.05,latLng.longitude+0.05);
        LatLng latLng5=new LatLng(latLng.latitude+0.06,latLng.longitude+0.03);
        LatLng latLng6=new LatLng(latLng.latitude+0.03,latLng.longitude+0.07);
        LatLng latLng7=new LatLng(latLng.latitude+0.02,latLng.longitude+0.01);
        LatLng latLng8=new LatLng(latLng.latitude+0.08,latLng.longitude+0.07);
        LatLng latLng9=new LatLng(latLng.latitude+0.1,latLng.longitude+0.05);

        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromBitmap(getViewBitmap(headView1.getView()));
        BitmapDescriptor bitmapDescriptor2= BitmapDescriptorFactory.fromView(headView2.getView());
        BitmapDescriptor bitmapDescriptor3= BitmapDescriptorFactory.fromView(headView3.getView());
        BitmapDescriptor bitmapDescriptor4= BitmapDescriptorFactory.fromView(headView4.getView());

        MarkerOptions marker=new MarkerOptions()
                .zIndex(1)
                .icon(bitmapDescriptor)
                .position(latLng)
                .anchor(0.51f,0.65f);//设置锚点

        MarkerOptions marker2=new MarkerOptions()
                .zIndex(2)
                .icon(bitmapDescriptor2)
                .position(latLng2);

        MarkerOptions marker3=new MarkerOptions()
                .zIndex(3)
                .icon(bitmapDescriptor3)
                .position(latLng3);

        MarkerOptions marker4=new MarkerOptions()
                .zIndex(4)
                .icon(bitmapDescriptor4)
                .position(latLng4);
//
//        baiduMap.addOverlay(marker);
//        baiduMap.addOverlay(marker2);
//        baiduMap.addOverlay(marker3);
//        baiduMap.addOverlay(marker4);

//        markerCollection.clear();
//        markerCollection.addMarker(marker);
//        markerCollection.addMarker(marker2);
//        markerCollection.addMarker(marker3);
//        markerCollection.addMarker(marker4);

        List itemList=new ArrayList<>();
        itemList.add(new MyClusterItem(latLng,headView2.getView()));
        itemList.add(new MyClusterItem(latLng2,headView2.getView()));
        itemList.add(new MyClusterItem(latLng3,headView3.getView()));
        itemList.add(new MyClusterItem(latLng4,headView4.getView()));
        itemList.add(new MyClusterItem(latLng5,headView1.getView()));
        itemList.add(new MyClusterItem(latLng6,headView2.getView()));
        itemList.add(new MyClusterItem(latLng7,headView2.getView()));
        itemList.add(new MyClusterItem(latLng8,headView2.getView()));
        itemList.add(new MyClusterItem(latLng9,headView2.getView()));

        clusterManager.clearItems();
        clusterManager.addItems(itemList);
        clusterManager.cluster();
    }

    @Override
    public void initBaiduMap() {
        mapView= (MapView) findViewById(R.id.id_mapView);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
//        mOnMarkerClickListener=new MyOnMarkerClickListener();
//        baiduMap.setOnMarkerClickListener(mOnMarkerClickListener);
        locationClient=new LocationClient(getApplicationContext());
        mBDBdLocationListener=new MyBDLocationListener();
        locationClient.registerLocationListener(mBDBdLocationListener);

        clusterManager=new ClusterManager<MyClusterItem>(getApplicationContext(),baiduMap);
        baiduMap.setOnMapStatusChangeListener(clusterManager);
        baiduMap.setOnMarkerClickListener(clusterManager);
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyClusterItem> cluster) {
                Toast.makeText(MainActivity.this,
                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyClusterItem>() {
            @Override
            public boolean onClusterItemClick(MyClusterItem item) {
                Toast.makeText(MainActivity.this,
                        "点击单个Item", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        requestPermissionsResult(requestCode, permissions, grantResults);
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
        clientOption.setScanSpan(2000);
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
        latLng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
        if (isFirstLoc){
            MapStatus mapStatus=new MapStatus.Builder().zoom(16.0f).target(latLng).build();
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

    class MyOnMarkerClickListener implements BaiduMap.OnMarkerClickListener{

        @Override
        public boolean onMarkerClick(Marker marker) {
            switch (marker.getZIndex()){
                case 1:
                    marker.setTitle("我是C.罗纳尔多");
                    Toast.makeText(MainActivity.this,"我是C.罗纳尔多",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    marker.setTitle("我是超强球星");
                    Toast.makeText(MainActivity.this,"我是超强球星",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    marker.setTitle("我是超人");
                    Toast.makeText(MainActivity.this,"我是超人",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    marker.setTitle("我是超级射手");
                    Toast.makeText(MainActivity.this,"我是超级射手",Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    }

    /**
     * 把view转成bitmap
     * @param view
     * @return
     */
    private Bitmap getViewBitmap(View view) {

        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis()-lastBackTime)>2000){
            lastBackTime=System.currentTimeMillis();
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            return;
        }else {
            finish();
        }
    }

}
