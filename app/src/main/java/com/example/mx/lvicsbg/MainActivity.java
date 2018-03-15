package com.example.mx.lvicsbg;





import android.os.Handler;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mx.lvicsbg.fragment.CarFragment;
import com.example.mx.lvicsbg.fragment.MapFragment;


public class MainActivity extends AppCompatActivity {

    private CarFragment carFragment;
    private MapFragment mapFragment;
    private View carLayout;
    private View mapLayout;
    private ImageView carImage;
    private ImageView mapImage;
    private FragmentManager fragmentManager;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        //初始化布局元素
        initView();
        fragmentManager=getSupportFragmentManager();
        setTabSelection(0);
        if (mapFragment!=null){
            this.mapFragment.updateUI();
        }

    }


    
    private void setTabSelection(int index) {
        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index){
            case 0:
                if(carFragment==null){
                    carFragment=new CarFragment();
                    transaction.add(R.id.content, carFragment);
                }else {
                    transaction.show(carFragment);
                }
                break;
            case 1:
                if(mapFragment==null){
                    mapFragment=new MapFragment();
                    transaction.add(R.id.content,mapFragment);
                }else {
                    transaction.show(mapFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (carFragment != null) {
            transaction.hide(carFragment);
        }
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
    }

    private void initView() {
        mapImage=(ImageView)findViewById(R.id.map_image);

        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1);
            }
        });
        carImage=(ImageView)findViewById(R.id.car_image);
        carImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(0);
            }
        });

    }

    public void setHandler(Handler handler){
        mHandler=handler;
    }



}
