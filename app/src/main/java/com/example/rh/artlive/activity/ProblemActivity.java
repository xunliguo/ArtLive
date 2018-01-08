package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.util.ActionSheetDialog;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;

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
 * Created by rh on 2017/12/14.
 */

public class ProblemActivity extends BaseFragmentActivity implements View.OnClickListener{

    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private String mCurrentPhotoPath;
    private String iconType="";//图片字节流

    List<String> list = new ArrayList<String>();
    private StringBuffer icontypebuffer=new StringBuffer();
    private String IconClass;

    private ImageView mAddView;
    private ImageView mAddtwoView;
    private ImageView mAddthirdView;
    private ImageView mAddfourView;
    private ImageView mAddfiveView;
    private ImageView mAddsixView;
    private ImageView mShowDraw;

    private TextView mSuyButon;
    private EditText mEditText;
    private int selected=1;

    private String mSchool_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        init();
        setListener();
    }

    private void init(){
        mAddView=(ImageView)findViewById(R.id.ivAvatar);
        Log.e("原始大小"+mAddView);
        mAddtwoView=(ImageView)findViewById(R.id.ivAvatar2);
        mAddthirdView=(ImageView)findViewById(R.id.ivAvatar3);
        mAddfourView=(ImageView)findViewById(R.id.ivAvatar4);
        mAddfiveView=(ImageView)findViewById(R.id.ivAvatar5);
        mAddsixView=(ImageView)findViewById(R.id.ivAvatar6);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);

        mSuyButon=(TextView)findViewById(R.id.editor);
        mEditText=(EditText)findViewById(R.id.add_content);
        Intent intent=getIntent();
        mSchool_Id=intent.getStringExtra("school_id");
    }
    private void setListener(){
        mAddView.setOnClickListener(this);
        mAddtwoView.setOnClickListener(this);
        mAddthirdView.setOnClickListener(this);
        mAddfourView.setOnClickListener(this);
        mAddfiveView.setOnClickListener(this);
        mAddsixView.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
        mSuyButon.setOnClickListener(this);
    }
    /**
     * 获取图片的路径转化为字节流 上传到服务器
     */
    private void setData(String iconType){
        HashMap map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("content",mEditText.getText()+"".toString().trim());
        map.put("img", IconClass);//图片字节流
        map.put("school_id", mSchool_Id);
        map.put("type", "APP");

        Log.e("图片"+IconClass+"提交的内容"+mEditText.getText()+"".toString().trim()+"学校id"+mSchool_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.PROBLEM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("提交数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        if ("1".equals(jsonObject.getString("state"))) {
                            ToastUtil.show(ProblemActivity.this,"评论成功");
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "data1", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editor:
                if ("".equals(mEditText.getText()+"".toString().trim())){
                    ToastUtil.show(this,"请输入内容");
                }else {
                    setData(iconType);
                }
                break;
            case R.id.showDraw:
                finish();
                break;
            case R.id.ivAvatar:
                selected=1;
                new ActionSheetDialog(ProblemActivity.this)
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
                        .addSheetItem("我的相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        selectImage();
                                    }
                                }).show();

                break;
            case R.id.ivAvatar2:
                selected=2;
                new ActionSheetDialog(ProblemActivity.this)
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
                        .addSheetItem("我的相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        selectImage();
                                    }
                                }).show();
                break;
            case R.id.ivAvatar3:
                selected=3;
                new ActionSheetDialog(ProblemActivity.this)
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
                        .addSheetItem("我的相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        selectImage();
                                    }
                                }).show();
                break;
            case R.id.ivAvatar4:
                selected=4;
                new ActionSheetDialog(ProblemActivity.this)
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
                        .addSheetItem("我的相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        selectImage();
                                    }
                                }).show();
                break;
            case R.id.ivAvatar5:
                selected=5;
                new ActionSheetDialog(ProblemActivity.this)
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
                        .addSheetItem("我的相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        selectImage();
                                    }
                                }).show();
                break;
            case R.id.ivAvatar6:
                selected=6;
                new ActionSheetDialog(ProblemActivity.this)
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
                        .addSheetItem("我的相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        selectImage();
                                    }
                                }).show();
                break;

        }
    }


//    /**
//     * 获取图片的路径转化为字节流 上传到服务器
//     */
//    private void setImageData(String toysClass,String AgeClass,String LocalClass,String brandClass,String iconType){
//
//        String[][] arr = {{iconType},{" "}};
//        HashMap map = new HashMap<>();
//        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
//        map.put("wjzl",toysClass);
//        map.put("synl",AgeClass);
//        map.put("district",LocalClass);
//        map.put("brand_id",brandClass);
//        map.put("img_url", arr);//图片字节流
//
////        map.put("title",mTitleText.getText()+"".toString().trim());
////        map.put("content",mContentText.getText()+"".toString().trim());
////        map.put("scjiage",mPriceText.getText()+"".toString().trim());
////        map.put("jiage",mLeasePriceText.getText()+"".toString().trim());
////        map.put("deposit",mTitleText.getText()+"".toString().trim());
//        HttpUtil.postHttpRequstProgess(EvaliateActivity.this, "正在加载...", UrlConstant.TOYSLEASE, map, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//            }
//            @Override
//            public void onResponse(String response) {
//                Log.e("个人出租"+response);
//                JSONObject jsonObject;
//                try {
//                    jsonObject = new JSONObject(response);
//                    if ("0".equals(jsonObject.getString("state"))) {
//                        if (jsonObject.has("data")) {
//                        }
//                    }
//                } catch (com.alibaba.fastjson.JSONException e) {
//                    e.printStackTrace();
//                } catch (org.json.JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "uplate", null);
//    }

    /**
     * 从相册中获取
     */
    public void selectImage() {
//        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
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

                Log.e("图片"+filePath);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                    Log.e("图片"+filePath);
                }
            }
            if (!TextUtils.isEmpty(filePath)) {
                // 自定义大小，防止OOM
                Bitmap bitmap = getSmallBitmap(filePath, 200, 200);
                if(selected==1) {
                    mAddView.setImageBitmap(bitmap);
                    mAddtwoView.setVisibility(View.VISIBLE);
                }else if(selected==2){
                    mAddtwoView.setImageBitmap(bitmap);
                    mAddthirdView.setVisibility(View.VISIBLE);
                }else if(selected==3){
                    mAddthirdView.setImageBitmap(bitmap);
                    mAddfourView.setVisibility(View.VISIBLE);
                }else if(selected==4){
                    mAddfourView.setImageBitmap(bitmap);
                    mAddfiveView.setVisibility(View.VISIBLE);
                }else if(selected==5){
                    mAddfiveView.setImageBitmap(bitmap);
                    mAddsixView.setVisibility(View.VISIBLE);
                }else if(selected==6){
                    mAddsixView.setImageBitmap(bitmap);
//                    mAddsixView.setVisibility(View.VISIBLE);
                }

                Log.e("图片大小"+mAddView);
            }
            testUpload(filePath);
            iconType=testUpload(filePath);
            list.add(iconType+",");
            Log.e("图片list"+list);

//            for (int i=0;i<3;i++){
            String aa=iconType;
            icontypebuffer.append(iconType+",");
//            }

            IconClass= new String(icontypebuffer);

            Log.e("图片大小"+IconClass);

            Log.e("我的字节流"+IconClass);
        }
    }
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
