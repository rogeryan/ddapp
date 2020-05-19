package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import retrofit2.Retrofit;

public class PlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private Channel currentChannel;
    private TextView tvName, tvQuality;
    private final static String TAG = "DianDian";
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ChannelLab lab = ChannelLab.getInstance();
    private MyPreference prefs = MyPreference.getInstance();
    private ImageButton sendButton;
    //TODO 完成接收到数据后更新界面的代码
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ChannelLab.MSG_HOT_COMMENTS:
                    if (msg.obj != null) {
                    List<Comment> hotComments = (List<Comment>) msg.obj;
                        updateHotComments(hotComments);
                }
                    break;
                case ChannelLab.MSG_ADD_COMMENT:
                    Toast.makeText(PlayerActivity.this, "感谢您的留言！",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
                case ChannelLab.MSG_FAILURE:
                    Toast.makeText(PlayerActivity.this, "评论失败，请稍候再试！",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Serializable s = getIntent().getSerializableExtra("channel");
        Log.d(TAG, "取得的当前频道对象是：" + s);
        if (s != null && s instanceof Channel) {
            currentChannel = (Channel) s;

            sendButton = findViewById(R.id.send);
            sendButton.setOnClickListener(v -> {
                EditText t = findViewById(R.id.comment);
                Comment c = new Comment();
                c.setAuthor("MyApp");
                c.setContent(t.getText().toString());
                //改进，随机点赞(0至100)
                Random random = new Random();
                c.setStar(random.nextInt(100));
                //调用retrofit上传评论
                lab.addComment(currentChannel.getId(), c, handler);
            });
        }
        updateUI();
    }

    private void updateUI() {
        tvName = findViewById(R.id.tv_name);
        tvQuality = findViewById(R.id.tv_quality);
        tvName.setText(currentChannel.getTitle());
        tvQuality.setText(currentChannel.getQuality());
        //读取当前用户名并显示
        TextView currentUser = findViewById(R.id.current_user);
        currentUser.setText(prefs.currentUser());
    }

    private void updateHotComments(List<Comment> hotComments) {
        if (hotComments != null && hotComments.size() > 0) {
            Comment c = hotComments.get(0);
            TextView username1 = findViewById(R.id.username1);
            username1.setText(c.getAuthor());
            TextView date1 = findViewById(R.id.date1);
            date1.setText(dateFormat.format(c.getDt()));
            TextView content1 = findViewById(R.id.content1);
            content1.setText(c.getContent());
            TextView star1 = findViewById(R.id.thumbup_count1);
            star1.setText(c.getStar() + "");
        }
        if (hotComments != null && hotComments.size() > 1) {
            Comment c = hotComments.get(1);
            TextView username1 = findViewById(R.id.username2);
            username1.setText(c.getAuthor());
            TextView date1 = findViewById(R.id.date2);
            date1.setText(dateFormat.format(c.getDt()));
            TextView content1 = findViewById(R.id.content2);
            content1.setText(c.getContent());
            TextView star1 = findViewById(R.id.thumbup_count2);
            star1.setText(c.getStar() + "");
        }
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
                new DefaultDataSourceFactory(this, TAG);
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
