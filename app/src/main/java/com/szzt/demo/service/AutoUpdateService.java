package com.szzt.demo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.szzt.demo.R;
import com.szzt.demo.WeatherActivity;
import com.szzt.demo.gson.Forecast;
import com.szzt.demo.gson.Weather;
import com.szzt.demo.util.HttpUtil;
import com.szzt.demo.util.Utility;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.szzt.demo.WeatherActivity.weatherString;

public class AutoUpdateService extends Service {
    private Runnable runnable;
    private Handler handler;
    private int Time = 8*60*60*1000;//周期时间
    private int anHour =8*60*60*1000;// 这是8小时的毫秒数 为了少消耗流量和电量，8小时自动更新一次
    private Timer timer = new Timer();
    @Override
    public void onCreate() {
        super.onCreate();

        // 方式二：采用timer及TimerTask结合的方法
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //updateWeather();
                Intent  intent = new Intent(AutoUpdateService.this, WeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        };
        timer.schedule(timerTask,
                1000,//延迟1秒执行
                Time);//周期时间

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
/*

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       updateWeather();
       updateBingPic();
        AlarmManager  manager = (AlarmManager) getSystemService(ALARM_SERVICE);
       // int  anHour  =  8 * 60 *60 * 1000;//这是8小时的毫秒数
        int  anHour  = 3 * 1000;//这是8小时的毫秒数
        long  triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent  i = new Intent(AutoUpdateService.this, WeatherActivity.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
*/
        /*
         * 更新天气
         * */
    public void updateWeather() {
        SharedPreferences  prefs  = PreferenceManager.getDefaultSharedPreferences(this);
        String  weatherString  = prefs.getString("weather",null);
        if(weatherString != null){
            //有缓存时直接解析天气数据
            Weather  weather = Utility.handleWeatherResponse(weatherString);
            String  weatherId = weather.basic.weatherId;
            String  weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=8b0a84d10078497499e2c4b4dd0bb13f";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                     if(weather != null && "ok".equals(weather.status)) {
                         SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                         editor.putString("weather", responseText);
                         editor.apply();
                     }
                }
            });
        }
        updateBingPic();
    }
        /*
         * 加载每日一图
         * */
        private void updateBingPic() {
            String requestBingPic = "http://guolin.tech/api/bing_pic";
            HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String bingPic = response.body().string();
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("bing_pic", bingPic);
                    editor.apply();
                }
            });
        }

}
