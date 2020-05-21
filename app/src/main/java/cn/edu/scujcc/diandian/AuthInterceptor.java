package cn.edu.scujcc.diandian;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 认证拦截器。自动为所有发出的网络请求添加token。
 */
public class AuthInterceptor implements Interceptor {
    MyPreference prefs = MyPreference.getInstance();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request = originalRequest.newBuilder()
                .addHeader("token", prefs.currentToken())
                .build();
        return chain.proceed(request);
    }
}
