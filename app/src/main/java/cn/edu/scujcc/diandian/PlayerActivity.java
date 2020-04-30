package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Retrofit;

public class PlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private Channel currentChannel;
    private TextView tvName, tvQuality;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ChannelLab lab = ChannelLab.getInstance();
    //TODO 完成接收到数据后更新界面的代码
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 2) {
                if (msg.obj != null) {
                    List<Comment> hotComments = (List<Comment>) msg.obj;
                    //更新界面
                    if (hotComments.size() > 0) {
                        Comment c1 = hotComments.get(0);
                        TextView username1 = findViewById(R.id.username1);
                        username1.setText(c1.getAuthor());
                        TextView date1 = findViewById(R.id.date1);
                        date1.setText(dateFormat.format(c1.getDt()));
                        TextView content1 = findViewById(R.id.content1);
                        content1.setText(c1.getContent());
                        TextView star1 = findViewById(R.id.thumbup_count1);
                        star1.setText(c1.getStar() + "");
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Serializable s = getIntent().getSerializableExtra("channel");
        Log.d("DianDian", "取得的当前频道对象是：" + s);
        if (s != null && s instanceof Channel) {
            currentChannel = (Channel) s;
        }
        updateUI();
    }

    private void updateUI() {
        tvName = findViewById(R.id.tv_name);
        tvQuality = findViewById(R.id.tv_quality);
        tvName.setText(currentChannel.getTitle());
        tvQuality.setText(currentChannel.getQuality());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    protected void onStart() {
        super.onStart();
        init();
        if (playerView != null) {
            playerView.onResume();
        }
    }

    protected void onStop() {
        super.onStop();
        if (playerView != null) {
            playerView.onPause();
        }
        clean();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player == null) {
            init();
            if (playerView != null) {
                playerView.onResume();
            }
        }
        //1.获取最新热门评论
        lab.getHotComments(currentChannel.getId(), handler);
    }

    /**
     * 自定义方法，初始化播放器
     */
    private void init() {
        player = ExoPlayerFactory.newSimpleInstance(this);
        player.setPlayWhenReady(true);
        //从界面查找视图
        playerView = findViewById(R.id.tv_player);
        //关联视图与播放器
        playerView.setPlayer(player);
        //准备播放的媒体
        Uri videoUrl = Uri.parse("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        if (null != currentChannel) {
            //使用当前频道的网址
            videoUrl = Uri.parse(currentChannel.getUrl());
        }
        DataSource.Factory factory =
                new DefaultDataSourceFactory(this, "DianDian");
        MediaSource videoSource = new HlsMediaSource.Factory(factory).createMediaSource(videoUrl);
        player.prepare(videoSource);
    }

    /**
     * 自定义方法，清理不用的资源
     */
    private void clean() {
        if (player != null) {
            player.release();
            player = null;
        }

        Retrofit b = RetrofitClient.getInstance();
    }
}
