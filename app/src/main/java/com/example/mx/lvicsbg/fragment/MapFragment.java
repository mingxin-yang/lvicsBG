package com.example.mx.lvicsbg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mx.lvicsbg.view.MapView;
import com.example.mx.lvicsbg.R;
import com.ivics.lib.communicatetools.Communicate;
import com.ivics.lib.data.Data;
import com.ivics.lib.protocol.DataProtocol;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 12 on 2017/10/24.
 */

public class MapFragment extends Fragment {

    /**
     * 地球半径
     */
    public static final double EARTH_RADIUS = 6371.393;

    public Data mapData;
    private DataProtocol dataProtocol;
    Unbinder unbinder;
    @BindView(R.id.map_view1)
    MapView mapView1;
    private View mapLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null !=mapLayout){
            ViewGroup parent =(ViewGroup) mapLayout.getParent();
            if(null != parent){
                parent.removeView(mapLayout);
            }
        }else {
            mapLayout = inflater.inflate(R.layout.fragment_map, container, false);
            unbinder = ButterKnife.bind(this, mapLayout);

//        EventBus.getDefault().register(this);
            mapData=new Data();
            dataProtocol=DataProtocol.getInstance();
            dataProtocol.start();
            this.mapView1=(MapView)this.mapLayout.findViewById(R.id.map_view1);

            dataProtocol.setOnDataRefreshListener(new Communicate.OnDataRefreshListener() {
                @Override
                public void onDataRefresh(final Data data) {
                        if (data != null){
                            mapView1.setData(data);
                            mapView1.postInvalidate();
                        }
                    }
                });

        }
        return mapLayout;
    }
    public void updateUI(){
        this.mapView1.invalidate();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            dataProtocol.stop();
        }else {
            dataProtocol.start();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册事件
//        EventBus.getDefault().unregister(this);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMoonEvent(DataEvent messageEvent){
//        data=messageEvent.getData();
//    }


}
