package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.rh.artlive.R;

/**
 * Created by rh on 2017/11/22.
 */

public class AspectActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
        setListener();

    }
    private void  init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
        }
    }
}
