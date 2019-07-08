package com.titan.jnly.common.uitls;

import android.annotation.SuppressLint;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Constant {

    public static final int PICK_PHOTO = 0x000003;
    public static final int PICK_AUDIO = 0x000004;
    public static final int PICK_VIDEO = 0x000005;
    public static final int PICK_PEOPLE = 0x000006;

    public static final String PREFS_NAME = "MYSP";

    public static DecimalFormat disFormat = new DecimalFormat("0.00");
    public static DecimalFormat sixFormat = new DecimalFormat("0.000000");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");


    /*String 字符串保留两位小数*/
    public static String strFormat(String value){
        return Constant.disFormat.format(new BigDecimal(value));
    }

    public static RequestBody requestBody(Object obj) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(obj));
    }

}
