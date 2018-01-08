package com.example.rh.artlive.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.FansBean;
import com.example.rh.artlive.richtext.RichTextEditor;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/20.
 * 发帖
 */

public class PostActivity extends BaseFragmentActivity implements View.OnClickListener{

    private TextPaint mEdit;
    private TextView mShowDraw;
    private ImageView mPhotoView;
    private TextView mSignView;

    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private RichTextEditor editor;
    private View.OnClickListener btnListener;

    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private File mCurrentPhotoFile;// 照相机拍照得到的图片

    ArrayList<String> tea_list =new ArrayList<>();
    StringBuffer buffer = new StringBuffer();//购物车id
    StringBuffer buffer_iamge = new StringBuffer();//购物车id
    private String Image;//全选按钮时购物车id数组
    private String Content;//全选按钮时购物车id数组
    private String type="0";
    private String mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        init();
        setListener();
    }


    private void init() {
        mShowDraw=(TextView) findViewById(R.id.showDraw);
        mPhotoView=(ImageView)findViewById(R.id.button1);

        mSignView=(TextView)findViewById(R.id.showTime);
        editor = (RichTextEditor) findViewById(R.id.richEditor);
    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mPhotoView.setOnClickListener(this);
        mSignView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.button1:
                type="0";
                // 打开系统相册
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");// 相片类型
                startActivityForResult(intent1, REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.showTime:
                List<RichTextEditor.EditData> editList2 = editor.buildEditData();
                com.example.rh.artlive.util.Log.e("1");
                // 下面的代码可以上传、或者保存，请自行实现
                dealEditData(editList2);
                break;

        }
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<RichTextEditor.EditData> editList) {
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                buffer.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                buffer_iamge.append(itemData.imagePath);
            }
        }
        com.example.rh.artlive.util.Log.e("listneir"+tea_list);
        Content=new String(buffer);
        Image=new String(buffer_iamge);
        setData(Content,Image);
    }
    /**
     * 发送
     */
    private void setData(String Content,String Image){
        HashMap<String,String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","APP");
        map.put("content",Content);
        map.put("img",Image);
        map.put("contenttype","#");
        map.put("sfyuanchuang","原创");
        com.example.rh.artlive.util.Log.e("发布帖子"+Content);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.SIGNPOST, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                com.example.rh.artlive.util.Log.e("发布帖子"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            ToastUtil.showToast(PostActivity.this,"发送成功");
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "wen", null);
    }

    protected void openCamera() {
        try {
            // Launch camera to take photo for selected contact
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {

            Uri uri = data.getData();
            insertBitmap(getRealFilePath(uri));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            insertBitmap(mCurrentPhotoFile.getAbsolutePath());
        }
    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {

        com.example.rh.artlive.util.Log.e("type"+type);
            editor.insertImage(imagePath);

        com.example.rh.artlive.util.Log.e("路径"+imagePath);
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

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
