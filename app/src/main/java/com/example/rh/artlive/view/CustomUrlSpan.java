package com.example.rh.artlive.view;

import android.content.Context;
import android.content.Intent;
import android.os.IInterface;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.example.rh.artlive.activity.WebViewActivity;

/**
 * Created by rh on 2017/12/5.
 */

public class CustomUrlSpan extends ClickableSpan {

    private Context context;
    private String url;
    public CustomUrlSpan(Context context, String url){
        this.context = context;
        this.url = url;
    }

    @Override
    public void onClick(View widget) {
        // 在这里可以做任何自己想要的处理

        Toast.makeText(context,"dainji"+url,Toast.LENGTH_LONG).show();
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);


    }
}
