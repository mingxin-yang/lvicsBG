package com.example.mx.lvicsbg.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mx.lvicsbg.R;
import com.ivics.lib.communicatetools.Communicate;
import com.ivics.lib.data.Data;
import com.ivics.lib.dataprocess.OBU;
import com.ivics.lib.dataprocess.Permission;
import com.ivics.lib.dataprocess.PermissionFrame;
import com.ivics.lib.dataprocess.TrafficLightResult;
import com.ivics.lib.protocol.DataProtocol;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;



/**
 * Created by 12 on 2017/10/24.
 */

public class CarFragment extends Fragment {
    @BindView(R.id.speed_limit)
    TextView speedLimit;
    Unbinder unbinder;
    @BindView(R.id.imageView)
    ImageView iv;
    @BindView(R.id.warningtop)
    ImageView ivt;
    @BindView(R.id.warningrl)
    ImageView ivrt;
    @BindView(R.id.warninglt)
    ImageView ivlt;
    @BindView(R.id.warningd)
    ImageView ivd;
    @BindView(R.id.warningrd)
    ImageView ivrd;
    @BindView(R.id.warningld)
    ImageView ld;
    @BindView(R.id.lefttime)
    TextView lefttime;
    @BindView(R.id.left)
    LinearLayout left;
    @BindView(R.id.forwardtime)
    TextView forwardtime;
    @BindView(R.id.forward)
    LinearLayout forward;
    @BindView(R.id.righttime)
    TextView righttime;
    @BindView(R.id.right)
    LinearLayout right;
    @BindView(R.id.trafficlight)
    LinearLayout trafficlight;
    @BindView(R.id.speed_ceil_left)
    TextView speedCeilLeft;
    @BindView(R.id.speed_floor_left)
    TextView speedFloorLeft;
    @BindView(R.id.speed_ceil_forward)
    TextView speedCeilForward;
    @BindView(R.id.speed_floor_forward)
    TextView speedFloorForward;
    @BindView(R.id.speed_ceil_right)
    TextView speedCeilRight;
    @BindView(R.id.speed_floor_right)
    TextView speedFloorRight;
    @BindView(R.id.recspeed)
    LinearLayout recspeed;
    @BindView(R.id.speed)
    TextView speed;
    @BindView(R.id.speed2)
    TextView speed2;
    @BindView(R.id.speedl)
    LinearLayout speedl;
    @BindView(R.id.traffic_warning_lights)
    ImageView trafficWarningLights;
    @BindView(R.id.warning_type)
    TextView warningType;
    @BindView(R.id.target_type)
    TextView targetType;
    @BindView(R.id.emergency_vehicle)
    TextView emergencyVehicle;
    @BindView(R.id.target_info)
    LinearLayout targetInfo;


    private MediaPlayer mediaPlayer;

    private DataProtocol dataProtocol;



    private int warningtype = 0;
    private int a = 0;
    private int i=10;
    private int speedDiffer=100;
    private int currentStreamID;
    private Thread thread;
    private AnimationDrawable animationDrawable;


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            iv.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            ivrt.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            ivlt.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            ivd.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable4 = new Runnable() {
        @Override
        public void run() {
            ivrd.setVisibility(View.INVISIBLE);
        }
    };
    Runnable runnable5 = new Runnable() {
        @Override
        public void run() {
            ld.setVisibility(View.INVISIBLE);
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View carLayout = inflater.inflate(R.layout.fragment_car, container, false);
        unbinder = ButterKnife.bind(this, carLayout);

        dataProtocol=DataProtocol.getInstance();
        dataProtocol.start();
        initView();
        return carLayout;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        dataProtocol=DataProtocol.getInstance();
//        dataProtocol.start();
//        initView();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        dataProtocol=DataProtocol.getInstance();
//        dataProtocol.start();
//        initView();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        initView();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//    }
//
//    @Override
//    public void onDestroyOptionsMenu() {
//        super.onDestroyOptionsMenu();
//        initView();
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            dataProtocol.stop();
        }else {
            dataProtocol.start();
            initView();
        }
    }



    public void initView(){
        dataProtocol.setOnDataRefreshListener(new Communicate.OnDataRefreshListener() {
            @Override
            public void onDataRefresh(Data data) {

//                EventBus.getDefault().post(new DataEvent(data));

                //速度信息
                if (data.getHostFrame() != null && data.getHostFrame().hostobu != null) {
                    double s = data.getHostFrame().hostobu.speed * 3.6;
                    Log.i("SPEED", s + "");
                    i = (int) Math.floor(s);
                    if(data.getMapResultFrame()!=null&&data.getMapResultFrame().objectList!=null){
                        double sl = data.getMapResultFrame().objectList.get(0).speedlimitceil * 3.6;
                        speedDiffer = (int) Math.floor(sl - s);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (speedDiffer < 5&&speedDiffer>0) {
                            speedLimit.setText("您将要超速");
                            speedDiffer=100;
                        }
                        speed.setText(i + "");
                    }
                });

                //碰撞预警信息
                if (data.getTargetFrame() != null && data.getTargetFrame().objectList != null) {
                    int flag = data.getTargetFrame().objectList.get(0).warningtype;
                    int targetflag = data.getTargetFrame().objectList.get(0).targettype;
//                if(flag==6){
//                    a=-1;
//                }
                    if (a == 0 || a == -1) {
                        a = 1;
                        playSound();
                    }

//                 warningtype=flag;

                    //预警事件类型
                    if (flag == 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                warningType.setText("Danger");
                            }
                        });
                    } else if (flag == 2) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                warningType.setText("Emergency");
                            }
                        });
                    } else if (flag == 3) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                warningType.setText("Brake");
                            }
                        });
                    } else if (flag == 4) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                warningType.setText("BlindZone");
                            }
                        });
                    } else if (flag == 5) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                warningType.setText("Rearend");
                            }
                        });
                    } else if (flag == 6) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                warningType.setText("Collision");
                            }
                        });
                    }
                    //预警目标类型
                    if (targetflag == 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                targetType.setText("Unknown");
                            }
                        });
                    } else if (targetflag == 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                targetType.setText("Motor");
                            }
                        });
                    } else if (targetflag == 2) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                targetType.setText("non-motor");
                            }
                        });
                    } else if (targetflag == 3) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                targetType.setText("Pedestrian");
                            }
                        });
                    }
                    double angle = data.getTargetFrame().objectList.get(0).targetangle;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                try {
                                    if (mediaPlayer.isPlaying()) {
                                        iv.setImageResource(R.drawable.animation);
                                        animationDrawable = (AnimationDrawable) iv.getDrawable();
                                        iv.setVisibility(View.VISIBLE);
                                        animationDrawable.start();
                                    }
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }

                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        iv.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            iv.setVisibility(View.VISIBLE);
                            handler.postDelayed(runnable, 1000);
                        }
                    });
                    //正前方
                    if (angle > -Math.PI / 4 && angle < Math.PI / 4) {

                    }
                    //右前方
                    if (angle > Math.PI / 4 && angle < Math.PI / 2) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivrt.setVisibility(View.VISIBLE);
                                handler.postDelayed(runnable1, 1000);
                            }
                        });
                    }
                    //左前方
                    if (angle > -Math.PI / 2 && angle < -Math.PI / 4) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivlt.setVisibility(View.VISIBLE);
                                handler.postDelayed(runnable2, 1000);
                            }
                        });
                    }
                    //正后方
                    if (angle < -Math.PI * 3 / 4 && angle > Math.PI * 3 / 4) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivd.setVisibility(View.VISIBLE);
                                handler.postDelayed(runnable3, 1000);
                            }
                        });
                    }
                    //右后方
                    if (angle > Math.PI / 2 && angle < Math.PI * 3 / 4) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivrd.setVisibility(View.VISIBLE);
                                handler.postDelayed(runnable4, 1000);
                            }
                        });
                    }
                    //左后方
                    if (angle > -Math.PI * 3 / 4 && angle < -Math.PI / 2) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ld.setVisibility(View.VISIBLE);
                                handler.postDelayed(runnable5, 1000);
                            }
                        });
                    }

                }

                //信号灯信息
                if (data.getTrafficLightResultFrame() != null && data.getTrafficLightResultFrame().
                        objectList != null) {
                    ArrayList<TrafficLightResult> objectList = data.getTrafficLightResultFrame().objectList;

                    for (int i = 0; i < 3; i++) {
                        final TrafficLightResult x = objectList.get(i);
                        if (x.turnangle > -Math.PI / 2 && x.turnangle < -Math.PI / 4) {
                            //左转的信号灯
                            if (x.lightstate == 3) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        left.setBackgroundResource(R.drawable.circle3);
                                    }
                                });

                            } else if (x.lightstate == 6) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        left.setBackgroundResource(R.drawable.circle6);
                                    }
                                });
                            } else if (x.lightstate == 5 || x.lightstate == 7) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        left.setBackgroundResource(R.drawable.circle57);
                                    }
                                });
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lefttime.setText(x.timeleft + "");
                                    speedCeilLeft.setText(x.recspeedceil + "");
                                    speedFloorLeft.setText(x.recspeedfloor + "");
                                }
                            });
                        }
                        if (x.turnangle > -Math.PI / 4 && x.turnangle < Math.PI / 4) {
                            //前行的信号灯
                            if (x.lightstate == 3) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        forward.setBackgroundResource(R.drawable.circle3);
                                    }
                                });

                            } else if (x.lightstate == 6) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        forward.setBackgroundResource(R.drawable.circle6);
                                    }
                                });
                            } else if (x.lightstate == 5 || x.lightstate == 7) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        forward.setBackgroundResource(R.drawable.circle57);
                                    }
                                });
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    forwardtime.setText(x.timeleft + "");
                                    speedCeilForward.setText(x.recspeedceil + "");
                                    speedFloorForward.setText(x.recspeedfloor + "");
                                }
                            });
                        }
                        if (x.turnangle > Math.PI / 4 && x.turnangle < Math.PI / 2) {
                            //右转的信号灯
                            if (x.lightstate == 3) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        right.setBackgroundResource(R.drawable.circle3);
                                    }
                                });

                            } else if (x.lightstate == 6) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        right.setBackgroundResource(R.drawable.circle6);
                                    }
                                });
                            } else if (x.lightstate == 5 || x.lightstate == 7) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        right.setBackgroundResource(R.drawable.circle57);
                                    }
                                });
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    righttime.setText(x.timeleft + "");
                                    speedCeilRight.setText(x.recspeedceil + "");
                                    speedFloorRight.setText(x.recspeedfloor + "");
                                }
                            });
                        }
                    }

                }

                //Rsi危险警告
                if (data.getRsiResultFrame() != null && data.getRsiResultFrame().objectList != null) {
                    short rsitype = data.getRsiResultFrame().objectList.get(0).rsitype;
                    if (rsitype == 37) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trafficWarningLights.setImageResource(R.drawable.traffic_warning_lights);
                            }
                        });
                    }
                    if (rsitype == 15) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trafficWarningLights.setImageResource(R.drawable.traffic_warning_lights);
                            }
                        });
                    }
                    if (rsitype == 2) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trafficWarningLights.setImageResource(R.drawable.traffic_warning_lights);
                            }
                        });
                    }
                    if (rsitype == 38) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trafficWarningLights.setImageResource(R.drawable.traffic_warning_lights);
                            }
                        });
                    }
                    if (rsitype == 17) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trafficWarningLights.setImageResource(R.drawable.traffic_warning_lights);
                            }
                        });
                    }
                    if (rsitype == 21) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trafficWarningLights.setImageResource(R.drawable.traffic_warning_lights);
                            }
                        });
                    }
                }

                //紧急车辆提醒
                if (data.getObuFrame() != null && data.getObuFrame().objectList != null) {
                    ArrayList<OBU> object = data.getObuFrame().objectList;
                    for (int i = 0; i < object.size(); i++) {
                        if (object.get(i).vehicleType2 != 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    emergencyVehicle.setText("周围有紧急车辆，请注意避让");
                                }
                            });

                        }
                    }
                }
                //异常车辆
                if (data.getObuFrame() != null && data.getObuFrame().objectList != null) {

                }


            }
            });
    }


    private void playSound() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                 soundPool.load(MainActivity.this,R.raw.warning,1);
//                 soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                     @Override
//                     public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                         currentStreamID=soundPool.play(1,1,1, 0, 0, 1);
//
//                         if(a==-1){
//                            soundPool.stop(currentStreamID);
//                            soundPool.play(1,1, 1, 0, 0, 2);
//                         }
//                     }
//                 });
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }

                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.warning);

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mediaPlayer != null) {
                            try {
                                mediaPlayer.start();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer = null;
                    }
                });


                a = 0;
            }

        });
        thread.start();
    }

    public void onDataRefresh(Data data){
        if(onDataRefreshes!=null){
            for (OnDataRefresh onDataRefresh : onDataRefreshes ){
                onDataRefresh.dataRefresh(data);
            }
        }
    }

    private interface OnDataRefresh{
        void dataRefresh(Data data);
    }

    private List<OnDataRefresh> onDataRefreshes;

    private void addOnDataRefresh(OnDataRefresh onDataRefresh){
        if(onDataRefresh==null) {
            onDataRefreshes = new ArrayList<>();
        }
        onDataRefreshes.add(onDataRefresh);
    }

    private void removeOnDataRefresh(OnDataRefresh onDataRefresh){
        if(onDataRefresh!=null){
            for (int j = 0; j <onDataRefreshes.size() ; j++) {
                if(onDataRefreshes.get(j)==onDataRefresh){
                    onDataRefreshes.remove(j);
                }
                
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
