package com.titan.jnly.common.mvp;

import android.content.Context;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.data.remote.com.NetEasyFactory;
import com.lib.bandaid.data.remote.com.NetEasyReq;
import com.lib.bandaid.data.remote.core.DownloadManager;
import com.lib.bandaid.data.remote.entity.DownloadInfo;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.DownWorkListen;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.util.CodeUtil;
import com.titan.jnly.Config;
import com.titan.jnly.login.api.ApiLogin;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.system.Constant;

import java.io.File;
import java.util.Date;

public class DicHandle {

    public static DicHandle create(Context context) {
        return new DicHandle(context);
    }

    private BaseMvpCompatAty aty;
    private NetEasyReq netEasyReq;

    private DicHandle(Context context) {
        netEasyReq = NetEasyFactory.createEasy(context);
        if (context instanceof BaseMvpCompatAty) {
            aty = (BaseMvpCompatAty) context;
        }
    }

    public void reqDic(User user) {
        netEasyReq.request(ApiLogin.class, new NetWorkListen<TTResult<UserInfo>>() {
            @Override
            public void onSuccess(TTResult<UserInfo> data) {
                UserInfo info = data.getContent();
                //更新账户及权限
                if (info != null) {
                    if (!info.localCheck()) info.setLastLogin(new Date());
                    String md5 = CodeUtil.convertMd5(Constant.getUser().getPwd());
                    info.setPwd(md5);
                    DbManager.createDefault().saveOrUpdate(info);
                    Constant.putUserInfo(info);
                    aty.showLongToast("账号权限更新成功!");
                }
                aty.dialogLoading();
                //更新字典
                String path = Config.APP_PATH_DIC.concat(File.separator).concat("dic_new.db");
                FileUtil.deleteFile(path);
                DownloadInfo down = new DownloadInfo(Config.BASE_URL.Sync_Dic, path);
                down.setListen(new DownWorkListen() {
                    @Override
                    public void progress(DownloadInfo info) {
                        if (info.isStart()) {
                            aty.dialogLoading();
                        }
                        if (info.isComplete()) {
                            String old = Config.APP_DIC_DB_PATH;
                            boolean del = FileUtil.deleteFile(old);
                            if (del) del = FileUtil.rename(info.getFilePath(), old);
                            if (del) aty.showLongToast("字典库更新成功");
                            else aty.showLongToast("字典库更新失败");
                            Constant.reloadSpecies();
                            aty.dialogHiding();
                        }
                        if (info.overButUnComplete()) {
                            aty.showLongToast("字典库更新失败");
                            aty.dialogHiding();
                        }
                    }
                });
                DownloadManager.getInstance().download(down);
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                System.out.println(errMsg);
            }
        }).httpLogin(user.getName(), user.getPwd());
    }
}
