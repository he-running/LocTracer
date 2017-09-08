package com.siho.loctracer;

import android.view.View;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.siho.loctracer.clusterutil.clustering.ClusterItem;


/**
 * 实现ClusterItem接口
 * Created by hesh on 2017/9/8.
 */

public class MyClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private View icon;


    public MyClusterItem(LatLng position,View icon){
        mPosition=position;
        this.icon=icon;
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
       return BitmapDescriptorFactory.fromView(icon);
    }
}
