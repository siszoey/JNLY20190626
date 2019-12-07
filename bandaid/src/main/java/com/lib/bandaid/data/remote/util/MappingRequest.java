package com.lib.bandaid.data.remote.util;

import com.lib.bandaid.data.remote.mock.annotation.PathVariable;
import com.lib.bandaid.data.remote.mock.annotation.RequestParam;
import com.lib.bandaid.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

public final class MappingRequest {

    private boolean isMatch = false;
    private Map<String, String> httpParam;
    private Request request;
    private Interceptor.Chain chain;
    private String requestUrl;
    private String urlExt;

    public static MappingRequest create(Interceptor.Chain chain, String mapping) {
        return new MappingRequest(chain, mapping);
    }

    private MappingRequest(Interceptor.Chain chain, String mapping) {
        this.chain = chain;
        this.request = chain.request();
        this.requestUrl = request.url().toString();
        this.urlExt = getUrlExt(requestUrl);
        this.urlExt = urlExt.equals("") ? "" : "." + urlExt;
        this.httpParam = handle(mapping);
    }

    /**
     * @param mapping
     */
    private Map<String, String> handle(String mapping) {
        this.isMatch = true;

        int reqSegmentCount = findCharCount(requestUrl, "/");
        int mapSegmentCount = findCharCount(mapping, "/");

        if (reqSegmentCount != mapSegmentCount) {
            this.isMatch = false;
            return new HashMap<>();
        }

        HttpUrl reqUrl = HttpUrl.parse(requestUrl);
        Map<String, String> param = new HashMap();
        //url里携带参数
        if (requestUrl.contains("?")) {
            Set<String> paramKeys = reqUrl.queryParameterNames();
            for (String key : paramKeys) {
                param.put(key, reqUrl.queryParameter(key));
            }
        }

        String temp1 = replace(requestUrl, "/", "?", "&");
        String temp2 = replace(mapping, "/", "?", "&");

        List<String> requestList = split(temp1, "/");
        List<String> mappingList = split(temp2, "/");

        String mapItem, reqItem;
        for (int i = 0; i < mappingList.size(); i++) {
            mapItem = mappingList.get(i);
            //处理有值类型
            if (mapItem.contains("{") && mapItem.endsWith("}")) {
                if (mapItem.contains("=")) {
                    param.putAll(matchItem(mapItem, requestList));
                } else {
                    reqItem = requestList.get(i);
                    if (i == mappingList.size() - 1) reqItem = reqItem.replace(urlExt, "");
                    param.put(mapItem.substring(1, mapItem.length() - 1), reqItem);
                }
            } else { //处理非值类型
                if (!mapItem.contains("=")) {
                    reqItem = requestList.get(i);
                    if (mapItem.equals(reqItem)) continue;
                } else {
                    if (requestList.contains(mapItem)) continue;
                }
                this.isMatch = false;
                break;
            }
        }
        if (!isMatch) param.clear();
        return param;
    }

    public boolean isMatch() {
        return isMatch;
    }

    private Map matchItem(String segment, List<String> list) {
        String[] mapArray, segmentArray;
        Map map = new HashMap();
        for (String item : list) {
            if (!item.contains("=")) continue;
            segmentArray = segment.split("=");
            mapArray = item.split("=");
            if (segmentArray[0].equals(mapArray[0])) {
                map.put(segmentArray[0], mapArray[1]);
                break;
            }
        }
        return map;
    }

    private String replace(String src, String replacement, String... targets) {
        for (String target : targets) {
            src = src.replace(target, replacement);
        }
        return src;
    }

    private List<String> split(String src, String split) {
        String[] res = src.split(split);
        List<String> list = Arrays.asList(res);
        return list;
    }

    /**
     * 在字符串里查询某个字符的数量
     *
     * @param srcText
     * @param findText
     * @return
     */
    public int findCharCount(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }


    public Object[] analysisGetParams(Method method) {
        Class<?>[] paramsType = method.getParameterTypes();
        Object[] objects = new Object[paramsType.length];
        List<Annotation[]> annotations = AnnotationUtil.getMethodParamsAnnotation(method);
        int index = 0;
        for (Annotation[] ans : annotations) {
            for (Annotation an : ans) {
                if (an.annotationType() == PathVariable.class) {
                    String flag = ((PathVariable) an).value();
                    String value = httpParam.get(flag);
                    objects[index] = ObjectUtil.convert(value, paramsType[index]);
                }
                if (an.annotationType() == RequestParam.class) {
                    String flag = ((RequestParam) an).value();
                    String value = httpParam.get(flag);
                    objects[index] = ObjectUtil.convert(value, paramsType[index]);
                }
            }
            index++;
        }
        return objects;
    }

    public Object[] analysisPostParams(Method method) {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        Class<?>[] paramsType = method.getParameterTypes();
        Object[] objects = new Object[paramsType.length];
        String contentType = requestBody.contentType().toString();
        if (contentType.startsWith("text/plain")
                || contentType.startsWith("application/json")) {
            String ss = reqBodyToTxt(requestBody);
            objects[0] = ObjectUtil.convert(ss, paramsType[0]);
        }
        return objects;
    }

    public static String reqBodyToTxt(RequestBody requestBody) {
        try {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object[] nullParams(Method method) {
        return new Object[method.getParameterTypes().length];
    }

    public static String getUrlExt(String extUrl) {
        String extension = "";
        String path = extUrl;
        String[] pathContents = path.split("[\\\\/]");
        if (pathContents != null) {
            int pathContentsLength = pathContents.length;
            String lastPart = pathContents[pathContentsLength - 1];
            String[] lastPartContents = lastPart.split("\\.");
            if (lastPartContents != null && lastPartContents.length > 1) {
                int lastPartContentLength = lastPartContents.length;
                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {
                    if (i < (lastPartContents.length - 1)) {
                        name += lastPartContents[i];
                        if (i < (lastPartContentLength - 2)) {
                            name += ".";
                        }
                    }
                }
                extension = lastPartContents[lastPartContentLength - 1];
            }
        }
        return extension;
    }
}
