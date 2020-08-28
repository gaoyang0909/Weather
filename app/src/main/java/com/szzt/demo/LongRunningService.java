package com.szzt.demo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.szzt.demo.gson.Weather;

public class LongRunningService extends Service {
    private Context  mcontext;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mcontext = MyApplication.getContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Weather weather = null;
                Intent intent  = new Intent(mcontext,WeatherActivity.class);
                startActivity(intent);
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int  anmintue = 2*1000;//一分钟更新
        long  triggerAtTime = SystemClock.elapsedRealtime() + anmintue;
        Intent i = new Intent(this,LongRunningService.class);
        PendingIntent  pi = PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return  super.onStartCommand(intent,flags,startId);
    }
}
