package cn.edu.scujcc.diandian;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * 使用单例模式创建Retrofit对象。
 */
public class RetrofitClient {
    private static Retrofit INSTANCE = null;

    public static Retrofit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Retrofit.Builder()
                    .baseUrl("http://47.115.34.11:8080")  //改为自己的阿里云服务器IP
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }
        return INSTANCE;
    }
}
