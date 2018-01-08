package com.example.rh.artlive.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.service.ScreenService;

/**
 * Created by rh on 2017/12/20.
 */

public class ScreenRecordActivity extends BaseFragmentActivity {

    Button mButton, mCapButton;
    ImageView mImageView;
    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private ScreenService recordService;
    private static int RECORD_REQUEST_CODE = 5;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        context=this;
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mButton = (Button) findViewById(R.id.butview);
        mCapButton = (Button) findViewById(R.id.capout);
        mImageView = (ImageView) findViewById(R.id.img);
        Intent intent = new Intent(this, ScreenService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //######## 录屏逻辑 ########
                if (recordService.isRunning()) {
                    recordService.stopRecord();
                    recordService.getSavePath();
                    Toast.makeText(context,"路径"+recordService.getSavePath()+ System.currentTimeMillis() + ".mp4",Toast.LENGTH_LONG).show();
                    mButton.setText("录屏");
                } else {
                    //这里是请求录屏权限
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ScreenRecordActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
                    } else {
                        Intent captureIntent = projectionManager.createScreenCaptureIntent();
                        startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
                    }
                }
            }
        });
        mCapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(TestActivity.this, CapoutActivity.class);
//                startActivity(in);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            //######## 录屏逻辑 ########
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();
            mButton.setText("结束");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            ScreenService.RecordBinder binder = (ScreenService.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
            mButton.setEnabled(true);
            mButton.setText(recordService.isRunning() ? "结束" : "开始");
        }


        @Override
        public void onServiceDisconnected(ComponentName arg0) {}
    };
}
