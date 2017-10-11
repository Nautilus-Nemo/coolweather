package com.example.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 曾浩 on 2017/10/7.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
