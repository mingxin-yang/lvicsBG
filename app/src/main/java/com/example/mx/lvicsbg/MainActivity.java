package com.example.mx.lvicsbg;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
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
    private ImageView ivrt;
    private ImageView ivlt;
    private ImageView ivd;
    private ImageView ivrd;
    private ImageView ld;
    private SoundPool soundPool;
    private Permission pm;
    private PermissionFrame pmf;
    private int warningtype=0;
    private int a=0;
    private int currentStreamID;
    private Thread thread;


    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            iv.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            ivrt.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable2=new Runnable() {
        @Override
        public void run() {
            ivlt.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable3=new Runnable() {
        @Override
        public void run() {
            ivd.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable4=new Runnable() {
        @Override
        public void run() {
            ivrd.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable5=new Runnable() {
        @Override
        public void run() {
            ld.setVisibility(View.INVISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv=(ImageView) findViewById(R.id.warningtop);
        ivrt=(ImageView) findViewById(R.id.warningrl);
        ivlt=(ImageView)findViewById(R.id.warninglt);
        ivd=(ImageView)findViewById(R.id.warningd);
        ivrd=(ImageView)findViewById(R.id.warningrd);
        ld=(ImageView)findViewById(R.id.warningld) ;

        mDataProtocol=DataProtocol.getInstance();
        pm=new Permission((byte)0x09,(byte)0x01,500);
        pmf=new PermissionFrame();
        pmf.objectList.add(pm);
        mDataProtocol.setFrame(pmf);
        mDataProtocol.start();
        soundPool=new SoundPool(1,  AudioManager.STREAM_SYSTEM,5);

        mDataProtocol.setOnDataRefreshListener(listener);



    }
     Communicate.OnDataRefreshListener listener=new Communicate.OnDataRefreshListener() {
         @Override
         public void onDataRefresh(final Data data) {
             if (data.getTargetFrame()!=null&&data.getTargetFrame().objectList!=null){
                 int flag=data.getTargetFrame().objectList.get(0).warningtype;
                 if(flag==6){
                    a=-1;
                 }
                 if(a==0){
                     a=1;
                     PlaySound();
                 }

//                 warningtype=flag;

                 double angle=data.getTargetFrame().objectList.get(0).targetangle;
                 //正前方
                 if(angle>-pi/4&&angle<pi/4){
                     MainActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             iv.setVisibility(View.VISIBLE);
                             handler.postDelayed(runnable,1000);
                         }
                     });
                 }
                 //右前方
                 if(angle>pi/4&&angle<pi/2){
                     MainActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             ivrt.setVisibility(View.VISIBLE);
                             handler.postDelayed(runnable1,1000);
                         }
                     });
                 }
                 //左前方
                 if(angle>-pi/2&&angle<-pi/4){
                     MainActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             ivlt.setVisibility(View.VISIBLE);
                             handler.postDelayed(runnable2,1000);
                         }
                     });
                 }
                 //正后方
                 if(angle<-pi*3/4&&angle>pi*3/4){
                     MainActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             ivd.setVisibility(View.VISIBLE);
                             handler.postDelayed(runnable3,1000);
                         }
                     });
                 }
                 //右后方
                 if(angle>pi/2&&angle<pi*3/4){
                     MainActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             ivrd.setVisibility(View.VISIBLE);
                             handler.postDelayed(runnable4,1000);
                         }
                     });
                 }
                 //左后方
                 if(angle>-pi*3/4&&angle<-pi/2){
                     MainActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             ld.setVisibility(View.VISIBLE);
                             handler.postDelayed(runnable5,1000);
                         }
                     });
                 }

             }


         }
     };
     private synchronized void PlaySound(){
         thread=new Thread(new Runnable() {
             @Override
             public void run() {
                 soundPool.load(MainActivity.this,R.raw.warning,1);
                 soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                     @Override
                     public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                         currentStreamID=soundPool.play(1,1,1, 0, 0, 1);

                         if(a==-1){
                            soundPool.stop(currentStreamID);
                            soundPool.play(1,1, 1, 0, 0, 2);
                         }
                     }
                 });
                 a=0;
             }
         });
         thread.start();
     }

}
