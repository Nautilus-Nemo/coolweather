package com.example.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 曾浩 on 2017/10/7.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
