package com.skyweather.android.util;

import android.text.TextUtils;

import com.google.gson.JsonArray;

import org.json.JSONArray;

public class Utility {
    public static boolean handleProvinceResp(String jsonStr){
        if (!TextUtils.isEmpty(jsonStr)){
            try{
                JSONArray allProvinces=new JSONArray(jsonStr);
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            

        }

        return false;
    }


    public static boolean handleCityResp(String jsonStr,int provinceId){
        if (!TextUtils.isEmpty(jsonStr)){
            try{
                JSONArray allCities=new JSONArray(jsonStr);
                for (int i=0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
            }
            return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }return false;


    public static boolean handleCountyResp(String jsonStr,int cityId){
        if (!TextUtils.isEmpty(jsonStr)){
            try{
                JSONArray allCounties=new JSONArray(jsonStr);
                for (int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
                
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
