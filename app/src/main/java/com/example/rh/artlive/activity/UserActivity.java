package com.example.rh.artlive.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.util.ActionSheetDialog;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.XCRoundImageView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/22.
 * 个人资料修改
 */

public class UserActivity extends BaseFragmentActivity implements View.OnClickListener{
    private static final String MENU_DATA_KEY = "name";
    private int supplierMenuIndex = 0;
    private ImageView mShowDraw;
    //相机拍摄的头像文件(本次演示存放在SD卡根目录下)
    private static final File USER_ICON = new File(Environment.getExternalStorageDirectory(), "user_icon.jpg");
    //请求识别码(分别为本地相册、相机、图片裁剪)
    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;
    private CircleImageView mImageView;


    private RelativeLayout mImageLayout;
    private RelativeLayout mNameLayout;
    private RelativeLayout mSexLayout;
    private RelativeLayout mArtLayout;
    private RelativeLayout mGradleLayout;
    private RelativeLayout mStudyLayout;
    private RelativeLayout mCityLayout;
    private RelativeLayout mHopeLayout;
    private RelativeLayout mPointLayout;
    private RelativeLayout mColegeLayout;

    private List<Map<String, String>> mMenuData1 = new ArrayList<>();; //默认
    private List<Map<String, String>> mMenuData2 = new ArrayList<>();; //品牌
    private List<Map<String, String>> mMenuData3 = new ArrayList<>();; //种类
    private List<Map<String, String>> mMenuData4 = new ArrayList<>();; //种类

    private ListView mPopListView;
    private PopupWindow mPopupWindow;
    private SimpleAdapter mMenuAdapter1;
    private SimpleAdapter mMenuAdapter2;
    private SimpleAdapter mMenuAdapter3;
    private SimpleAdapter mMenuAdapter4;

    List<String> tea_list ;
    List<String> tea_list1 ;
    List<String> tea_list2 ;
    List<String> tea_list3 ;

    private EditText mNameText;

    private TextView mSexView;
    private TextView mOpenView;
    private TextView mGradleView;
    private TextView mStudyView;
    private TextView mCityView;
    private TextView mTargetSchool;
    private TextView mPointView;
    private TextView mSchoolView;
    private TextView mShop;
    private TextView mArtView;
    private TextView mSaveView;
    private EditText mUsreName;
    private EditText mPointText;
    private EditText mSchoolText;

    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private String icon="";
    private String mArt="";
    private String mGradle="";
    private String mCity="";
    private String mSchool="";
    private String mSex="0";
    private String in="";
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }

        init();
        getData();
        initPopMenu();
        setListener();
    }
    private void  init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);

        mNameText=(EditText)findViewById(R.id.phone);

        mSexView=(TextView)findViewById(R.id.sex_value);
        mOpenView=(TextView)findViewById(R.id.boxin_value);
        mGradleView=(TextView)findViewById(R.id.student_value);
        mStudyView=(TextView)findViewById(R.id.activity_value);
        mCityView=(TextView)findViewById(R.id.user_city);
        mTargetSchool=(TextView)findViewById(R.id.target_school);
        mPointView=(TextView)findViewById(R.id.point);
        mSchoolView=(TextView)findViewById(R.id.user_school);
        mShop=(TextView)findViewById(R.id.shop);
        mArtView=(TextView)findViewById(R.id.boxin_value);
        mSaveView=(TextView)findViewById(R.id.editor);
        mUsreName=(EditText)findViewById(R.id.user_name);
        mPointText=(EditText)findViewById(R.id.user_point);
        mSchoolText=(EditText)findViewById(R.id.user_school);


        mImageView=(CircleImageView)findViewById(R.id.roundImageView);

        mImageLayout=(RelativeLayout)findViewById(R.id.done);
        mNameLayout=(RelativeLayout)findViewById(R.id.dtwo);
        mSexLayout=(RelativeLayout)findViewById(R.id.dthird);
        mArtLayout=(RelativeLayout)findViewById(R.id.dfour);
        mGradleLayout=(RelativeLayout)findViewById(R.id.dfive);
        mStudyLayout=(RelativeLayout)findViewById(R.id.dsix);
        mCityLayout=(RelativeLayout)findViewById(R.id.dseven);
        mHopeLayout=(RelativeLayout)findViewById(R.id.deiter);
        mPointLayout=(RelativeLayout)findViewById(R.id.dnine);
        mColegeLayout=(RelativeLayout)findViewById(R.id.dten);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mImageLayout.setOnClickListener(this);
        mNameLayout.setOnClickListener(this);
        mSexLayout.setOnClickListener(this);
        mGradleLayout.setOnClickListener(this);
        mArtLayout.setOnClickListener(this);
        mStudyLayout.setOnClickListener(this);
        mCityLayout.setOnClickListener(this);
        mPointLayout.setOnClickListener(this);
        mHopeLayout.setOnClickListener(this);
        mColegeLayout.setOnClickListener(this);
        mArtView.setOnClickListener(this);
        mSaveView.setOnClickListener(this);
    }


    private void getData(){
        tea_list = new ArrayList<String>();
        tea_list1 = new ArrayList<String>();
        tea_list2 = new ArrayList<String>();
        tea_list3 = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.MINEMODIFY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("个人资料列表"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            //艺考方向
                            JSONArray city=data.getJSONArray("yikaofangxiang");
                            for (int i=0;i<city.length();i++){
                                JSONObject jsonObject1=city.getJSONObject(i);
                                tea_list.add(jsonObject1.getString("tag_name"));
                            }
                            int size=tea_list.size();
                            String[] array = (String[])tea_list.toArray(new String[size]);
                            for(int i=0;i<array.length;i++){
                                Map<String, String> map = new HashMap<>();
                                map.put(MENU_DATA_KEY, array[i]);
                                mMenuData1.add(map);
                            }
                            //年级
                            JSONArray status=data.getJSONArray("nianji");
                            for(int i=0;i<status.length();i++){
                                JSONObject jsonObject1=status.getJSONObject(i);
                                tea_list1.add(jsonObject1.getString("tag_name"));
                            }
                            Log.e("one"+tea_list1);
                            int size1=tea_list1.size();
                            String[] array1 = (String[])tea_list1.toArray(new String[size1]);
                            for(int i=0;i<array1.length;i++){
                                Map<String, String> map = new HashMap<>();
                                map.put(MENU_DATA_KEY, array1[i]);
                                mMenuData2.add(map);
                            }
                            //城市
                            JSONArray third=data.getJSONArray("city");
                            for(int i=0;i<third.length();i++){
                                JSONObject direction=third.getJSONObject(i);
                                tea_list2.add(direction.getString("city_name"));
                            }
                            int sizetwo=tea_list2.size();
                            String[] array2 = (String[])tea_list2.toArray(new String[sizetwo]);
                            for(int i=0;i<array2.length;i++){
                                Map<String, String> map = new HashMap<>();
                                map.put(MENU_DATA_KEY, array2[i]);
                                mMenuData3.add(map);
                            }
                            //学校
                            JSONArray identity=data.getJSONArray("school");
                            for(int i=0;i<identity.length();i++){
                                JSONObject jsonObject1=identity.getJSONObject(i);
                                tea_list3.add(jsonObject1.getString("school_name"));
                            }
                            int size3=tea_list3.size();
                            String[] array3 = (String[])tea_list3.toArray(new String[size3]);
                            for(int i=0;i<array3.length;i++){
                                Map<String, String> map = new HashMap<>();
                                map.put(MENU_DATA_KEY, array3[i]);
                                mMenuData4.add(map);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
    }


    private void setSave(){
        HashMap<String, String> map = new HashMap<>();


        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("user_img",icon);
        map.put("user_nickname",mUsreName.getText()+"".toString().trim());
        map.put("user_sex",mSex);
        map.put("user_artdirection",mArt);
        map.put("user_grade",mGradle);
        map.put("user_experience",in);
        map.put("user_area",mCity);
        map.put("user_school",mSchool);
        map.put("user_achievement",mPointText.getText()+"".toString().trim());
        map.put("user_attendschool",mSchoolText.getText()+"".toString().trim());
        map.put("type","APP");
        Log.e("文化分"+mPointText.getText()+"",toString().trim());
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.MINEMODIFY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("保存个人资料"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("msg"+jsonObject.getString("msg"));
                    if ("1".equals(jsonObject.getString("state"))) {
                       ToastUtil.showToast(UserActivity.this,"保存成功");
                       AppBus.getInstance().post(new AppCityEvent("save"));
                      finish();
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
            case R.id.showDraw:
                finish();
                break;
            case R.id.done:
                setIcon();
                break;
            case R.id.dtwo:
                break;
            case R.id.dthird:
                setSex();
                break;
            case R.id.dfour:
                mPopListView.setAdapter(mMenuAdapter1);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 0;
                break;
            case R.id.dfive:
                mPopListView.setAdapter(mMenuAdapter2);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 1;
                break;
            case R.id.dsix:
                setStudy();
                break;
            case R.id.dseven:
                mPopListView.setAdapter(mMenuAdapter3);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 2;
                break;
            case R.id.deiter:
                mPopListView.setAdapter(mMenuAdapter4);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 3;
                break;
            case R.id.dten:
                break;
            case R.id.editor:
                if ("".equals(mUsreName.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入姓名");
                }else if ("".equals(mPointText.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入文化分");
                }else {
                    setSave();
                }
                break;
        }
    }


    /**
     * popu文件
     */
    private void initPopMenu() {
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_popwin_supplier_list, null);
        mPopListView = (ListView) popView.findViewById(R.id.popwin_list_view);
//        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        mPopupWindow = new PopupWindow(popView, (getScreenWidth() / 5) * 3, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        // 设置动画效果
        mPopupWindow.setAnimationStyle(R.style.AnimationFade);

        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        this.getWindow().setAttributes(params);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
//        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        popView.findViewById(R.id.popwin_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        /**
         * 用来显示item的标题
         */
        mMenuAdapter1 = new SimpleAdapter(this, mMenuData1, R.layout.item_popwin_list,
                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        mMenuAdapter2 = new SimpleAdapter(this, mMenuData2, R.layout.item_popwin_list,
                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        mMenuAdapter3 = new SimpleAdapter(this, mMenuData3, R.layout.item_popwin_list,
                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        mMenuAdapter4 = new SimpleAdapter(this, mMenuData4, R.layout.item_popwin_list,
                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        /**
         * 获取点击item 的name
         */
        mPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (supplierMenuIndex) {
                    case 0:
                        mArt=mMenuData1.get(i).get(MENU_DATA_KEY);
                        mOpenView.setText(mArt);
                        mPopupWindow.dismiss();
                        break;
                    case 1:
                        mGradle=mMenuData1.get(i).get(MENU_DATA_KEY);
                        Log.e("地址"+mMenuData1.get(i).get(MENU_DATA_KEY));
                        mGradleView.setText(mMenuData2.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        break;
                    case 2:
                        mCity=mMenuData3.get(i).get(MENU_DATA_KEY);
                        mCityView.setText(mMenuData3.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        break;
                    case 3:
                        mSchool=mMenuData4.get(i).get(MENU_DATA_KEY);
                        mTargetSchool.setText(mMenuData4.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setSex(){
        new ActionSheetDialog(UserActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("男", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                mSexView.setText("男");
                                mSex="1";
                            }
                        })
                .addSheetItem("女", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                mSexView.setText("女");
                                mSex="0";
                            }
                        }).show();
    }
    private void getPicFromCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(USER_ICON));
        startActivityForResult(intent, CODE_CAMERA_REQUEST);
    }

    private void setIcon(){
        new ActionSheetDialog(UserActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
//                                dispatchTakePictureIntent();
                                getPicFromCamera();
                            }
                        })
                .addSheetItem("手机相册", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
//                               selectImage();
//                                //调用获取本地图片的方法
                             getPicFromLocal();
                            }
                        }).show();
    }
    private void getPicFromLocal() {
        Intent intent = new Intent();
        // 获取本地相册方法一
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //获取本地相册方法二
//    intent.setAction(Intent.ACTION_PICK);
//    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//        "image/*");
        startActivityForResult(intent, CODE_PHOTO_REQUEST);
    }
    /**
     * 图片裁剪
     *
     * @param uri
     */
    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
    /*outputX outputY 是裁剪图片宽高
     *这里仅仅是头像展示，不建议将值设置过高
     * 否则超过binder机制的缓存大小的1M限制
     * 报TransactionTooLargeException
     */
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_PHOTO_CLIP);
    }
    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mImageView.setImageBitmap(photo);
        }
    }
    //艺考培训
    private void setStudy(){
        new ActionSheetDialog(UserActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("从未参加过艺考培训", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                mStudyView.setText("从未参加过培训");
                                in="0";
                            }
                        })
                .addSheetItem("已参加过艺考培训", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                mStudyView.setText("已参加过艺考培训");
                                in="1";
                            }
                        }).show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(UserActivity.this, "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_CAMERA_REQUEST:
                if (USER_ICON.exists()) {
                    photoClip(Uri.fromFile(USER_ICON));
                }
                break;
            case CODE_PHOTO_REQUEST:
                if (data != null) {

//                    try {
//                        file = new File(new URI(data.getData().toString()));
//
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }

                    photoClip(data.getData());

                }
                break;
            case CODE_PHOTO_CLIP:
                if (data != null) {
                    setImageToHeadView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // 回调成功
//        if (resultCode == RESULT_OK) {
//            String filePath = null;
//            //判断是哪一个的回调
//            if (requestCode == REQUEST_IMAGE_GET) {
//                //返回的是content://的样式
//                filePath = getFilePathFromContentUri(data.getData(), this);
//            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
//                if (mCurrentPhotoPath != null) {
//                     mCurrentPhotoPath=filePath;
//                }
//            }
//            if (!TextUtils.isEmpty(filePath)) {
//                // 自定义大小，防止OOM
//                Bitmap bitmap = getSmallBitmap(filePath, 200, 200);
//                mImageView.setImageBitmap(bitmap);
//            }
//               testUpload(filePath);
//               icon=testUpload(filePath);
//
//               Log.e("字节流1"+icon);
////                setIonData(icon);
//        }
//    }

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
