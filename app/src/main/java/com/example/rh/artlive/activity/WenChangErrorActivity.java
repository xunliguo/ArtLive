package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rh.artlive.R;

/**
 * Created by rh on 2017/12/25.
 * 文常错题
 */

public class WenChangErrorActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;
    private TextView mEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenchang_error);
        init();
        setListener();

    }
    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mEdit=(TextView)findViewById(R.id.editor);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.editor:
                finish();
                break;
        }
    }
}
