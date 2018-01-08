package com.example.rh.artlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.PostActivity;
import com.example.rh.artlive.activity.PostDetailsActivity;
import com.example.rh.artlive.activity.UserMainActivity;
import com.example.rh.artlive.activity.UserMainUActivity;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.bean.NoPaiBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.fragment.FollowFragment;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.ottoEvent.AppShopEvent;
import com.example.rh.artlive.photo.DynamicGridAdapter;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.ScreenUtils;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.NoScrollGridView;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.example.rh.artlive.view.ReboundScrollView_Horizontal;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/25.
 */

public class HomeAdapter extends BaseRecyclerAdapter<FollowBean> {

    private String zt;
    private String mToken;
    private String mUser_id;
    public static int flag=0;
    public static int type=0;
    private LinearLayout mLayout1;
    private LinearLayout mLayout2;
    private LinearLayout mLayout3;
    private LinearLayout mLayout4;
    private LinearLayout mLayout5;
    private LinearLayout mLayout6;
    private LinearLayout mLayout7;
    private LinearLayout mLayout8;
    private LinearLayout mLayout9;

    List<String> list_a=new ArrayList<String>();
    List<String> list_name_a=new ArrayList<String>();
    List<String> list_name_id=new ArrayList<String>();
    private ArrayList<String> picarr1 =new ArrayList<>();
    private ArrayList<String> picarr2 =new ArrayList<>();
    private ArrayList<String> picarr3 =new ArrayList<>();
    private ArrayList<String> picarr4 =new ArrayList<>();
    private ArrayList<String> picarr5 =new ArrayList<>();
    private ArrayList<String> picarr6 =new ArrayList<>();
    private ArrayList<String> picarr7 =new ArrayList<>();

    public HomeAdapter(Context context, int layoutId, List<FollowBean> datas,List<String> list,List<String> list_name,List<String> list_id,ArrayList<String> list_det1
                      ,ArrayList<String> list_det2,ArrayList<String> list_det3,ArrayList<String> list_det4,ArrayList<String> list_det5,ArrayList<String> list_det6
                      ,ArrayList<String> list_det7,String mToken,String mUser_id) {
        super(context, layoutId, datas);
        mContext=context;
        list_a=list;
        list_name_a=list_name;
        list_name_id=list_id;
        picarr1=list_det1;
        picarr2=list_det2;
        picarr3=list_det3;
        picarr4=list_det4;
        picarr5=list_det5;
        picarr6=list_det6;
        picarr7=list_det7;
        this.mToken=mToken;
        this.mUser_id=mUser_id;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, final FollowBean s) {

//            Log.e("图片"+s.getPicarrBean().getPicarr_img_url().length);

        mLayout1=holder.getView(R.id.layout2);
        mLayout2=holder.getView(R.id.layout3);
        mLayout3=holder.getView(R.id.layout4);
        mLayout4=holder.getView(R.id.layout5);
        mLayout5=holder.getView(R.id.layout6);
        mLayout6=holder.getView(R.id.layout7);
        mLayout7=holder.getView(R.id.layout8);
        mLayout8=holder.getView(R.id.layout9);
        mLayout9=holder.getView(R.id.layout10);

        TextView name=holder.getView(R.id.user_title);
        TextView mShareView=holder.getView(R.id.textView3);
        final TextView mZanView=holder.getView(R.id.textView2);
        TextView time=holder.getView(R.id.time);
        TextView content=holder.getView(R.id.content);
        final ImageView image=holder.getView(R.id.click_down);
        RelativeLayout mheader=holder.getView(R.id.follow_header);
//        RelativeLayout mGridLayout=holder.getView(R.id.grid_layout);

        CircleImageView circleImageView=holder.getView(R.id.follow_user_icon);
        CircleImageView mUserMain=holder.getView(R.id.follow_user_image);
        CircleImageView mUserMain_one=holder.getView(R.id.follow_user_image_one);
        CircleImageView mUserMain_two=holder.getView(R.id.follow_user_image_two);
        CircleImageView mUserMain_third=holder.getView(R.id.follow_user_image_third);
        CircleImageView mUserMain_four=holder.getView(R.id.follow_user_image_four);
        CircleImageView mUserMain_five=holder.getView(R.id.follow_user_image_five);
        CircleImageView mUserMain_six=holder.getView(R.id.follow_user_image_six);
        CircleImageView mUserMain_seven=holder.getView(R.id.follow_user_image_seven);
        CircleImageView mUserMain_eight=holder.getView(R.id.follow_user_image_eight);
        CircleImageView mUserMain_night=holder.getView(R.id.follow_user_image_night);

        TextView mFollowName=holder.getView(R.id.follow_name);
        TextView mFollowName_one=holder.getView(R.id.follow_name_one);
        TextView mFollowName_two=holder.getView(R.id.follow_name_two);
        TextView mFollowName_third=holder.getView(R.id.follow_name_third);
        TextView mFollowName_four=holder.getView(R.id.follow_name_four);
        TextView mFollowName_five=holder.getView(R.id.follow_name_five);
        TextView mFollowName_six=holder.getView(R.id.follow_name_six);
        TextView mFollowName_seven=holder.getView(R.id.follow_name_seven);
        TextView mFollowName_eight=holder.getView(R.id.follow_name_eight);
        TextView mFollowName_night=holder.getView(R.id.follow_name_night);

        final LinearLayout mClick_Up=holder.getView(R.id.me_user_tv_up);
        final LinearLayout mComm=holder.getView(R.id.me_user_tv_cart2);
        //去掉滑动条
        ReboundScrollView_Horizontal reboundScrollView_horizontal=holder.getView(R.id.reboundScrollView1);
        reboundScrollView_horizontal.setHorizontalScrollBarEnabled(false);

        NoScrollGridView picarr=holder.getView(R.id.gridView_picarr);
        int size=list_a.size();
        final String[] array = (String[])list_a.toArray(new String[size]);
        for (int i=0;i<array.length;i++){
            if (i==0){
                Glide.with(mContext).load(array[i]).into(mUserMain);
            }else if (i==1){
                mLayout1.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_one);
            }else if (i==2){
                mLayout2.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_two);
            }else if (i==3){
                mLayout3.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_third);
            }else if (i==4){
                mLayout4.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_four);
            }else if (i==5){
                mLayout5.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_five);
            }else if (i==6){
                mLayout6.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_six);
            }else if (i==7){
                mLayout7.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_seven);
            }else if (i==8){
                mLayout8.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_eight);
            }else if (i==9){
                mLayout9.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(array[i]).into(mUserMain_night);
            }
        }

        int size3=list_name_id.size();
        final String[] array3 = (String[])list_name_id.toArray(new String[size3]);
        for (int i=0;i<array3.length;i++){
            if (i==0){
                mUserMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(mContext,PostActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            }else if (i==1){
                final String value=array3[i];
                mUserMain_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }else if (i==2){
                final String value=array3[i];
                mUserMain_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }else if (i==3){
                final String value=array3[i];
                mUserMain_third.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }else if (i==4){
                final String value=array3[i];
                mUserMain_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }else if (i==5){
                final String value=array3[i];
                mUserMain_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }else if (i==6){
                final String value=array3[i];
                mUserMain_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }else if (i==7){
                final String value=array3[i];
                mUserMain_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }else if (i==8){
                final String value=array3[i];
                mUserMain_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }else if (i==9){
                final String value=array3[i];
                mUserMain_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent7=new Intent(mContext, UserMainUActivity.class);
                        intent7.putExtra("user_id",value);
                        mContext.startActivity(intent7);
                    }
                });
            }
        }




        int size2=list_name_a.size();
        final  String[] array2=list_name_a.toArray(new String[size2]);
        for (int i=0;i<array2.length;i++){
            if (i==0){
                mFollowName.setText(array2[i]);
            }else if (i==1){
                mFollowName_one.setText(array2[i]);
            }else if (i==2){
                mFollowName_two.setText(array2[i]);
            }else if (i==3){
                mFollowName_third.setText(array2[i]);
            }else if (i==4){
                mFollowName_four.setText(array2[i]);
            }else if (i==5){
                mFollowName_five.setText(array2[i]);
            }else if (i==6){
                mFollowName_six.setText(array2[i]);
            }else if (i==7){
                mFollowName_seven.setText(array2[i]);
            }else if (i==8){
                mFollowName_eight.setText(array2[i]);
            }else if (i==9){
                mFollowName_night.setText(array2[i]);
            }
        }
        if (s.getI().equals("0")) {
            int size1 = picarr1.size();
            final String[] array1 = (String[]) picarr1.toArray(new String[size1]);
            if (array1.length > 0) {
                picarr.setVisibility(View.VISIBLE);
                picarr.setAdapter(new DynamicGridAdapter(array1, mContext));
                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, array1);
                    }
                });
            } else {
                picarr.setVisibility(View.GONE);
            }
        }else if (s.getI().equals("1")){
            int size1 = picarr2.size();
            final String[] array1 = (String[]) picarr2.toArray(new String[size1]);
            if (array1.length > 0) {
                picarr.setVisibility(View.VISIBLE);
                picarr.setAdapter(new DynamicGridAdapter(array1, mContext));
                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, array1);
                    }
                });
            } else {
                picarr.setVisibility(View.GONE);
            }
        }else if (s.getI().equals("2")){
            int size1 = picarr3.size();
            final String[] array1 = (String[]) picarr3.toArray(new String[size1]);
            if (array1.length > 0) {
                picarr.setVisibility(View.VISIBLE);
                picarr.setAdapter(new DynamicGridAdapter(array1, mContext));
                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, array1);
                    }
                });
            } else {
                picarr.setVisibility(View.GONE);
            }
        }else if (s.getI().equals("3")){
            int size1 = picarr4.size();
            final String[] array1 = (String[]) picarr4.toArray(new String[size1]);
            if (array1.length > 0) {
                picarr.setVisibility(View.VISIBLE);
                picarr.setAdapter(new DynamicGridAdapter(array1, mContext));
                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, array1);
                    }
                });
            } else {
                picarr.setVisibility(View.GONE);
            }
        }else if (s.getI().equals("4")){
            int size1 = picarr5.size();
            final String[] array1 = (String[]) picarr5.toArray(new String[size1]);
            if (array1.length > 0) {
                picarr.setVisibility(View.VISIBLE);
                picarr.setAdapter(new DynamicGridAdapter(array1, mContext));
                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, array1);
                    }
                });
            } else {
                picarr.setVisibility(View.GONE);
            }
        }else if (s.getI().equals("5")){
            int size1 = picarr6.size();
            final String[] array1 = (String[]) picarr6.toArray(new String[size1]);
            if (array1.length > 0) {
                picarr.setVisibility(View.VISIBLE);
                picarr.setAdapter(new DynamicGridAdapter(array1, mContext));
                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, array1);
                    }
                });
            } else {
                picarr.setVisibility(View.GONE);
            }
        }else if (s.getI().equals("6")){
            int size1 = picarr7.size();
            final String[] array1 = (String[]) picarr7.toArray(new String[size1]);
            if (array1.length > 0) {
                picarr.setVisibility(View.VISIBLE);
                picarr.setAdapter(new DynamicGridAdapter(array1, mContext));
                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, array1);
                    }
                });
            } else {
                picarr.setVisibility(View.GONE);
            }
        }
        /**
         * 通过设置控件高度，使GirdView最多显示九张图片
         */
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) picarr.getLayoutParams();
        params.height=950;//设置当前控件布局的高度
        picarr.setLayoutParams(params);//将设置好的布局参数应用到控件中


        content.setText(s.getForum_content());
        time.setText(s.getTime());
        Glide.with(mContext).load(s.getUser_img()).into(circleImageView);
        name.setText(s.getUser_nickname());
        mShareView.setText(s.getZfcount());
        mZanView.setText(s.getZan());

         //zanstate 1代表已点赞 0代表未点赞
        if ("1".equals(s.getZanstate())){
            mZanView.setTextColor(Color.parseColor("#FD4A2E"));
            image.setImageResource(R.mipmap.follow_down);
            type=1;
        }else if ("0".equals(s.getZanstate())){
            mZanView.setTextColor(Color.parseColor("#9A9A9A"));
            image.setImageResource(R.mipmap.follow_up);
            type=0;
        }


        /**
         * 点赞
         */
        mClick_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int aa=Integer.parseInt(s.getZan());


                if (type==1){
                    mZanView.setText(String.valueOf(aa-1));
                    mZanView.setTextColor(Color.parseColor("#9A9A9A"));
                    image.setImageResource(R.mipmap.follow_up);
                    click_up("qxzan",s.getForum_id());
                }else if (type==0){
                    if (flag==0){
                        mZanView.setText(String.valueOf(aa+1));
                        mZanView.setTextColor(Color.parseColor("#FD4A2E"));
                        image.setImageResource(R.mipmap.follow_down);
                        click_up("zan",s.getForum_id());
                        flag=1;
                    }else{
                        mZanView.setText(String.valueOf(aa));
                        mZanView.setTextColor(Color.parseColor("#9A9A9A"));
                        image.setImageResource(R.mipmap.follow_up);
                        click_up("qxzan",s.getForum_id());
                        flag=0;
                    }
                }
            }
        });
        /**
         * 评论
         */
        mComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, PostDetailsActivity.class);
                intent.putExtra("forum_id",s.getForum_id());
                mContext.startActivity(intent);
            }
        });


        if ("0".equals(s.getI())){
            mheader.setVisibility(View.VISIBLE);
        }else{
            mheader.setVisibility(View.GONE);
        }
    }

    /**
     * 点赞功能
     */
    private void click_up(String click,String type_id){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mToken);
        map.put("type","forum");
        map.put("action",click);
        map.put("type_id",type_id);
        map.put("userid",mUser_id);
        HttpUtil.postHttpRequstProgess(mContext, "正在加载", UrlConstant.CLICK_UP, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("点赞数据"+response);
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
        }, "data1", null);
    }

    /**
     * 点击图片放大浏览
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }
}
