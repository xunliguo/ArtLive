package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.util.ActionSheetDialog;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/22.
 */

public class ApplyTwoActivity extends BaseFragmentActivity implements View.OnClickListener{
    private ImageView mShowDraw;

    private RelativeLayout mStepLayout;

    private String teacher_id;

    private String ident;
    private String identback;
    private String identhand;
    private String edu;


    private ImageView mShowIdent;
    private ImageView mIdentBack;
    private ImageView mIdentHand;
    private ImageView mEduView;
    private EditText mIdentText;

    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

    private int i=1;

    ArrayList<String> tea_list ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_two);
        init();
        setListener();
    }

    private void init(){
        Intent intent=getIntent();
        teacher_id=intent.getStringExtra("teacher_id");
        mStepLayout=(RelativeLayout)findViewById(R.id.setting);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mShowIdent=(ImageView)findViewById(R.id.up_ident);
        mIdentBack=(ImageView)findViewById(R.id.ident_back);
        mIdentHand=(ImageView)findViewById(R.id.ident_hand);
        mEduView=(ImageView)findViewById(R.id.edu);
        mIdentText=(EditText)findViewById(R.id.ident_two);
    }
    private void setListener(){
        mStepLayout.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
        mShowIdent.setOnClickListener(this);
        mIdentBack.setOnClickListener(this);
        mIdentHand.setOnClickListener(this);
        mEduView.setOnClickListener(this);
    }

    private void setIcon() {
        new ActionSheetDialog(ApplyTwoActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                dispatchTakePictureIntent();
                            }
                        })
                .addSheetItem("手机相册", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                selectImage();
                            }
                        }).show();
    }

    private void Setup(){
        tea_list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("teacher_id",teacher_id);
        map.put("type","APP");
        map.put("ID",mIdentText.getText()+"".toString().trim());
        map.put("teacher_positive_img",ident);
        map.put("teacher_side_img",identback);
        map.put("teacher_hand_img",identhand);
        map.put("teacher_education_img",edu);
        Log.e("正面"+ident+"反面"+identback+"手持"+identhand+"学历"+edu);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.TWOTEP, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("第二步"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("msg"+jsonObject.getString("msg"));
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray problem=data.getJSONArray("problem");
                            for (int i=0;i<problem.length();i++) {
                                JSONObject jsonObject1=problem.getJSONObject(i);
                                tea_list.add(jsonObject1.getString("forum_title"));
                            }
                            Intent intent=new Intent(ApplyTwoActivity.this,ApplyThirdActivity.class);
                            intent.putExtra("teacher_id",data.getString("teacher_id"));
                            intent.putExtra("tea_list",tea_list);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "setup", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                if ("".equals(mIdentText.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入省份证号");
                }else{
                    Setup();
                }
                break;
            case R.id.showDraw:
                finish();
                break;
            case R.id.up_ident:
                i=1;
                setIcon();
                break;
            case R.id.ident_back:
                i=2;
                setIcon();
                break;
            case R.id.ident_hand:
                i=3;
                setIcon();
                break;
            case R.id.edu:
                i=4;
                setIcon();
                break;
        }
    }
    /**
     * 从相册中获取
     */
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        } else {
            showToast("未找到图片查看器");
        }
    }

    /**
     * 创建新文件
     *
     * @return
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* 文件名 */
                ".jpg",         /* 后缀 */
                storageDir      /* 路径 */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * 拍照片
     */
    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            // 创建文件来保存拍的照片
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 异常处理
            }
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            showToast("无法启动相机");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param uri     content:// 样式
     * @param context
     * @return real file path
     */
    public static String getFilePathFromContentUri(Uri uri, Context context) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 获取小图片，防止OOM
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            String filePath = null;
            //判断是哪一个的回调
            if (requestCode == REQUEST_IMAGE_GET) {
                //返回的是content://的样式
                filePath = getFilePathFromContentUri(data.getData(), this);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                }
            }
            if (!TextUtils.isEmpty(filePath)) {
                // 自定义大小，防止OOM
                Bitmap bitmap = getSmallBitmap(filePath, 200, 200);
                if (i==1){
                    mShowIdent.setImageBitmap(bitmap);
                }else if (i==2){
                    mIdentBack.setImageBitmap(bitmap);
                }else if (i==3){
                    mIdentHand.setImageBitmap(bitmap);
                }else if (i==4){
                    mEduView.setImageBitmap(bitmap);
                }
            }
            testUpload(filePath);
            String icon=testUpload(filePath);
            if (i==1){
                ident=icon;
            }else if (i==2){
                identback=icon;
            }else if (i==3){
                identhand=icon;
            }else if (i==4){
                edu=icon;
            }
            Log.e("字节流"+icon);
//            setIonData(icon);
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
}
