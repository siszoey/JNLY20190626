package com.titan.jnly.patrol.util;

import com.camera.lib.widget.ImagePagerBean;
import com.lib.bandaid.data.remote.entity.TTFileResult;
import com.lib.bandaid.util.MapUtil;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.widget.collect.image.CollectImgBean;
import com.titan.jnly.Config;
import com.titan.jnly.Config_pro;
import com.titan.jnly.patrol.bean.CureModel;
import com.titan.jnly.patrol.bean.PatrolModel;

import java.util.List;
import java.util.Map;

/**
 * 处理移动端数据和服务器数据之间的转换
 */
public class DataConvertUtil {

    private final static String[] PATROL_SER_KEYS = new String[]{
            "Id", "LON", "LAT", "DZBQH", "XIAN", "XIANG", "CUN",
            "SZZWM", "YHXZ", "BHXZ", "YHXZ", "PatrolContent", "PatrolDep",
            "PatrolUser", "PatrolDate", "UserId",
            "SZS", "SZHJ", "PatrolLevel"
    };

    private final static String[] CURE_SER_KEYS = new String[]{
            "Id", "LON", "LAT", "DZBQH", "XIAN", "XIANG", "CUN",
            "SZZWM", "YHXZ", "BHXZ", "YHXZ", "PatrolContent", "PatrolDep",
            "BSPS", "YHSWFZ", "TRGL", "STBH", "HJGL", "SHIFEI",
            "PatrolUser", "PatrolDate", "UserId", "MaintainDep",
            "SZS", "SZHJ", "PatrolLevel"
    };

    public static Map<String, Object> convertPatrolSer2Local(PatrolModel model) {
        Map serMap = model.getPatrolRecord();
        List<TTFileResult> img = model.getPatrolImgs();
        Map record = MapUtil.itemClone(serMap, new MapUtil.ItemListen() {
            @Override
            public Object itemListen(String key, Object val) {
                if (key.equals("SZS") || key.equals("SZHJ") || key.equals("PatrolLevel")) {
                    if (val instanceof String) return val;
                    return ((Double) val).intValue() + "";
                }
                return val;
            }
        }, PATROL_SER_KEYS);

        List<CollectImgBean> beans = TTFileResult.convertImgBean(Config.BASE_URL.ImgScanService, img);
        String json = ObjectUtil.convert(beans, String.class);
        record.put("PatrolImgs", json);
        record.put("IS_EDIT", true);
        return record;
    }

    public static Map<String, Object> convertCureSer2Local(CureModel model) {
        Map serMap = model.getMaintainRecord();
        List<TTFileResult> img = model.getMaintainImgs();
        Map record = MapUtil.itemClone(serMap, new MapUtil.ItemListen() {
            @Override
            public Object itemListen(String key, Object val) {
                if (key.equals("SZS") || key.equals("SZHJ")) {
                    if (val instanceof String) return val;
                    return ((Double) val).intValue() + "";
                }
                return val;
            }
        }, CURE_SER_KEYS);

        List<CollectImgBean> beans = TTFileResult.convertImgBean(Config.BASE_URL.ImgScanService, img);
        String json = ObjectUtil.convert(beans, String.class);

        record.put("MaintainImgs", json);
        record.put("IS_EDIT", true);
        return record;
    }

}
