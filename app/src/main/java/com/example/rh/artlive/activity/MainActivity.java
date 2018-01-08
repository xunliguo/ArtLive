package com.example.rh.artlive.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.application.DemoApplication;
import com.example.rh.artlive.camera.CameraActivity;
import com.example.rh.artlive.fragment.HomeFragment;
import com.example.rh.artlive.fragment.MineFragment;
import com.example.rh.artlive.fragment.NewsFragment;
import com.example.rh.artlive.fragment.WormFragment;
import com.example.rh.artlive.live.CommonUtil;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.popup.PopupMenuUtil;
import com.example.rh.artlive.scan.QRActivity;
import com.example.rh.artlive.util.ActionSheetDialog;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.TFileUtils;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.ToastUtil;
import com.hyphenate.EMCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

import static android.R.attr.key;
import static android.R.attr.x;

/**
 * Created by rh on 2017/11/23.
 */

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_PICK_CITY = 233;
    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码



    private HomeFragment testFragment1;//shouye
    private NewsFragment testFragment2;//订餐
    private WormFragment testFragment3;//购物
    private MineFragment testFragment4;//我的

    FragmentManager fragmentManager;

    private RelativeLayout mAddUp;
    private RelativeLayout mDown;
    private LinearLayout mBottomLayout;
    private RelativeLayout mSearchLayout;

    private ImageButton mNews;
    private ImageButton mHome;
    private ImageButton mWorm;
    private ImageButton mMine;
    private ImageButton mAdd;

    private LinearLayout mNews1;
    private LinearLayout mHome1;
    private LinearLayout mWorm1;
    private LinearLayout mMine1;
    private LinearLayout mAdd1;

    private ImageView mSetting;
    private ImageView mScan;
    private ImageView mLocal;

    private TextView mHomeView;
    private TextView mWormView;
    private TextView mMineView;
    private TextView mNewsView;
    private Context context;

    //记录用户首次点击返回键的时间
    private long firstTime=0;
    private static final int NO = x;


    @Override
    public void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
        Log.e("1");
    }

    @Override
    public void onStop() {
        super.onStop();
        AppBus.getInstance().unregister(this);
        Log.e("1");
    }
    // 接受事件
    @Subscribe
    public void setContent(AppCityEvent data) {
        if ("worm".equals(data.getName())){              //虫窝上滑吊顶效果，隐藏头部、导航栏
            mBottomLayout.setVisibility(View.GONE);
        }else if ("worm_1".equals(data.getName())){
            mBottomLayout.setVisibility(View.VISIBLE);
        }else if ("carmera".equals(data.getName())){     //点击拍摄防微信的小视频效果
            getPermissions();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        fragmentManager = getSupportFragmentManager();
        testFragment1 = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, testFragment1).commit();
        init();
        setListener();
    }


    private void init(){
        mNews=(ImageButton)findViewById(R.id.art_news);
        mHome=(ImageButton)findViewById(R.id.art_home);
        mWorm=(ImageButton)findViewById(R.id.art_worm);
        mMine=(ImageButton)findViewById(R.id.art_mine);
        mAdd=(ImageButton)findViewById(R.id.main_add);

        mNews1=(LinearLayout)findViewById(R.id.art_news1);
        mHome1=(LinearLayout)findViewById(R.id.art_home1);
        mWorm1=(LinearLayout)findViewById(R.id.art_worm1);
        mMine1=(LinearLayout) findViewById(R.id.art_mine1);


        mSetting=(ImageView)findViewById(R.id.showTime);
        mLocal=(ImageView)findViewById(R.id.class_img);
        mScan=(ImageView)findViewById(R.id.titleBar_city_name);

        mAddUp=(RelativeLayout)findViewById(R.id.adds);
        mDown=(RelativeLayout)findViewById(R.id.down);
        mBottomLayout=(LinearLayout)findViewById(R.id.art_bottom);
        mSearchLayout=(RelativeLayout)findViewById(R.id.searchmenu);

        mHomeView=(TextView)findViewById(R.id.art_home_view);
        mMineView=(TextView)findViewById(R.id.art_mine_view);
        mWormView=(TextView)findViewById(R.id.art_worm_view);
        mNewsView=(TextView)findViewById(R.id.art_news_view);
    }

    private void setListener(){
        mNews1.setOnClickListener(this);
        mHome1.setOnClickListener(this);
        mWorm1.setOnClickListener(this);
        mMine1.setOnClickListener(this);
        mLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                //大视图文本通知
                //创建消息构造器,在扩展包
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                //设置当有消息是的提示，图标和提示文字
                builder.setSmallIcon(R.drawable.photo).setTicker("有新消息了");
                //需要样式
                NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
                style.setBigContentTitle("上课通知");//通知的标题
                style.bigText("今天下午要在综B上jsp");//通知的文本内容
                //大视图文本具体内容
                style.setSummaryText("这是正常的课程安排，请各位同学按时上课");
                builder.setStyle(style);
                //显示消息到达的时间，这里设置当前时间
                builder.setWhen(System.currentTimeMillis());
                //获取一个通知对象
                Notification notification = builder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                //发送(显示)通知
                //notify()第一个参数id An identifier for this notification unique within your application
                //get?意思说，这个通知在你的应用程序中唯一的标识符
                manager.notify(NO, notification);
            }
        });
        mSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenuUtil.getInstance()._show(context, mAdd);
            }
        });
        mSetting.setOnClickListener(this);
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QRActivity.class);
                startActivityForResult(intent,REQUEST_CODE_PICK_CITY);
            }
        });
    }

    private void hideFragments(FragmentTransaction transaction) {

        if (testFragment1 != null) {
            transaction.hide(testFragment1);
        }
        if (testFragment2 != null) {
            transaction.hide(testFragment2);
        }
        if (testFragment3 != null) {
            transaction.hide(testFragment3);
        }
        if (testFragment4 != null) {
            transaction.hide(testFragment4);
        }
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (v.getId()){
            case R.id.art_home1:
                mAddUp.setVisibility(View.VISIBLE);
                mDown.setVisibility(View.GONE);
                mHome.setImageResource(R.mipmap.home_click);
                mNews.setImageResource(R.mipmap.news);
                mWorm.setImageResource(R.mipmap.worm);
                mMine.setImageResource(R.mipmap.mine);
                mHomeView.setTextColor(Color.parseColor("#ed731b"));
                mNewsView.setTextColor(Color.parseColor("#666666"));
                mWormView.setTextColor(Color.parseColor("#666666"));
                mMineView.setTextColor(Color.parseColor("#666666"));
                if (testFragment1 == null) {
                    testFragment1 = new HomeFragment();
                    transaction.add(R.id.content, testFragment1);
                } else {
                    transaction.show(testFragment1);
                }
                break;
            case R.id.art_news1:
                mAddUp.setVisibility(View.GONE);
                mDown.setVisibility(View.VISIBLE);
                mHome.setImageResource(R.mipmap.home);
                mNews.setImageResource(R.mipmap.news_click);
                mWorm.setImageResource(R.mipmap.worm);
                mMine.setImageResource(R.mipmap.mine);
                mHomeView.setTextColor(Color.parseColor("#666666"));
                mNewsView.setTextColor(Color.parseColor("#ed731b"));
                mWormView.setTextColor(Color.parseColor("#666666"));
                mMineView.setTextColor(Color.parseColor("#666666"));
                if (testFragment2 == null) {
                    testFragment2 = new NewsFragment();
                    transaction.add(R.id.content, testFragment2);
                } else {
                    transaction.show(testFragment2);
                }
                break;
            case R.id.art_worm1:

                mAddUp.setVisibility(View.GONE);
                mDown.setVisibility(View.GONE);
                mHome.setImageResource(R.mipmap.home);
                mNews.setImageResource(R.mipmap.news);
                mWorm.setImageResource(R.mipmap.worm_click);
                mMine.setImageResource(R.mipmap.mine);
                mHomeView.setTextColor(Color.parseColor("#666666"));
                mNewsView.setTextColor(Color.parseColor("#666666"));
                mWormView.setTextColor(Color.parseColor("#ed731b"));
                mMineView.setTextColor(Color.parseColor("#666666"));
                if (testFragment3 == null) {
                    testFragment3 = new WormFragment();
                    transaction.add(R.id.content, testFragment3);
                } else {
                    transaction.show(testFragment3);
                }
                break;
            case R.id.art_mine1:
                mAddUp.setVisibility(View.GONE);
                mDown.setVisibility(View.GONE);
                mHome.setImageResource(R.mipmap.home);
                mNews.setImageResource(R.mipmap.news);
                mWorm.setImageResource(R.mipmap.worm);
                mMine.setImageResource(R.mipmap.mine_click);
                mHomeView.setTextColor(Color.parseColor("#666666"));
                mNewsView.setTextColor(Color.parseColor("#666666"));
                mWormView.setTextColor(Color.parseColor("#666666"));
                mMineView.setTextColor(Color.parseColor("#ed731b"));
                if (testFragment4 == null) {
                    testFragment4 = new MineFragment();
                    transaction.add(R.id.content, testFragment4);
                } else {
                    transaction.show(testFragment4);
                }
                break;
            case R.id.showTime:
                setSex();
                break;
        }
        transaction.commit();
    }

    //选择性别
    private void setSex(){
        new ActionSheetDialog(MainActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("通讯录", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                            }
                        })
                .addSheetItem("好友申请", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                startActivity(new Intent(MainActivity.this,
                                        NewFriendsMsgActivity.class));
                            }
                        })
                .addSheetItem("退出", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                logout();
                            }
                        }).show();
    }

    private void logout() {
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoApplication.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        finish();
                        startActivity(new Intent(MainActivity.this,
                                LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,
                                "unbind devicetokens failed",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                PopupMenuUtil.getInstance()._close();
                ToastUtil.show(this,"再按一次退出程序");
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), 100);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
            }
        } else {
            startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), 100);
        }
    }

    /**
     * 提交相册
     */
    private void setUpload(String path){
        HashMap<String,String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("img",path);
        com.example.rh.artlive.util.Log.e("相册"+path);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.UP_PHOTO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                com.example.rh.artlive.util.Log.e("提交相册"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "wen", null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            android.util.Log.i("CJT", "picture");
            String path = data.getStringExtra("path");
            //base流
            testUpload(path);
            setUpload(testUpload(path));
            Log.e("我的相册"+path);
//            photo.setImageBitmap(BitmapFactory.decodeFile(path));
        }
        if (resultCode == 102) {
            android.util.Log.i("CJT", "video");
            String path = data.getStringExtra("path");
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 图片转换成文件流
     * @param path
     * @return
     */
    public String testUpload(String path) {
        try {

            String srcUrl = path; // "/mnt/sdcard/"; //路径
            // String fileName = PhotoName+".jpg"; //文件名
            FileInputStream fis = new FileInputStream(srcUrl);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[65536];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            String uploadBuffer = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT)); // 进行Base64编码
            Log.e("字节流"+uploadBuffer);
            fis.close();//这两行原来没有
            baos.flush();
            return uploadBuffer;

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return soapObject;
        return null;
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), 100);
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
