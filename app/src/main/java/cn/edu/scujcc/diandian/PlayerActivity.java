package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.Serializable;

import retrofit2.Retrofit;

public class PlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private Channel currentChannel;
    private TextView tvName, tvQuality;
    private Button sendButton;
    private ChannelLab lab = ChannelLab.getInstance();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 2://显示热门评论
                    currentChannel = (Channel) msg.obj;
                    updateUI();
                    break;
                case 3://评论成功了，提示一下用户
                    Toast.makeText(PlayerActivity.this, "感谢您的留言！",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
                case 4:  //评论失败了，提示一下用户
                    Toast.makeText(PlayerActivity.this, "评论失败，请稍候再试。",
                            Toast.LENGTH_LONG)
                            .show();
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
            updateUI();
            sendButton = findViewById(R.id.send);
            sendButton.setOnClickListener(v -> {
                EditText t = findViewById(R.id.message);
                Comment c = new Comment();
                c.setAuthor("MyApp");
                c.setStar(1);
                c.setContent(t.getText().toString());
                lab.addComment(currentChannel.getId(), c, handler);
            });
        }
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
