package com.example.mx.lvicsbg;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ivics.lib.communicatetools.Communicate;
import com.ivics.lib.data.Data;
import com.ivics.lib.dataprocess.Permission;
import com.ivics.lib.dataprocess.PermissionFrame;
import com.ivics.lib.dataprocess.TargetFrame;
import com.ivics.lib.protocol.DataProtocol;

public class MainActivity extends AppCompatActivity {

    private DataProtocol mDataProtocol;
    private double pi=3.1415;
    private ImageView iv;
    private SoundPool soundPool;
    private Permission pm;
    private PermissionFrame pmf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv=(ImageView) findViewById(R.id.warningtop);

        mDataProtocol=DataProtocol.getInstance();
        pm=new Permission((byte)0x09,(byte)0x01,500);
        pmf=new PermissionFrame();
        pmf.objectList.add(pm);
        mDataProtocol.setFrame(pmf);
        mDataProtocol.start();
        soundPool=new SoundPool(1, AudioManager.STREAM_SYSTEM,5);

        mDataProtocol.setOnDataRefreshListener(listener);

    }
     Communicate.OnDataRefreshListener listener=new Communicate.OnDataRefreshListener() {
         @Override
         public void onDataRefresh(final Data data) {
             if (data.getTargetFrame()!=null&&data.getTargetFrame().objectList!=null){
                 double angle=data.getTargetFrame().objectList.get(0).targetangle;
//                         if(angle>-pi/4&&angle>pi/4){
                 MainActivity.this.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         iv.setVisibility(View.VISIBLE);
                         soundPool.load(MainActivity.this,R.raw.warning,1);
                         soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                             @Override
                             public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                                 soundPool.play(1,1, 1, 0, 0, 1);
                             }
                         });

                     }
                 });


//                         }
             }


         }
     };
}
