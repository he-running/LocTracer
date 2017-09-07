package com.siho.loctracer;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.mapapi.map.MapView;

/**
 * Created by hesh on 2017/9/7.
 */
public class TestActivity extends BaiduMapActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initBaiduMap();
        initLocOption();
        checkPermission();
    }

    @Override
    public void initBaiduMap() {
        mapView= (MapView) findViewById(R.id.id_mapView);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        super.initBaiduMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
