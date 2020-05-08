package cn.edu.scujcc.diandian;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    @GET("/user/login/{username}/{password}")
    Call<Integer> login(@Path("username") String username, @Path("password") String password);
}
