package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.example.rh.artlive.R;
import com.example.rh.artlive.widget.Constants;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by Administrator on 2017/8/9.
 */

public class WelcomeActivity extends BaseFragmentActivity {
    public static final String TAG = WelcomeActivity.class.getSimpleName();
    protected Handler mHandler = null;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Constants.SCREEN_DENSITY = metrics.density;
		Constants.SCREEN_HEIGHT = metrics.heightPixels;
		Constants.SCREEN_WIDTH = metrics.widthPixels;

		mHandler = new Handler(getMainLooper());
		initView();
        getAppSignSha1(this);
    }

    protected void initView() {
        handler.postDelayed(new Runnable() {
                                            @Override
            public void run() {
               startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                finish();
            }
        },3000);



    }

    /**
     * 获得app 的sha1值 *
     * @param context
     * @return */
    public static String getAppSignSha1(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),PackageManager.GET_SIGNATURES);
// X509证书，X.509是一种非常通用的证书格式
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory .generateCertificate(new ByteArrayInputStream(sign.toByteArray())); // md5
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] b = md.digest(cert.getEncoded());
            return byte2HexFormatted(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将获取到得编码进行16进制转换
     *
     * @param arr
     * @return
     */
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }
}
