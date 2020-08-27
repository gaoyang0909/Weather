package com.szzt.demo.util;

import android.text.TextUtils;

import com.szzt.demo.db.City;
import com.szzt.demo.db.County;
import com.szzt.demo.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /*
    * 解析和处理服务器返回的省级数据
    * */
    public   static   boolean  handleProvinceResponse(String  response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray  allProvinces  = new JSONArray(response);
                for(int i = 0; i < allProvinces.length(); i++){
                    JSONObject  provinceObject = allProvinces.getJSONObject(i);
                    Province  province = new Province();
                    province.setProvinceNmae(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    * 解析和处琎服务器返回的市级数据
    * */
    public   static   boolean  handleCityResponse(String  response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray  allProvinces  = new JSONArray(response);
                for(int i = 0; i < allProvinces.length(); i++){
                    JSONObject  provinceObject = allProvinces.getJSONObject(i);
                    City  city = new City();
                    city.setCityName(provinceObject.getString("name"));
                    city.setCityCode(provinceObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
    * 解析和处理服务器返回的县级数据
    * */
    public   static   boolean  handleCountyResponse(String  response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray  allProvinces  = new JSONArray(response);
                for(int i = 0; i < allProvinces.length(); i++){
                    JSONObject  provinceObject = allProvinces.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(provinceObject.getString("name"));
                    county.setWeatherId(provinceObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}