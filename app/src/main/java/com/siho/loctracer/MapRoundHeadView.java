package com.siho.loctracer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

/**
 * 用于在地图显示圆形头像
 * Created by hesh on 2017/9/5.
 */
public class MapRoundHeadView{

    private Context context;
    private View view;
    private BootstrapCircleThumbnail circleThumbnail;

    public MapRoundHeadView(Context context,int bg,int image) {
        view=LayoutInflater.from(context).inflate(R.layout.circleview,null);
        circleThumbnail= (BootstrapCircleThumbnail) view.findViewById(R.id.cv_circleView);
        view.setBackgroundResource(bg);
        circleThumbnail.setImageResource(image);
    }

    public View getView(){
        return view;
    }
}
