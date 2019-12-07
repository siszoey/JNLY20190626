package com.lib.bandaid.data.remote.util;

import com.lib.bandaid.data.remote.mock.annotation.DeleteMapping;
import com.lib.bandaid.data.remote.mock.annotation.GetMapping;
import com.lib.bandaid.data.remote.mock.annotation.PatchMapping;
import com.lib.bandaid.data.remote.mock.annotation.PostMapping;
import com.lib.bandaid.data.remote.mock.annotation.PutMapping;
import com.lib.bandaid.util.ObjectUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * okHttp的Response构建
 */
public final class ResponseUtil {

    private static String CONTENT_TYPE = "content-type";

    public static Response createString(int status, String msg, Interceptor.Chain chain, String content) {
        return new Response.Builder()
                .code(status)
                .message(msg)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaTypeUtil.APPLICATION_JSON, content))
                .addHeader(CONTENT_TYPE, MediaTypeUtil.APPLICATION_JSON_VALUE)
                .build();
    }

    public static Response createObject(int status, String msg, Interceptor.Chain chain, String content_type, Object content) {
        if (content_type.contains("json") || content_type.contains("text")) {
            return new Response.Builder()
                    .code(status)
                    .message(msg)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse(content_type), (String) content))
                    .addHeader(CONTENT_TYPE, content_type)
                    .build();
        } else {
            if (content == null) content = new byte[0];
            ResponseBody body = ResponseBody.create(MediaType.parse(content_type), (byte[]) content);
            Response response = new Response.Builder()
                    .code(status)
                    .message(msg)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(body)
                    .addHeader(CONTENT_TYPE, content_type)
                    .build();
            return response;
        }
    }

    public static Response createDefault(Interceptor.Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }

    public static Response createSmart(

            Interceptor.Chain chain,
            Object methodMapping,
            int status,
            String msg,
            Object content

    ) {

        String name;
        String[] consumes, value, headers, params, produces, path;
        if (methodMapping instanceof GetMapping) {
            name = ((GetMapping) methodMapping).name();
            consumes = ((GetMapping) methodMapping).consumes();
            value = ((GetMapping) methodMapping).value();
            headers = ((GetMapping) methodMapping).headers();
            params = ((GetMapping) methodMapping).params();
            path = ((GetMapping) methodMapping).path();
            produces = ((GetMapping) methodMapping).produces();
        } else if (methodMapping instanceof PostMapping) {
            name = ((PostMapping) methodMapping).name();
            consumes = ((PostMapping) methodMapping).consumes();
            value = ((PostMapping) methodMapping).value();
            headers = ((PostMapping) methodMapping).headers();
            params = ((PostMapping) methodMapping).params();
            path = ((PostMapping) methodMapping).path();
            produces = ((PostMapping) methodMapping).produces();
        } else if (methodMapping instanceof PutMapping) {
            name = ((PutMapping) methodMapping).name();
            consumes = ((PutMapping) methodMapping).consumes();
            value = ((PutMapping) methodMapping).value();
            headers = ((PutMapping) methodMapping).headers();
            params = ((PutMapping) methodMapping).params();
            path = ((PutMapping) methodMapping).path();
            produces = ((PutMapping) methodMapping).produces();
        } else if (methodMapping instanceof DeleteMapping) {
            name = ((DeleteMapping) methodMapping).name();
            consumes = ((DeleteMapping) methodMapping).consumes();
            value = ((DeleteMapping) methodMapping).value();
            headers = ((DeleteMapping) methodMapping).headers();
            params = ((DeleteMapping) methodMapping).params();
            path = ((DeleteMapping) methodMapping).path();
            produces = ((DeleteMapping) methodMapping).produces();
        } else if (methodMapping instanceof PatchMapping) {
            name = ((PatchMapping) methodMapping).name();
            consumes = ((PatchMapping) methodMapping).consumes();
            value = ((PatchMapping) methodMapping).value();
            headers = ((PatchMapping) methodMapping).headers();
            params = ((PatchMapping) methodMapping).params();
            path = ((PatchMapping) methodMapping).path();
            produces = ((PatchMapping) methodMapping).produces();
        } else {
            name = null;
            consumes = null;
            value = null;
            headers = null;
            params = null;
            path = null;
            produces = null;
        }
        if (ObjectUtil.isEmpty(produces)) {
            String res = ObjectUtil.convert(content);
            return createString(status, msg, chain, res);
        } else {
            return createObject(status, msg, chain, produces[0], content);
        }
    }

}
