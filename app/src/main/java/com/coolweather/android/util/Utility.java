package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.AQI;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.NowWeather;
import com.coolweather.android.gson.Suggest;
import com.google.gson.Gson;
import com.google.gson.JsonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static boolean handleProvinceResp(String jsonStr){
        if (!TextUtils.isEmpty(jsonStr)){
            try {
                JSONArray array=new JSONArray(jsonStr);
                for (int i = 0; i<array.length() ; i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.setProvinceName(jsonObject.getString("name"));
                    province.save();
                }



                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static boolean handleCityResp(String jsonStr,int provinceId){
        if (!TextUtils.isEmpty(jsonStr)){
            try {
                JSONArray array=new JSONArray(jsonStr);
                for (int i = 0; i<array.length() ; i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    City city=new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
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

    public static boolean handleCountyResp(String jsonStr,int cityId){
        if (!TextUtils.isEmpty(jsonStr)){
            try {
                JSONArray array=new JSONArray(jsonStr);
                for (int i = 0; i<array.length() ; i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setCityId(cityId);
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.save();
                }



                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
    private static Gson gson=new Gson();
    public static Forecast handleForecastResponse(String response){
        try {
            Forecast forecast = gson.fromJson(response, Forecast.class);
            return forecast;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static AQI handleAQIResponse(String response) {
        try {
            AQI aqi = gson.fromJson(response, AQI.class);
            return aqi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Suggest handleSuggestResponse(String response) {
        try {
            Suggest suggest = gson.fromJson(response, Suggest.class);
            return suggest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static NowWeather handleNowWeatherResponse(String response) {
        try {
            NowWeather nowWeather = gson.fromJson(response, NowWeather.class);
            return nowWeather;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}































