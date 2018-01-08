package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rh.artlive.R;
import com.example.rh.artlive.live.DemoActivity;
import com.example.rh.artlive.live.FakeServer;
import com.example.rh.artlive.live.LiveKit;
import com.example.rh.artlive.live.LookActivity;
import com.example.rh.artlive.util.Log;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by rh on 2017/12/14.
 * 加号直播
 */

public class AddLiveActivity extends BaseFragmentActivity {


    private Button mView;
    private Button mee;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlive);

        mView=(Button) findViewById(R.id.ViewId);
        mee=(Button)findViewById(R.id.see);
        mee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url="http://live1.aiegi.cn/AppName/StreamName.m3u8";
                //http://live1.aiegi.cn/AppName/StreamName.m3u8
//                fakeLogin("张三", "123456", "http://live1.aiegi.cn/test/zhang.m3u8?auth_key=1510130834-0-0-e99805f4c8665b05c04072b5a9f3383a");
                Log.e("token","toekn");
//                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                        intent.putExtra(MainActivity.LIVE_URL, url);
//                startActivity(intent);
//                  user="13";
                final UserInfo user = FakeServer.getLoginUser("12", "1231");

                final String token="fcwMBm3dKnuRkJXHcaP6gsCCjH4o6JjLdHR8Js6hTz9twBhDdJ6G3Py2dfBHCxZV18G9qGthP0RgdRMlOOrDMA==";


                LiveKit.connect(token, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        Log.e( "connect onTokenIncorrect");
                        // 检查appKey 与token是否匹配.
                        android.util.Log.e("获取token","s");
                    }

                    @Override
                    public void onSuccess(String userId) {
                        Log.e("connect onSuccess");
                        LiveKit.setCurrentUser(user);
                        Intent intent = new Intent(AddLiveActivity.this, LookActivity.class);
                        intent.putExtra(LookActivity.LIVE_URL, url);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
//                        Log.e("获取token",+errorCode);
                        Log.e("connect onError = " + errorCode);
                        // 根据errorCode 检查原因.
                    }
                });
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddLiveActivity.this, DemoActivity.class);
                startActivity(intent);
            }
        });
    }
}
