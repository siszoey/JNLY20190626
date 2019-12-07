package com.lib.bandaid.data.remote.intercept;

import com.lib.bandaid.data.remote.mock.annotation.DeleteMapping;
import com.lib.bandaid.data.remote.mock.annotation.GetMapping;
import com.lib.bandaid.data.remote.mock.annotation.PatchMapping;
import com.lib.bandaid.data.remote.mock.annotation.PostMapping;
import com.lib.bandaid.data.remote.mock.annotation.PutMapping;
import com.lib.bandaid.data.remote.mock.annotation.RequestMapping;
import com.lib.bandaid.data.remote.util.MappingMethod;
import com.lib.bandaid.data.remote.util.MappingRequest;
import com.lib.bandaid.data.remote.util.ReflectUtil;
import com.lib.bandaid.data.remote.util.ResponseUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 静态Mapping拦截器
 */
public class DispatchStaticIpt implements Interceptor {

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private static DispatchStaticIpt dispatch;

    public static DispatchStaticIpt getInstance() {
        if (dispatch == null) {
            synchronized (DispatchStaticIpt.class) {
                if (dispatch == null) {
                    dispatch = new DispatchStaticIpt();
                }
            }
        }
        return dispatch;
    }

    private DispatchStaticIpt() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Map<String, Method> map = MappingMethod.getInstance().getRequestMapping();
        for (String mappingUrl : map.keySet()) {
            Method method = map.get(mappingUrl);
            RequestMapping classMapping = null;
            Class<?> clazz = method.getDeclaringClass();
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                classMapping = clazz.getAnnotation(RequestMapping.class);
            }
            Response response = null;
            Object methodMapping = null;
            if (method.isAnnotationPresent(GetMapping.class)) {
                methodMapping = method.getAnnotation(GetMapping.class);
                response = handleResponse(chain, method, mappingUrl, classMapping, methodMapping);
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                methodMapping = method.getAnnotation(PostMapping.class);
                response = handleResponse(chain, method, mappingUrl, classMapping, methodMapping);
            }
            if (method.isAnnotationPresent(PutMapping.class)) {
                methodMapping = method.getAnnotation(PutMapping.class);
                response = handleResponse(chain, method, mappingUrl, classMapping, methodMapping);
            }
            if (method.isAnnotationPresent(DeleteMapping.class)) {
                methodMapping = method.getAnnotation(DeleteMapping.class);
                response = handleResponse(chain, method, mappingUrl, classMapping, methodMapping);
            }
            if (method.isAnnotationPresent(PatchMapping.class)) {
                methodMapping = method.getAnnotation(PatchMapping.class);
                response = handleResponse(chain, method, mappingUrl, classMapping, methodMapping);
            }
            if (response != null) return response;
        }
        System.out.println("该请求由远程服务处理: " + chain.request().url().toString());
        return ResponseUtil.createDefault(chain);
    }

    private Response handleResponse(
            Chain chain,
            Method method,
            String mappingUrl,
            RequestMapping classMapping,
            Object methodMapping
    ) {
        Request request = chain.request();
        String requestUrl = request.url().toString();
        MappingRequest mappingRequest = MappingRequest.create(chain, mappingUrl);
        boolean flag = mappingRequest.isMatch();
        if (flag) {
            Object[] objects = MappingRequest.nullParams(method);
            if (METHOD_GET.equals(request.method())) {
                objects = mappingRequest.analysisGetParams(method);
            }
            if (METHOD_POST.equals(request.method())) {
                objects = mappingRequest.analysisPostParams(method);
            }
            Object obj = ReflectUtil.invoke(method, objects);
            System.out.println("该请求由本地服务处理: " + requestUrl);
            return ResponseUtil.createSmart(chain, methodMapping, 200, "本地服务器", obj);
        }
        return null;
    }
}
