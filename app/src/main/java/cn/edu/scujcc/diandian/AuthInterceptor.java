package cn.edu.scujcc.diandian;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private MyPreference prefs = MyPreference.getInstance();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request authRequest = originalRequest.newBuilder()
                .addHeader("token", prefs.currentToken())
                .build();
        return chain.proceed(authRequest);
    }
}
