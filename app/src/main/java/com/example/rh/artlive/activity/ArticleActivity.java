package com.example.rh.artlive.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.richtext.RichTextEditor;
import com.example.rh.artlive.richtext.RichTextEditor.EditData;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
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
 * 文章
 */

public class ArticleActivity extends BaseFragmentActivity implements View.OnClickListener{

    private EditText mText;
    private TextPaint mEdit;
    private ImageView mShowDraw;
    private ImageView mPhotoView;
    private ImageView mArtNet;
    private ImageView mArtHeader;
    private TextView mPreView;
    private TextView mLeadView;
    private TextView mFinishView;

    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private RichTextEditor editor;
    private View.OnClickListener btnListener;

    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private File mCurrentPhotoFile;// 照相机拍照得到的图片

    ArrayList<String> tea_list =new ArrayList<>();
    StringBuffer buffer = new StringBuffer();//购物车id
    private String ShopId;//全选按钮时购物车id数组
    private String type="0";
    private String mTitle;

    private ImageView mButton;
    private ImageView mImageView;

    private RelativeLayout mHeaderSey;

    URLSpan urlSpan = new URLSpan("http://www.sdwfqin.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        init();
        setListener();
    }
    private void init() {
        mText = (EditText) findViewById(R.id.phone);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mButton=(ImageView)findViewById(R.id.art_add);
        mImageView=(ImageView)findViewById(R.id.art_iamge);
        mPhotoView=(ImageView)findViewById(R.id.button1);
        mArtNet=(ImageView)findViewById(R.id.button2);
        mArtHeader=(ImageView)findViewById(R.id.button3);
        mPreView=(TextView)findViewById(R.id.showTime);
        mLeadView=(TextView)findViewById(R.id.lead_view);
        mFinishView=(TextView)findViewById(R.id.login_btn);
        mEdit = mText.getPaint();
        editor = (RichTextEditor) findViewById(R.id.richEditor);
        mHeaderSey=(RelativeLayout)findViewById(R.id.lead);




//        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this)
//                .setTitle("修改");
//
//        View view = View.inflate(ArticleActivity.this, R.layout.dialog_xg, null);
////        final EditText et_name = view.findViewById(R.id.et_name);
////        final EditText et_sex = view.findViewById(R.id.et_sex);
////        final EditText et_address = view.findViewById(R.id.et_address);
//
//
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mPreView.setOnClickListener(this);
        mPhotoView.setOnClickListener(this);
        mArtNet.setOnClickListener(this);
        mArtHeader.setOnClickListener(this);
        mFinishView.setOnClickListener(this);
        mButton.setOnClickListener(this);
    }

    /**
     * 自定义对话框
     */
    private void showCustomizeDialog() {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(ArticleActivity.this);
        final View dialogView = LayoutInflater.from(ArticleActivity.this)
                .inflate(R.layout.view_dialog,null);
        customizeDialog.setTitle("编辑导语");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText edit_text =(EditText) dialogView.findViewById(R.id.edit_text);
                        mHeaderSey.setVisibility(View.VISIBLE);
                        mLeadView.setText(edit_text.getText()+"".toString().trim());
                    }
                });
        customizeDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.showTime:
                //跳转预览界面
                List<EditData> editList = editor.buildEditData();
                for (EditData itemData : editList) {
                    if (itemData.inputStr != null) {
                        tea_list.add("str" + itemData.inputStr);
                    } else if (itemData.mSDPath != null) {
                        tea_list.add("pat" + itemData.mSDPath);
                    }
                    com.example.rh.artlive.util.Log.e("图片路径"+tea_list);
                }
                Intent intent=new Intent(ArticleActivity.this,PreViewActivity.class);
                intent.putExtra("art_title",mText.getText()+"".toString().trim());
                intent.putStringArrayListExtra("art_content",tea_list);
                startActivity(intent);
                break;
            case R.id.login_btn:
                List<EditData> editList2 = editor.buildEditData();
                com.example.rh.artlive.util.Log.e("1");
                // 下面的代码可以上传、或者保存，请自行实现
                dealEditData(editList2);
                break;
            case R.id.button1:
                type="0";
                // 打开系统相册
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");// 相片类型
                startActivityForResult(intent1, REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.button2:
                ToastUtil.showToast(this,"请直接在文本框输入完整的连接");
                break;
            case R.id.button3:
                showCustomizeDialog();
                break;
            case R.id.art_add:
                type="1";
                // 打开系统相册
                Intent intent3 = new Intent(Intent.ACTION_PICK);
                intent3.setType("image/*");// 相片类型
                startActivityForResult(intent3, REQUEST_CODE_PICK_IMAGE);
                break;
        }
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<EditData> editList) {
        for (EditData itemData : editList) {
            if (itemData.inputStr != null) {
                com.example.rh.artlive.util.Log.e("commit inputStr=" + itemData.inputStr);
                buffer.append("str" + itemData.inputStr);
                tea_list.add("str" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
               com.example.rh.artlive.util.Log.e("commit imgePath=" + itemData.imagePath);
                tea_list.add(" "+"pat" + itemData.imagePath);
                buffer.append(" "+"pat" + itemData.imagePath);
            }

        }
        com.example.rh.artlive.util.Log.e("listneir"+tea_list);
        ShopId=new String(buffer);
        setData(ShopId);
    }
    /**
     * 发布文章
     */
    private void setData(String ShopId){
        HashMap<String,String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","APP");
        map.put("lead",mLeadView.getText()+"".toString().trim());
        map.put("title",mText.getText()+"".toString().trim());
        map.put("content",ShopId);
        com.example.rh.artlive.util.Log.e("发布文章"+ShopId);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ARTICLE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                com.example.rh.artlive.util.Log.e("发布文章数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            ToastUtil.showToast(ArticleActivity.this,"发送成功");
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
        if ("1".equals(type)){
            mButton.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageURI(Uri.fromFile(new File(imagePath)));
            mSharePreferenceUtil.setString(SharedPerfenceConstant.ARTHEADER,imagePath);
        }else if ("0".equals(type)){
            editor.insertImage(imagePath);
            editor.testUpload("http://baidu.com");
        }

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
