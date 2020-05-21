package cn.edu.scujcc.diandian;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request authRequest = originalRequest.newBuilder()
                .addHeader("token", "12345678")
                .build();
        return chain.proceed(authRequest);
    }
}
