package com.example.rh.artlive.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.util.GlideCacheUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;

/**
 * Created by rh on 2017/11/22.
 */

public class SettingActivity extends BaseFragmentActivity implements View.OnClickListener{

    private RelativeLayout mClean;
    private RelativeLayout mLawLayout;
    private RelativeLayout mAboutLayout;
    private RelativeLayout mFeedBook;
    private Button mLogin_Out;
    private ImageView mShowDraw;
    private RelativeLayout mpingfen;
    private TextView mcrush_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        setListener();
    }
    private void init(){
        mClean=(RelativeLayout)findViewById(R.id.dfives);
        mLawLayout=(RelativeLayout)findViewById(R.id.deitera);
        mAboutLayout=(RelativeLayout)findViewById(R.id.dsixs);
        mFeedBook=(RelativeLayout)findViewById(R.id.dthirds);
        mLogin_Out=(Button)findViewById(R.id.login_out);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mpingfen =(RelativeLayout)findViewById(R.id.dsevens);
        mcrush_size =(TextView)findViewById(R.id.crush_size);
        String cacheSize = GlideCacheUtil.getInstance().getCacheSize(SettingActivity.this);
        if (cacheSize.equals("0.0Byte")) {
            mcrush_size.setText("0 k");
        } else{
            mcrush_size.setText(cacheSize);
        }
    }
    private void setListener(){
        mClean.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
        mLawLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mFeedBook.setOnClickListener(this);
        mLogin_Out.setOnClickListener(this);
        mpingfen.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dfives:
            showNormalDialog();

                break;
            case R.id.showDraw:
                finish();
                break;
            case R.id.deitera:
                Intent intent=new Intent(SettingActivity.this,LawActivity.class);
                startActivity(intent);
                break;
            case R.id.dsixs:
                Intent intent1=new Intent(SettingActivity.this,AboutUsActivity.class);
                startActivity(intent1);
                break;
            case R.id.dthird:
                Intent intent2=new Intent(SettingActivity.this,FeedBookActivity.class);
                startActivity(intent2);
                break;
            case R.id.login_out:
                mSharePreferenceUtil.setString(SharedPerfenceConstant.TOKEN,"");
                Intent intent3=new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent3);
                finish();
                break;
                case R.id.dsevens:
                    goToMarket(SettingActivity.this,"com.example.rh.artlive");
                    break;
        }

    }


    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SettingActivity.this);
        normalDialog.setTitle("温馨提示");
        normalDialog.setMessage("你确定要清除所有缓存吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GlideCacheUtil.getInstance().clearImageAllCache(SettingActivity.this);
                        GlideCacheUtil.getInstance().clearImageMemoryCache(SettingActivity.this);
                        Toast.makeText(SettingActivity.this, "清除完成！", Toast.LENGTH_SHORT).show();
                        mcrush_size.setText("0 k");
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();

    }

}
