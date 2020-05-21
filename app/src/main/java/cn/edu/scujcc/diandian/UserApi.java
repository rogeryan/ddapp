package cn.edu.scujcc.diandian;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @GET("/user/login/{username}/{password}")
    Call<Result<String>> login(@Path("username") String username, @Path("password") String password);

    /**
     * 用户注册
     *
     * @param user 用户填写的信息
     * @return
     */
    @POST("/user/register")
    Call<User> register(@Body User user);

}
