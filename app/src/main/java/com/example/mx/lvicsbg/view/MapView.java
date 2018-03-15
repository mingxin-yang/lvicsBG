package com.example.mx.lvicsbg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.mx.lvicsbg.util.MapDistance;
import com.example.mx.lvicsbg.fragment.MapFragment;
import com.ivics.lib.data.Data;
import com.ivics.lib.dataprocess.OBU;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by XIN on 2017/11/9.
 */

public class MapView extends View {
    private Map<String, Double> mapDistance;
    private Map<String, Double> mapAngle;

    public Data mapdata;
    public Data mdata;
    int flag=0;
    double mapDistanceValue;
    String mapDistanceKey;
    double mapDistanceX;
    double mapDistanceY;
    double mapAngleValue;
    MapFragment fragment=new MapFragment();
    private float ratiox;
    private float ratioy;


    public MapView(Context context) {
        super(context);
        ratiox =(float)0.6;
        ratioy = (float)0.6;
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ratiox =(float)0.6;
        ratioy = (float)0.6;
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mapdata=new Data();
        findLoc();
    }



    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        //自身的位置
        Paint mpaint=new Paint();
        mpaint.setColor(Color.GREEN);
        Path path=new Path();
        path.moveTo(getWidth()/2,getHeight()/2);
        path.lineTo(getWidth()/2-60F,getHeight()/2+60F);
        path.lineTo(getWidth()/2,getHeight()/2-84.85F);
        path.lineTo(getWidth()/2+60F,getHeight()/2+60F);
        canvas.drawPath(path,mpaint);

         if(mapDistanceX != 0 && mapDistanceY != 0){
             Log.i("ondraw","刷新了");
             Log.i("distancexy",ratiox+"");
             Paint paint=new Paint();
             paint.setColor(Color.RED);
             Path path1=new Path();
             path1.moveTo(getWidth()/2+(float)mapDistanceX*ratiox,getHeight()/2+(float)mapDistanceY*ratioy);
             path1.lineTo(getWidth()/2-30F+(float)mapDistanceX*ratiox,getHeight()/2+30F+(float)mapDistanceY*ratioy);
             path1.lineTo(getWidth()/2+(float)mapDistanceX*ratiox,getHeight()/2-34.85F+(float)mapDistanceY*ratioy);
             path1.lineTo(getWidth()/2+30F+(float)mapDistanceX*ratiox,getHeight()/2+30F+(float)mapDistanceY*ratioy);
             canvas.drawPath(path1,paint);
         }

    }

    public void findLoc(){
        mdata=mapdata;
        if (mdata!=null&&mdata.getObuFrame() != null && mdata.getObuFrame().objectList != null
                && mdata.getHostFrame() != null && mdata.getHostFrame().hostobu != null) {
            Log.i(TAG, mdata.getHostFrame().hostobu.speed+"");
            List<OBU> othersLocationList = mdata.getObuFrame().objectList;
            //自身obu的经纬度
            double hostLatitude = mdata.getHostFrame().hostobu.latitude;
            double hostLongitude = mdata.getHostFrame().hostobu.longitude;
            //放置周边obu的deviceID和与自身obu的距离
            mapDistance = new HashMap<>();
            mapAngle = new HashMap<>();
            for (int i = 0; i < othersLocationList.size(); i++) {
                mapDistance.put(othersLocationList.get(i).deviceID, getDistance(
                        othersLocationList.get(i).latitude,
                        othersLocationList.get(i).longitude,
                        hostLatitude, hostLongitude
                ));
            }
            for (int i = 0; i < othersLocationList.size(); i++) {
                mapAngle.put(othersLocationList.get(i).deviceID, getAngle(
                        othersLocationList.get(i).latitude,
                        othersLocationList.get(i).longitude,
                        hostLatitude, hostLongitude
                ));
            }
        }
        if(mapDistance!=null&&mapAngle!=null){
            Iterator kvD = mapDistance.entrySet().iterator();
            Iterator kvA = mapAngle.entrySet().iterator();
            for (int i = 0; i < mapDistance.size(); i++) {
                Map.Entry entry = (Map.Entry) kvD.next();
                mapDistanceValue = (double) entry.getValue();
                Log.i("distance",mapAngleValue+"");
                mapDistanceKey = (String) entry.getKey();
                Map.Entry entry1 = (Map.Entry) kvA.next();
                mapAngleValue = mapAngle.get(mapDistanceKey);
                mapDistanceY = mapDistanceValue * Math.cos(mapAngleValue);
                Log.i("y", mapDistanceY+"");
                mapDistanceX = mapDistanceValue * Math.sin(mapAngleValue);

            }
        }
    }

    public void setData(Data data){
        mapdata=data;
        Log.i(TAG, "setdata:"+mapdata+"");
        flag=1;
        findLoc();
    }

    //两个经纬度之间的距离
    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        return MapDistance.getInstance().getShortDistance(lat1, lng1, lat2, lng2);
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    //两点的方向角
    private double getAngle(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        lat_a = lat_a * Math.PI / 180;
        lng_a = lng_a * Math.PI / 180;
        lat_b = lat_b * Math.PI / 180;
        lng_b = lng_b * Math.PI / 180;

        d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a) * Math.cos(lat_b) * Math.cos(lng_b - lng_a);
        d = Math.sqrt(1 - d * d);
        d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;
        d = Math.asin(d) * 180 / Math.PI;

//     d = Math.round(d*10000);
        return d;
    }


}
