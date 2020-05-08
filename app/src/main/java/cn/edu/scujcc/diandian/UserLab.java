package cn.edu.scujcc.diandian;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserLab {
    public final static int USER_LOGIN_SUCCESS = 1;
    public final static int USER_LOGIN_PASSWORD_ERROR = -1;
    public final static int USER_LOGIN_NET_ERROR = -2;
    private final static String TAG = "DianDian";
    private static UserLab INSTANCE = null;

    private UserLab() {
    }

    public static UserLab getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new UserLab();
        }
        return INSTANCE;
    }

    public void login(String username, String password, Handler handler) {
        Retrofit retrofit = RetrofitClient.getInstance();
        UserApi api = retrofit.create(UserApi.class);
        Call<Integer> call = api.login(username, password);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d(TAG, "登录成功返回数据");
                boolean loginSuccess = false;
                if (response.body() != null) {
                    int result = response.body().intValue();
                    if (result == 1) {  //登录成功
                        loginSuccess = true;
                    }
                }
                if (loginSuccess) {
                    Message msg = new Message();
                    msg.what = USER_LOGIN_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = USER_LOGIN_PASSWORD_ERROR;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "登录失败！", t);
                Message msg = new Message();
                msg.what = USER_LOGIN_NET_ERROR;
                handler.sendMessage(msg);
            }
        });
    }
}
