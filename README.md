---
title: android天气项目设计文档（第一行代码）
categories:
- android
tags:
- android
- project
---

### 1. 设计目的

为了让广大`Android`手机用户能够在第一时间获取最新天气预报消息，以便提前预防，方便出行。同时，把气象翟海早餐的瞬时降到最低，也可以提高公共服务质量，更好的发挥气象事业对经济社会发展的现实作用，有很强的实用价值。虽然该技术在Android平台早已比较成熟，但是通过本次软件的设计开发仍然能帮助我更好的认识Android系统的工作原理。

### 2.开发环境

- `android studio`
- `jdk`
- `sdk`

### 3.需求分析
 1. 提供用户查看特定地区天气信息和生活建议
 2. 采用灵活方式实现用户获取到的数据是实时的
 3. 设计良好用户界面，提高用户体验
### 4.设计

#### （1）数据库设计
  采用开源Android数据库框架LitePal,它采用对象关系映射(ORM)的模式，并将我们平时最常用到的一些数据库功能进行了封装。使得不用编写SQL语句就可以完成各种建表和增删改查的操作。

  

    Province数据表
| 属性        | 数据类型   |  说明  |
| :-----:   | :-----:  | :----:  |
| `id`     | `integer` |   自增主键     |
| `provinceName`       |   `text`   |  省份名称    |
| `provinceCode`       |    `integer` |  省份代号  |


    City数据表
| 属性        | 数据类型   |  说明  |
| :-----:   | :-----:  | :----:  |
| `id`     | `integer` |   自增主键     |
| `cityName`       |   `text`   |  城市名称    |
| `cityCode`       |    `integer` |  城市代号  |
| `provinceId`       |    `integer` |  当前市属省的id值  |


    County数据表
| 属性        | 数据类型   |  说明  |
| :-----:   | :-----:  | :----:  |
| `id`     | `integer` |   自增主键     |
| `countyName`       |   `text`   |  县名称    |
| `countyCode`       |    `integer` |  县代号  |
| `cityId`       |    `integer` |  当前县属市的id值  |


#### （2）功能模块

从功能需求分析可以看出，整个应用程序应该分为4个模块。分别是**用户界面**，**数据获取模块**和**数据库适配器**和**网络请求模块**。各模块之间的关系如图：

```graphTD
  B -->|startService 启动服务| I(后台服务)
  I -->|八小时更新|H
  B[用户界面]-->|设置相关信息| G(</Br>数据获取模块)
  G-->|天气信息| B
  G-->D[数据库获取模块]
  D-->G
  D-->E[Litepal数据库框架]
  E-->D
  H[okhttp3封装Http请求]-->|解析返回天气数据信息|G
  H-->E
  F[和风天气预报服务]-->H
```
---
从模块结构图中不难看出，数据获取模块是整个应用程序的核心。主要是由`sharePreferencr`构成。获取数据信息从`okhttp3`**网络请求模块**中获取数据和服务每八小时**网络请求模块**获取一次数据。用户**设置相关信息**获取信息，*一种*从**数据获取模块**中寻找数据，*第二种*直接通过`okhttp3`**网络请求模块**请求获取数据并存储在**数据获取模块**中，在从**数据获取模块**中获取数据，能保证天气数据实时，且能保证网络阻塞情况下，能从**数据获取模块**获得数据。`startService`保证长时间未更新数据情况下，采用上述*第二种*方式从**数据获取模块**获得实时数据。**数据获取模块**是一种较良好方式从`Http请求`中获得实时数据的一种存储方式。**数据库适配器**使用`Litepal`数据库框架映射数据库。存储地区信息等比较固定的信息，避免频繁从**网络请求模块**中获取数据。

### 5.项目功能描述
#### （1）系统主要功能
1. 点击特定省份，城市和县来查看特定地区实时天气信息和服务信息
2. 下拉刷新获取实时的天气信息和服务信息
3. 后台服务，八小时自动跟新实时天气和服务信息
4. 数据库存储地区信息，消除频繁去服务区获取数据
5. sharePreference存储天气信息，获取实时天气信息
6. 获取每日一图，每日更换天气界面背景，获得好的用户体验

#### （2）系统各模块具体实现
     工程源代码的结构如图
![项目结构](http://bruce.u.qiniudn.com/2013/11/27/reading/photos-0.jpg)

     源代码文件的名称以及说明：
| 包名称        | 文件名   |  说明  |
| :-----:   | :-----:  | :----:  |
| `com.example.a13612.weather.db`   | `City.java` |   数据库表City映射模型     |
| `com.example.a13612.weather.db`   |  `County.java`|  数据库表County映射模型    |
| `com.example.a13612.weather.db`   | `Province.java` |  数据库表Province映射模型  |
| `com.example.a13612.weather.gson` |  `AQI.java` |  天气数据`json`数据格式解析模型  |
| `com.example.a13612.weather.gson` |  `Basic.java` | 天气数据`json`数据格式解析模型 |
| `com.example.a13612.weather.gson` |`Forecast.java` | 天气数据`json`数据格式解析模型|
| `com.example.a13612.weather.gson` |  `Now.java` |  天气数据`json`数据格式解析模型  |
| `com.example.a13612.weather.gson`|`Suggestion.java`| 天气数据`json`数据格式解析模型|
|`com.example.a13612.weather.gson` |`Weather.java`|  天气数据`json`数据格式解析模型  |


    对应的json数据格式，采用gson解析

``` json
  {
    "HeWeather": [
    {
      "status":"ok",         //响应码
      "basic":{},            //地区信息
      "aqi":{},              //空气质量
      "now":{},              //今天天气预报
      "suggestion": {}       //生活建议
      "daily_forecast":[]    //未来几天天气预报
    }
    ]
  }
```



| 包名称        | 文件名   |  说明  |
| :-----:   | :-----:  | :----:  |
| `com.example.a13612.weather.service` |  `AutoUpdateService.java` |  后台服务类，每八小时更新数据信息  |
| `com.example.a13612.weather.utils`|`HttpUtil.java`| 服务器交互工具类，采用`okhttp3`网络通信库|
|`com.example.a13612.weather.utils` |`Utility.java`|  解析和处理从服务器返回`json`数据工具类  |
|`com.example.a13612.weather.ChooseAreaFragment` |`ChooseAreaFragment.java`|  选择地区，来确定需要哪个地区天气信息  |
|`com.example.a13612.weather.WeatherActivity` |`WeatherActivity.java`|  设置显示天气信息，加载天气界面  |
|`com.example.a13612.weather.MainActivity` |`MainActivity.java`|  主活动，通过查看sharePreference是否存在数据来加载具体类,布局  |

    Android的资源文件保存在/res的子目录中。其中,/res/layout/保存布局，/res/drawable/保存图片等信息    

| 资源目录   | 文件   |  说明  |
| :-----:   | :-----:  | :----:  |
| `layout` |  `activity_main.xml` |  复用碎片`ChooseAreaFragment`  |
| `layout` |  `activity_weather.xml` |  天气界面，inlucde多个布局文件，引入下拉，opendraw  |
| `layout` |  `aqi.xml` |  空气质量布局  |
| `layout` |  `choose_area.xml` |  选择地区布局  |
| `layout` |  `forecast.xml` |  多个天气预报布局  |
| `layout` |  `forecast_item.xml` |  单个天气预报item  |
| `layout` |  `now.xml` |  当前天气预报布局  |
| `layout` |  `suggestion.xml` |  生活建议不  |
| `layout` |  `title.xml` |  标题框布局  |
| `drawable` |  `ic_back.png` |  返回上一级地区按钮图片  |
| `drawable` |  `ic_home.png` |  实现在天气界面打开侧框选择地区界面  |

    配置文件
    
    <1>项目所需的各种依赖库声明，具体配置文件app/build.gradle

``` gradle
  deplencies{
        implementation 'org.litepal.android:core:1.4.1'
        implementation 'com.squareup.okhttp3:okhttp:3.4.1'
        implementation 'com.google.code.gson:gson:2.7'
        implementation 'com.github.bumptech.glide:glide:3.7.0'
        }
```
    作用：LitePal用于对数据库操作，OkHttp用于进行网络请求，GSON用于解析JSON数据，Glide用于加载图片和展示图片
    
    <2>LitePal配置文件，用于匹配映射数据库模型类，位于assets/litepal.xml
``` xml
  <litepal>
    <dbname value="weather" />
    <version value="1" />
    <list>
        <mapping class="com.example.a13612.weather.db.Province" />
        <mapping class="com.example.a13612.weather.db.City" />
        <mapping class="com.example.a13612.weather.db.County" />
    </list>
</litepal>
```
     作用：建立数据库：weather，创建数据库表Province,City,County
     
     <3>AndroidManifest.xml,进行权限声明，活动,服务注册，LitePal声明
``` xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.a13612.weather">
    <!--访问网络权限-->
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        <!--LitePal声明-->
        android:name="org.litepal.LitePalApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        <!--设置主题为自己定义AppTheme主题，具体配置AppTheme请看res/values/styles.xml-->
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <!-- 指定当前活动能够响应的action和category -->
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".WeatherActivity"></activity>
        <service
            android:name=".service.AutoUpdateService"
            android:enabled="true"
            android:exported="true"></service>
    </application>

</manifest>
```

### 6.数据提供者

    <1> 地区信息提供url接口：
     http://guolin.tech/api/china 提供所有省份信息
     http://guolin.tech/api/china/"+provinceCode 根据省份信息查找特定城市信息
     http://guolin.tech/api/china/"+provinceCode+"/"+cityCode 根据省份信息和特定城市信息查找特定地区信息
     
    <2>和风天气数据信息提供urL接口：
    "http://47.90.126.26/api/weather?cityid="+weatherId+"&key=de25d114b80e43ed907cc1db97335395"
    weatherId：具体特定地区信息提供
    key:http://guolin.tech/api/weather/register注册获取key
    
    <3>每日一图提供url接口：
    http://guolin.tech/api/bing_pc
### 7.系统界面

​    


​    




