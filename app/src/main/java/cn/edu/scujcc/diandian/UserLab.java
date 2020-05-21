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
        Call<Result<String>> call = api.login(username, password);
        call.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                boolean loginSuccess = false;
                String token = null;
                if (response.body() != null) {
                    Log.d(TAG, "服务器返回结果" + response.body());
                    Result<String> result = response.body();
                    if (result.getStatus() == Result.OK) {  //登录成功
                        loginSuccess = true;
                        token = result.getData();
                    }
                }
                if (loginSuccess) {
                    Message msg = new Message();
                    msg.what = USER_LOGIN_SUCCESS;
                    msg.obj = token;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = USER_LOGIN_PASSWORD_ERROR;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                Log.e(TAG, "登录失败！", t);
                Message msg = new Message();
                msg.what = USER_LOGIN_NET_ERROR;
                handler.sendMessage(msg);
            }
        });
    }

    public void register(User user, Handler handler) {
        Log.d(TAG, "准备向服务器注册用户：" + user);
    }
}
