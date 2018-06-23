package com.example.a13612.weather.utils;

import android.text.TextUtils;

import com.example.a13612.weather.db.City;
import com.example.a13612.weather.db.County;
import com.example.a13612.weather.db.Province;
import com.example.a13612.weather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     * @param response 服务器返回省级数据
     * @return
     */
    public static boolean handleProvinceResponse(String response){
        //工具类审查是否为字符串response是否为空
        if(!TextUtils.isEmpty(response) ){
            try{
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.setProvinceName(provinceObject.getString("name"));
                    //save（）方法将数据存储到数据库之中
                    province.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param provinceId  当要确定市级上一级市级的时候需要用到这个来确定，因为服务器传递
     *                属于确定下一级数据
     * @param response
     * @return
     */
    public static boolean handleCityResponse(String response,int provinceId){
        //工具类审查是否为字符串response是否为空
        if(!TextUtils.isEmpty(response) ){
            try{
                JSONArray allCities=new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);
                    //save（）方法将数据存储到数据库之中
                    city.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * @param response 服务器返回的县级数据
     * @param cityId  当要确定县级上一级市级的时候需要用到这个来确定，因为服务器传递
     *                属于确定下一级数据
     * @return
     */
    public static boolean handleCountyResponse(String response,int cityId){
        //工具类审查是否为字符串response是否为空
        if(!TextUtils.isEmpty(response) ){
            try{
                JSONArray allCounties=new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCountyName(countyObject.getString("name"));
                    county.setCityId(cityId);
                    //save（）方法将数据存储到数据库之中
                    county.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
