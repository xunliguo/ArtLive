package com.example.rh.artlive.live;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.BaseFragmentActivity;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import okhttp3.Call;

/**
 * Created by rh on 2017/12/14.
 */

public class LookActivity extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback {


    public static final String LIVE_URL = "http://live1.aiegi.cn/AppName/StreamName.flv";
    private String mRoom_Id="";
    private String mLive_Url="";

    private ViewGroup background;
    private ChatListView chatListView;
    private BottomPanelFragment bottomPanel;
    private ImageView btnGift;
    private ImageView btnHeart;
    private HeartLayout heartLayout;
    private SurfaceView surfaceView;

    private Random random = new Random();
    private Handler handler = new Handler(this);
    private ChatListAdapter chatListAdapter;
    private KSYMediaPlayer ksyMediaPlayer;
    private SurfaceHolder surfaceHolder;
    private static LiveGiftBean mGift;
    public static ArrayList<LiveGiftBean> giftBeen ;
    private GiftItemView giftView ;



    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            ksyMediaPlayer.start();
        }
    };

    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (ksyMediaPlayer != null && ksyMediaPlayer.isPlaying())
                ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (ksyMediaPlayer != null)
                ksyMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (ksyMediaPlayer != null) {
                ksyMediaPlayer.setDisplay(null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        LiveKit.addEventHandler(handler);
        initView();
        startLiveShow();
        //获取礼物数据
        setData();
    }

    private void initView() {
        Intent intent=getIntent();
        mRoom_Id=intent.getStringExtra("room_id");
        mLive_Url=intent.getStringExtra("path");
        background = (ViewGroup) findViewById(R.id.background);
        chatListView = (ChatListView) findViewById(R.id.chat_listview);
        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
        btnGift = (ImageView) bottomPanel.getView().findViewById(R.id.btn_gift);
        btnHeart = (ImageView) bottomPanel.getView().findViewById(R.id.btn_heart);
        giftView = (GiftItemView) findViewById(R.id.gift_item_first);

        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        surfaceView = (SurfaceView) findViewById(R.id.player_surface);

        chatListAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatListAdapter);
        background.setOnClickListener(this);
        btnGift.setOnClickListener(this);
        btnHeart.setOnClickListener(this);
        bottomPanel.setInputPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text) {
                final TextMessage content = TextMessage.obtain(text);
                LiveKit.sendMessage(content);
            }
        });

        ksyMediaPlayer = new KSYMediaPlayer.Builder(this).build();
        ksyMediaPlayer.setOnPreparedListener(onPreparedListener);
        ksyMediaPlayer.setScreenOnWhilePlaying(true);
        ksyMediaPlayer.setBufferTimeMax(5);
        ksyMediaPlayer.setTimeout(20, 100);
    }

    private void startLiveShow() {
        //聊天室id
        final InformationNotificationMessage content = InformationNotificationMessage.obtain("来啦");
        LiveKit.sendMessage(content);
        joinChatRoom(mRoom_Id);
        playShow(mLive_Url);
    }

    private void joinChatRoom(final String roomId) {
        LiveKit.joinChatRoom("ChatRoom01", 2, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                final InformationNotificationMessage content = InformationNotificationMessage.obtain("来啦");
                LiveKit.sendMessage(content);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(LookActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playShow(String liveUrl) {
        try {
            ksyMediaPlayer.setDataSource(liveUrl);
            ksyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
    }

    @Override
    public void onBackPressed() {
        if (!bottomPanel.onBackAction()) {
            finish();
            return;
        }
    }

    /**
     *获取礼物数据
     */
    private void setData(){
        giftBeen = new ArrayList<LiveGiftBean>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        com.example.rh.artlive.util.HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.LIVEGIFT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                com.example.rh.artlive.util.Log.e("礼物数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray gift=data.getJSONArray("gift");
                            for (int i=0;i<gift.length();i++){
                                JSONObject jsonObject1=gift.getJSONObject(i);
                                mGift=new LiveGiftBean(jsonObject1.getString("gift_id"),jsonObject1.getString("title"),jsonObject1.getString("price"),jsonObject1.getString("poster"));
                                giftBeen.add(mGift);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(background)) {
            bottomPanel.onBackAction();
        } else if (v.equals(btnGift)) {
            FragmentGiftDialog.newInstance().setOnGridViewClickListener(new FragmentGiftDialog.OnGridViewClickListener() {
                @Override
                public void click(LiveGiftBean gift) {
                    gift.setTitle("文人骚客");
//                    gift.giftName = "送你一个小礼物" ;
                    if (!giftBeen.contains(gift)){
                        giftBeen.add(gift);
                        giftView.setGift(gift);
                    }
                    giftView.addNum(1);
                }
            }).show(getSupportFragmentManager(),"dialog");
            GiftMessage msg = new GiftMessage("2", "送您一个礼物");
            LiveKit.sendMessage(msg);
        } else if (v.equals(btnHeart)) {
            heartLayout.post(new Runnable() {
                @Override
                public void run() {
                    int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                    heartLayout.addHeart(rgb);
                }
            });
            GiftMessage msg = new GiftMessage("1", "为您点赞");
            LiveKit.sendMessage(msg);
        }
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SENT: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {
                GiftMessage msg1 = new GiftMessage("1", "为您点赞");
                break;
            }
            default:
        }
        chatListAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    protected void onDestroy() {
        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
                Toast.makeText(LookActivity.this, "退出聊天室成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
                Toast.makeText(LookActivity.this, "退出聊天室失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
        ksyMediaPlayer.stop();
        super.onDestroy();
    }
}
