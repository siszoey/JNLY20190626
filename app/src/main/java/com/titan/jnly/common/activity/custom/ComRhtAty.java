package com.titan.jnly.common.activity.custom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.common.uitl.RichTxtUtil;
import com.titan.jnly.pubser.bean.ItemContent;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

public class ComRhtAty extends BaseMvpCompatAty {

    public static void start(Activity activity, ItemContent item) {
        Intent intent = new Intent(activity, ComRhtAty.class);
        ComRhtAty.title = item.getPoliciesRegulation().getTitle();
        ComRhtAty.content = item.getPoliciesRegulation().getPublishContent();
        activity.startActivity(intent);
    }

    //private static
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
        content = RichTxtUtil.appendRichTxtImgUrl(content, Config.BASE_URL.BaseRich_Url);
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