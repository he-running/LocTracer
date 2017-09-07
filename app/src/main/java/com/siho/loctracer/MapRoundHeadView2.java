package com.siho.loctracer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

/**
 * 用于在地图显示圆形头像
 * Created by hesh on 2017/9/5.
 */
public class MapRoundHeadView2 {

    private Context context;
    private View view;
    private RelativeLayout rl_circleVieBg;
    private BootstrapCircleThumbnail circleThumbnail;
    private TextView textView;

    public MapRoundHeadView2(Context context, int bg, int image, String tx) {
        view=LayoutInflater.from(context).inflate(R.layout.circleview,null);

        rl_circleVieBg= (RelativeLayout) view.findViewById(R.id.rl_circleView);
        circleThumbnail= (BootstrapCircleThumbnail) view.findViewById(R.id.cv_circleView);
        textView= (TextView) view.findViewById(R.id.tv_num);

        rl_circleVieBg.setBackgroundResource(bg);
        circleThumbnail.setImageResource(image);
//        circleThumbnail.setImageBitmap(image);//可傳入一個bitmap對象
        textView.setText(tx);
    }

    public View getView(){
        return view;
    }
}
