package cn.edu.scujcc.diandian;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChannelApi {
    @GET("/channel")
    Call<List<Channel>> getAllChannels();

    /**
     * 获取热门评论。
     *
     * @param channelId 频道编号
     * @return 热门评论的列表
     */
    @GET("/channel/{channelId}/hotcomments")
    Call<List<Comment>> getHotComments(@Path("channelId") String channelId);
}
