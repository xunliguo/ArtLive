package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

/**
 * Created by rh on 2017/12/7.
 */

public class WebViewActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView mAbout;
    private LinearLayout mLinearLayout;
    private AgentWeb mAgentWeb;
    private ImageView mShowDraw;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
        setListener();
    }

    private void init(){
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        mAbout=(TextView)findViewById(R.id.shop);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mLinearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go(url);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
    }

    /**
     * AgentWeb
     */
    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mAbout != null && !TextUtils.isEmpty(title))
                if (title.length() > 10)
                    title = title.substring(0, 10) + "...";
            mAbout.setText(title);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
        }
    }
}
