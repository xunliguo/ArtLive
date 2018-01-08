package com.example.rh.artlive.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.AppRecordEvent;
import com.example.rh.artlive.bean.FansBean;
import com.example.rh.artlive.bean.ReadBean;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.recorder.LyricView;
import com.example.rh.artlive.recorder.PlaybackDialogFragment;
import com.example.rh.artlive.recorder.RecordAudioDialogFragment;
import com.example.rh.artlive.recorder.RecordingItem;
import com.example.rh.artlive.recorder.RecordingService;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/20.
 */

public class RecordActivity extends BaseFragmentActivity implements View.OnClickListener{
    private static final int REQUEST_CODE_PICK_CITY = 233;



    private ImageView mShowDraw;
    private ImageView mBackMusicView;
    private Context context;
    private RelativeLayout mRecordLayout;
    private LinearLayout mRecordPlayLayout;
    private ImageView mBack;
    private ImageView mBack_Image;

    LyricView view;
    ArrayList<String> list;
    ArrayList<Long> list1;
    List<String> tea_list ;

    private String mBackReading;
    private String mBackReading_id;
    private String mBackReading_title;
    private String mBackPhoto;
    MediaPlayer mediaPlayer;
    private Button mButton;

    private RelativeLayout mSign_Red;
    private RelativeLayout mAgin_Red;
    //录音
    private static final String TAG = "RecordAudioDialogFragme";
    private int mRecordPromptCount = 0;
    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;
    long timeWhenPaused = 0;
    private FloatingActionButton mFabRecord;
    private Chronometer mChronometerTime;
    private ImageView mIvClose;
    private File folder;
    //播放
    private static final String LOG_TAG = "PlaybackFragment";

    private static final String ARG_ITEM = "recording_item";
    private RecordingItem item;

    private Handler mHandler = new Handler();

    private MediaPlayer mMediaPlayer = null;

    private SeekBar mSeekBar = null;
    private FloatingActionButton mPlayButton = null;
    private TextView mCurrentProgressTextView = null;
    private TextView mFileNameTextView = null;
    private TextView mFileLengthTextView = null;

    //stores whether or not the mediaplayer is currently playing audio
    private boolean isPlaying = false;

    //stores minutes and seconds of the length of the file.
    long minutes = 0;
    long seconds = 0;
    private static long mFileLength = 0;


    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }
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
    @Subscribe
    public void setContent(AppRecordEvent data) {
        if ("image".equals(data.getImage())){
            Log.e("获取的图片"+data.getName());
            Glide.with(this).load(data.getName()).into(mBack_Image);
        }else if ("image".equals(data.getImage())){
            startMusic(data.getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        context=this;
        init();
        setListener();
    }

    private void init(){
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        Intent intent=getIntent();
        mBackReading=intent.getStringExtra("back_content");
        mBackReading_id=intent.getStringExtra("red_id");
        mBackReading_title=intent.getStringExtra("red_title");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mBackMusicView=(ImageView)findViewById(R.id.background_music);
        view = (LyricView)findViewById(R.id.view);
        mButton=(Button)findViewById(R.id.login_btn);
        mBack=(ImageView)findViewById(R.id.add_back_image);
        mBack_Image=(ImageView)findViewById(R.id.back_image);


        mRecordLayout=(RelativeLayout)findViewById(R.id.record_bottom);
        mSign_Red=(RelativeLayout)findViewById(R.id.sign_red);
        mAgin_Red=(RelativeLayout)findViewById(R.id.agin_red);
        mRecordPlayLayout=(LinearLayout) findViewById(R.id.record_play);

        mChronometerTime = (Chronometer) findViewById(R.id.record_audio_chronometer_time);
        mFabRecord = (FloatingActionButton) findViewById(R.id.record_audio_fab_record);
        mFabRecord.setColorNormal(getResources().getColor(R.color.orange));
        mFabRecord.setColorPressed(getResources().getColor(R.color.orange));

        mFileNameTextView = (TextView) findViewById(R.id.file_name_text_view);
        mFileLengthTextView = (TextView) findViewById(R.id.file_length_text_view);
        mCurrentProgressTextView = (TextView) findViewById(R.id.current_progress_text_view);
        mPlayButton = (FloatingActionButton)findViewById(R.id.fab_play);
        //取字符串，换行显示歌词
        if (mBackReading!=null){
            Log.e("读物"+mBackReading);
            String[] result1 = mBackReading.split("，");
            for (int j=0;j<result1.length;j++){
                list.add(result1[j]);
                list1.add(0l);
            }
            /**
             * 把歌词数组放入LyricView
             */
            mButton.setVisibility(View.GONE);
            view.setLyricText(list, list1);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.scrollToIndex(1);
                }
            }, 1000);
        }
        //播放初始化

    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mFabRecord.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mBackMusicView.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mSign_Red.setOnClickListener(this);
        mAgin_Red.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    /**
     * 播放背景音乐
     */
    private void startMusic(String music) {
        try {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(music);//设置播放的数据源。
            Log.e("背景音乐"+music);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mediaPlayer.prepare();//同步的准备方法。
            mediaPlayer.prepareAsync();//异步的准备
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 录音播放初始化控件等等
     */
    private void mRecordPlay(){
        RecordingItem recordingItem = new RecordingItem();
        //获取音频路径以及时长
        SharedPreferences sharePreferences = getSharedPreferences("sp_name_audio", MODE_PRIVATE);
        final String filePath = sharePreferences.getString("audio_path", "");
        long elpased = sharePreferences.getLong("elpased", 0);
        item=new RecordingItem();
        recordingItem.setFilePath(filePath);
        recordingItem.setLength((int) elpased);
        item.setFilePath(filePath);
        item.setLength((int) elpased);
        item.setName(mBackReading_title);

        mFileNameTextView.setText(item.getName());
        mFileLengthTextView.setText(String.format("%02d:%02d", minutes,seconds));

        Log.e("播放音频"+filePath);
        long itemDuration = sharePreferences.getLong("elpased", 0);
        mFileLength = sharePreferences.getLong("elpased", 0);
        minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        ColorFilter filter = new LightingColorFilter
                (getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
        mSeekBar.getProgressDrawable().setColorFilter(filter);
        mSeekBar.getThumb().setColorFilter(filter);
        mFileLengthTextView.setText(String.valueOf(mFileLength));
        /**
         * 进度条拖动事件
         */
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mHandler.removeCallbacks(mRunnable);

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);
                    mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes,seconds));
                    updateSeekBar();
                } else if (mMediaPlayer == null && fromUser) {
                    prepareMediaPlayerFromPoint(progress);
                    updateSeekBar();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mMediaPlayer != null) {
                    // remove message Handler from updating progress bar
                    mHandler.removeCallbacks(mRunnable);
                }
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer != null) {
                    mHandler.removeCallbacks(mRunnable);
                    mMediaPlayer.seekTo(seekBar.getProgress());
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);
                    mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes,seconds));
                    updateSeekBar();
                }
            }
        });
        testUpload(filePath);
    }
    /**
     * 点击开始录音，开启服务
     * @param start
     */
    private void onRecord(boolean start) {
        Intent intent = new Intent(RecordActivity.this, RecordingService.class);

        if (start) {
            // start recording
            mFabRecord.setImageResource(R.drawable.ic_media_stop);
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(RecordActivity.this, "开始录音...", Toast.LENGTH_SHORT).show();
            folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir();
            }
            //start Chronometer
            mChronometerTime.setBase(SystemClock.elapsedRealtime());
            mChronometerTime.start();

            //start RecordingService
            context.startService(intent);
            //keep screen on while recording
//            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            //stop recording
            mRecordLayout.setVisibility(View.GONE);
            mRecordPlayLayout.setVisibility(View.VISIBLE);
            mFabRecord.setImageResource(R.drawable.ic_mic_white_36dp);
            //mPauseButton.setVisibility(View.GONE);
            mChronometerTime.stop();
            timeWhenPaused = 0;
            Toast.makeText(RecordActivity.this, "录音结束...", Toast.LENGTH_SHORT).show();

            context.stopService(intent);
            //allow the screen to turn off again once recording is finished
            RecordActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.record_audio_fab_record:
                if (mBackReading==null){
                    ToastUtil.showToast(this,"请选择读物");
                }else {
                    mRecordLayout.setVisibility(View.VISIBLE);
                    mRecordPlayLayout.setVisibility(View.GONE);
                    //动态添加权限
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RecordActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
                    } else {
                        onRecord(mStartRecording);
                        mStartRecording = !mStartRecording;
                    }
                }
                break;
            case R.id.fab_play:
                onPlay(isPlaying);
                isPlaying = !isPlaying;
                break;
            case R.id.background_music:
                Intent intent=new Intent(RecordActivity.this,BackMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn:
                Intent intent1=new Intent(RecordActivity.this,ReadingActivity.class);
                startActivity(intent1);
                break;
            case R.id.agin_red:
                mRecordLayout.setVisibility(View.VISIBLE);
                mRecordPlayLayout.setVisibility(View.GONE);
                //动态添加权限
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RecordActivity.this
                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
                }else {
                    onRecord(mStartRecording);
                    mStartRecording = !mStartRecording;
                }
                break;
            case R.id.sign_red:
//                Log.e("录音"+testUpload(item.getFilePath()));
                onPlay(isPlaying);
                isPlaying = !isPlaying;
                Intent intent2=new Intent(RecordActivity.this,RecordEditActivity.class);
                intent2.putExtra("voice",testUpload(item.getFilePath()));
                intent2.putExtra("record",mBackReading_id);
                intent2.putExtra("title",mBackReading_title);
                intent2.putExtra("type","up");
                startActivity(intent2);
                break;
            case R.id.add_back_image:
                Intent intent3=new Intent(RecordActivity.this,RecordBackActivity.class);
                startActivity(intent3);
                break;
        }
    }



    /**
     * 播放，继续
     * @param isPlaying
     */
    private void onPlay(boolean isPlaying){
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if(mMediaPlayer == null) {
                startPlaying(); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }

        } else {
            pausePlaying();
        }
    }

    private void startPlaying() {
        mRecordPlay();
        mPlayButton.setImageResource(R.drawable.ic_media_pause);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(item.getFilePath());

            Log.e("路径"+item.getFilePath());
            mSharePreferenceUtil.setString(SharedPerfenceConstant.VIDEO_PATH,item.getFilePath());
            mMediaPlayer.prepare();
            mSeekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            android.util.Log.e(LOG_TAG, "prepare() failed");
        }
       //停止播放
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });
        //更新进度条
        updateSeekBar();

        //keep screen on while playing audio
        RecordActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 进度条的拖动
     * @param progress
     */
    private void prepareMediaPlayerFromPoint(int progress) {
        //set mediaPlayer to start from middle of the audio file

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(item.getFilePath());
            mMediaPlayer.prepare();
            mSeekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(progress);

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });

        } catch (IOException e) {
            android.util.Log.e(LOG_TAG, "prepare() failed");
        }

        //keep screen on while playing audio
        RecordActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void pausePlaying() {
        mPlayButton.setImageResource(R.drawable.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.pause();
    }

    private void resumePlaying() {
        mPlayButton.setImageResource(R.drawable.ic_media_pause);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        updateSeekBar();
    }

    private void stopPlaying() {
        mPlayButton.setImageResource(R.drawable.ic_media_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

        mSeekBar.setProgress(mSeekBar.getMax());
        isPlaying = !isPlaying;

        mCurrentProgressTextView.setText(mFileLengthTextView.getText());
        mSeekBar.setProgress(mSeekBar.getMax());

        //allow the screen to turn off again once audio is finished playing
        RecordActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 更新进度条
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mMediaPlayer != null){

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                mSeekBar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 1000);
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
