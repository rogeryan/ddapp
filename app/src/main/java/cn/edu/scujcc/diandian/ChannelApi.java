package cn.edu.scujcc.diandian;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChannelApi {
    @GET("/channel")
    Call<List<Channel>> getAllChannels();
}
