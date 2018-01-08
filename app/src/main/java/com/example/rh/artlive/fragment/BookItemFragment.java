package com.example.rh.artlive.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.BookPayActivity;
import com.example.rh.artlive.activity.WenChangActivity;
import com.example.rh.artlive.articlechoice.NoScrollListview;
import com.example.rh.artlive.articlechoice.OptionsListAdapter;
import com.example.rh.artlive.articlechoice.QuestionBean;
import com.example.rh.artlive.bean.BookIntroBean;
import com.example.rh.artlive.bean.CorrectBean;
import com.example.rh.artlive.live.DemoActivity;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/20.
 */

public class BookItemFragment extends BaseFragment implements View.OnClickListener ,TextToSpeech.OnInitListener {

    private View view;
    BookIntroBean bookIntroBean;
    int index;
    LocalBroadcastManager mLocalBroadcastManager;

    private TextView mContentView;
    private LinearLayout mClickLayout;
    private Dialog datePickerDialog;

    private TextToSpeech textToSpeech; // TTS对象


    public BookItemFragment(int index) {
        this.index = index;
        bookIntroBean = BookPayActivity.bookBeanList.get(index);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        textToSpeech.shutdown(); // 关闭，释放资源
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        view = inflater.inflate(R.layout.book_item, container, false);
        init();
        setListener();
        return view;
    }

    private void init() {

        TextView name=(TextView)view.findViewById(R.id.chapter);
        mContentView=(TextView)view.findViewById(R.id.book_content);
        mClickLayout=(LinearLayout)view.findViewById(R.id.click_content);
        name.setText(bookIntroBean.getName());
        mContentView.setText("        "+bookIntroBean.getContent());
        textToSpeech = new TextToSpeech(getActivity(), this); // 参数Context,TextToSpeech.OnInitListener

    }

    private void setListener() {
        mClickLayout.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_content:
                initDialog();
                break;
        }
    }

    /**
     * 添加直播选择
     */
    private void initDialog() {
        datePickerDialog = new Dialog(getActivity(), R.style.time_dialog);
        datePickerDialog.setCancelable(false);
        datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePickerDialog.setContentView(R.layout.custom_book_picker);
        Window window = datePickerDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        window.setAttributes(lp);
        datePickerDialog.show();

        TextView cancel=datePickerDialog.findViewById(R.id.tv_cancle);
        final TextView sum=datePickerDialog.findViewById(R.id.tv_select);
        TextView selected_class=datePickerDialog.findViewById(R.id.selected_class);
        final EditText editText=datePickerDialog.findViewById(R.id.phone);
        final LinearLayout one=datePickerDialog.findViewById(R.id.one);
        final LinearLayout third=datePickerDialog.findViewById(R.id.third);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.leyikao.jumptopage");
                mLocalBroadcastManager.sendBroadcast(intent);
                datePickerDialog.dismiss();
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent("com.leyikao.jumptonext");
                mLocalBroadcastManager.sendBroadcast(intent1);
                datePickerDialog.dismiss();
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                    textToSpeech.setPitch(0.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                    textToSpeech.speak(mContentView.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }
    /**
     * 用来初始化TextToSpeech引擎
     * status:SUCCESS或ERROR这2个值
     * setLanguage设置语言，帮助文档里面写了有22种
     * TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失。
     * TextToSpeech.LANG_NOT_SUPPORTED:不支持
     */
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getActivity(), "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
