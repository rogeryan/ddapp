package cn.edu.scujcc.diandian;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChannelApi {
    @GET("/channel")
    Call<Result<List<Channel>>> getAllChannels();

    /**
     * 获取热门评论。
     *
     * @param channelId 频道编号
     * @return 热门评论的列表
     */
    @GET("/channel/{channelId}/hotcomments")
    Call<Result<List<Comment>>> getHotComments(@Path("channelId") String channelId);

    /**
     * 新增评论
     *
     * @param channelId 频道编号
     * @param comment   评论对象
     * @return 频道对象
     */
    @POST("/channel/{channelId}/comment")
    Call<Channel> addComment(@Path("channelId") String channelId, @Body Comment comment);
}
