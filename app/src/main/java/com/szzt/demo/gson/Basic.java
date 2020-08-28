package com.szzt.demo.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("city")
    public   String  cityName;//city表示城市名
    @SerializedName("id")
    public   String  weatherId;//id表示城市对应的天气id
    public   Update  update;//loc表示天气的更新时间
    public   class   Update{
        @SerializedName("loc")
        public  String updateTime;
    }
}
