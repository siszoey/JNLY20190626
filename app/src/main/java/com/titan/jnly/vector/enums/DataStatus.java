package com.titan.jnly.vector.enums;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.data.Feature;
import com.lib.bandaid.data.local.sqlite.utils.UUIDTool;
import com.lib.bandaid.utils.SimpleMap;

import java.util.Map;

public enum DataStatus {


    LOCAL_ADD("本地新增", (short) 0),
    LOCAL_EDIT("本地编辑", (short) 1),
    REMOTE_SYNC("远程同步", (short) 2);

    public final static String UUID = "UUID";
    public final static String DATA_STATUS = "DATA_STATUS";

    private final String name;
    private final short status;


    private DataStatus(String name, short status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public short getStatus() {
        return status;
    }

    /**
     * 根据枚举整型表达式获取枚举对象
     */
    public static DataStatus getEnum(short status) {
        DataStatus dbType = null;

        for (int i = 0; i < DataStatus.values().length; i++) {
            if (DataStatus.values()[i].getStatus()==status) {
                dbType = DataStatus.values()[i];
                break;
            }
        }
        return dbType;
    }
    /**
     * 根据枚举描名称获取枚举对象
     */
    public static DataStatus getEnum(String name) {
        DataStatus type = null;
        for (int i = 0; i < DataStatus.values().length; i++) {
            if (DataStatus.values()[i].getName().equalsIgnoreCase(name.trim())) {
                type = DataStatus.values()[i];
                break;
            }
        }
        return type;
    }

    public static Map createAdd() {
        return new SimpleMap().push("UUID", UUIDTool.get32UUID()).push("DATA_STATUS", DataStatus.LOCAL_ADD.getStatus());
    }

    public static Map createEdit() {
        return new SimpleMap().push("DATA_STATUS", DataStatus.LOCAL_EDIT.getStatus());
    }

    public static Map createSync() {
        return new SimpleMap().push("DATA_STATUS", DataStatus.REMOTE_SYNC.getStatus());
    }

    public static boolean isAdd(@NonNull Feature feature) {
        if (feature == null) return false;
        Map map = feature.getAttributes();
        short status = (short) map.get(DATA_STATUS);
        if (status == LOCAL_ADD.getStatus()) return true;
        return false;
    }

    public static boolean isEdit(@NonNull Feature feature) {
        if (feature == null) return false;
        Map map = feature.getAttributes();
        short status = (short) map.get(DATA_STATUS);
        if (status == LOCAL_EDIT.getStatus()) return true;
        return false;
    }

    public static boolean isSync(@NonNull Feature feature) {
        if (feature == null) return false;
        Map map = feature.getAttributes();
        short status = (short) map.get(DATA_STATUS);
        if (status == REMOTE_SYNC.getStatus()) return true;
        return false;
    }
}
