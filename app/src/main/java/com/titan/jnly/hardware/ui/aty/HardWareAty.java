package com.titan.jnly.hardware.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lib.bandaid.activity.BaseAppCompatActivity;

import com.lib.bandaid.utils.SPfUtil;
import com.supoin.rfidservice.sdk.DataUtils;
import com.supoin.rfidservice.sdk.ModuleController;
import com.titan.jnly.R;
import com.titan.jnly.hardware.utils.CodeUtil;

import java.util.ArrayList;
import java.util.List;

public class HardWareAty extends BaseAppCompatActivity implements View.OnClickListener {

    private Spinner sp_gonglu;
    private TextView tv_readcontent;
    private EditText ed_writecontent;
    private ModuleController moduleController;
    private Button btn_write, btn_read, btn_gonglu;
    private List<String> gonglu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "电子标签读写", Gravity.CENTER);
        setContentView(R.layout.hardware_ui_aty_read_and_write);
    }

    @Override
    protected void initialize() {
        btn_write = $(R.id.btn_write);
        btn_read = $(R.id.btn_read);
        btn_gonglu = $(R.id.btn_gonglu);
        tv_readcontent = $(R.id.tv_readcontent);
        ed_writecontent = $(R.id.ed_writecontent);
        sp_gonglu = $(R.id.sp_gonglu);
    }

    @Override
    protected void registerEvent() {
        btn_read.setOnClickListener(this);
        btn_write.setOnClickListener(this);
        btn_gonglu.setOnClickListener(this);
    }

    @Override
    protected void initClass() {
        initWidget();
        initModule();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_read) {
            moduleController.moduleReadTag(DataUtils.BANK_EPC, 2, 6, null, null);
        }
        if (id == R.id.btn_write) {
            if (ed_writecontent.getText().toString().trim().length() == 11) {
                String text = ed_writecontent.getText().toString().trim();
                SPfUtil.putT(_context, "str", text);
                moduleController.moduleWriteTag(DataUtils.BANK_EPC, 2, DataUtils.hexStrToByte(CodeUtil.str2HexStr(ed_writecontent.getText().toString().trim() + "0").replace(" ", "")), null, null);
            } else {
                showLongToast("请输入正确的格式");
            }
        }
        if (id == R.id.btn_gonglu) {
            moduleController.moduleSetParameters(DataUtils.PARA_POWER, Integer.parseInt(sp_gonglu.getSelectedItem() + ""));
        }
    }

    private void initWidget() {
        for (int i = 5; i < 31; i++) {
            gonglu.add(i + "");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gonglu);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gonglu.setAdapter(arrayAdapter);
        String str = SPfUtil.readT(_context, "str");
        str = str == null ? "" : str;
        if (!str.equals("")) {
            ed_writecontent.setText(str);
        }
    }

    private void initModule() {
        moduleController = ModuleController.getInstance(this, new ModuleController.DataListener() {

            @Override
            public void onServiceStarted() {
                super.onServiceStarted();
                showLongToast("服务启动成功");
            }

            @Override
            public void onError() {
                super.onError();
                showLongToast("模块不存在");
            }

            @Override
            public void onConnect(boolean isSuccess) {
                super.onConnect(isSuccess);
                showLongToast("连接成功");
            }

            @Override
            public void onDisConnect(boolean isSuccess) {
                super.onDisConnect(isSuccess);
                showLongToast("断开连接");
            }

            @Override
            public void onSetParameters(int paraKey, boolean isSuccess) {
                super.onSetParameters(paraKey, isSuccess);
                if (isSuccess) {
                    showLongToast("成功");
                } else {
                    showLongToast("失败");
                }
            }

            @Override
            public void onReadTag(byte[] tagData) {
                super.onReadTag(tagData);
                if (tagData.length != 0) {
                    tv_readcontent.setText(CodeUtil.hexStr2Str(DataUtils.byteToHexStr(tagData)).substring(0, CodeUtil.hexStr2Str(DataUtils.byteToHexStr(tagData)).length() - 1));
                }
            }

            @Override
            public void onWriteTag(boolean isSuccess) {
                super.onWriteTag(isSuccess);
                if (isSuccess) {
                    showLongToast("写入成功");
                } else {
                    showLongToast("写入失败");
                }
            }
        });
    }

}
