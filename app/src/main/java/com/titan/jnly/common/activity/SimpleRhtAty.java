package com.titan.jnly.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.titan.jnly.R;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

/**
 * 富文本显示框，主要用于新闻展示
 */
public class SimpleRhtAty extends BaseMvpCompatAty {

    public static void start(Activity activity, String title, String content) {
        Intent intent = new Intent(activity, SimpleRhtAty.class);
        SimpleRhtAty.title = title;
        SimpleRhtAty.content = content;
        activity.startActivity(intent);
    }

    private static String title;
    private static String content;
    private TextView tvRich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, title, Gravity.CENTER);
        RichText.initCacheDir(this);
        RichText.debugMode = true;
        setContentView(R.layout.com_aty_rtf_layout);
    }

    @Override
    protected void initialize() {
        tvRich = $(R.id.tvRich);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        RichText.from(content)
                .autoFix(true) // 是否自动修复，默认true
                .autoPlay(true) // gif图片是否自动播放
                .showBorder(true) // 是否显示图片边框
                .borderColor(Color.RED) // 图片边框颜色
                .scaleType(ImageHolder.ScaleType.fit_center) // 图片缩放方式
                .urlClick(new OnUrlClickListener() {
                    @Override
                    public boolean urlClicked(String url) {
                        //拦截特殊url，自定义处理
                        if (url.startsWith("code://")) {
                            return true;
                        }
                        return false;
                    }
                })
                .into(tvRich);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.recycle();
    }
}
