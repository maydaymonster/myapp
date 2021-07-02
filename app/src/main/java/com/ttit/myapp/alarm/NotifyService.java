package com.ttit.myapp.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.ttit.myapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotifyService extends Service{
    private NotificationManager nm;

    private AlarmManager alarm;

    private boolean isRec = false;
    private boolean isFirst = true;
    private String ACTION = Intent.ACTION_TIME_TICK;
    private SharedPreferences sp;
    private IntentFilter ifter;
    private TimeReceiver receiver;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        sp = getSharedPreferences("alarmNote", MODE_PRIVATE);

        alarm= (AlarmManager) getSystemService(ALARM_SERVICE);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ifter = new IntentFilter();
        ifter.addAction(ACTION);
        receiver = new TimeReceiver();
        //注册receiver
        if (isRec == false) {
            registerReceiver(receiver, ifter);
            isRec = true;
        }

        new Thread(new Runnable() {
            //            AlarmDateBase db;
            @Override
            public void run() {
                Log.d("新", "线程");

            }
        }).start();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("拜拜");
        unregisterReceiver(receiver);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO 自动生成方法根
        Log.i("Service", "onStart now");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String curTime = sdf.format(new Date());
        System.out.println(curTime+"--我是不朽的");

        String time =sp.getString("time", "0000");
        System.out.println("curTime:"+curTime+"    setTime:"+time);
        if (time.equals("0000")||curTime.compareTo(time)>0)
        {
            stopSelf();
        }

        if (curTime.equals(time)) {
            String text = sp.getString("text", "默认事件");
            System.out.println("equal-onStart");
            Notification notify = new Notification(R.drawable.ic_notiation,
                    "小贴士", System.currentTimeMillis());
            PendingIntent pi = PendingIntent.getActivity(this,0,
                    new Intent(this,PlanActivity.class),0);
            notify.defaults = Notification.DEFAULT_ALL;
            nm.notify(1044, notify);
            Intent ActIntent = new Intent(this,DialogActivity.class);
            ActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(ActIntent);

            if (sp.getString("nextTime","").equals("noexist"))
            {
                stopSelf();
                Log.d("yes", "结束了,clear-sp");
            }
            else {
                Log.d("yes", "还没结束");
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


}
