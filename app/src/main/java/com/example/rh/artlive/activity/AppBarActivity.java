package com.example.rh.artlive.activity;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.fragment.FollowFragment;
import com.example.rh.artlive.fragment.HomeFragment;
import com.example.rh.artlive.fragment.HotFragment;
import com.example.rh.artlive.view.CustomViewPager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/11/25.
 */

public class AppBarActivity extends BaseFragmentActivity  implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{
    private MediaPlayer mMediaPlayer=null;//媒体播放器
    private AudioManager mAudioManager=null;//声音管理器
    private Button mPlayButton=null;
    private Button mPauseButton=null;
    private Button mStopButton=null;
    private SeekBar mSoundSeekBar=null;
    private int maxStreamVolume;//最大音量
    private int currentStreamVolume;//当前音量
    private int setStreamVolume;//设置的音量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mn);
        mMediaPlayer=new MediaPlayer();//加载res/raw的happyis.mp3文件
        try {
            mMediaPlayer.setDataSource("http://app.cmepa.com/Uploads/record/2017-11-30/5a1ff9d397660.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAudioManager=(AudioManager)this.getSystemService(AUDIO_SERVICE);
        mPlayButton=(Button)findViewById(R.id.Play);
        mPauseButton=(Button)findViewById(R.id.Pause);
        mStopButton=(Button)findViewById(R.id.Stop);
        mSoundSeekBar=(SeekBar)findViewById(R.id.SoundSeekBar);
        mPlayButton.setOnClickListener(this);
        mPauseButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
        maxStreamVolume=mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentStreamVolume=mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundSeekBar.setMax(maxStreamVolume);
        mSoundSeekBar.setProgress(currentStreamVolume);
        mSoundSeekBar.setOnSeekBarChangeListener(this);
    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.Play:
                mMediaPlayer.start();
                break;
            case R.id.Pause:
                mMediaPlayer.pause();
                break;
            case R.id.Stop:
                System.out.println("Stop");
                mMediaPlayer.stop();
                try{
                    mMediaPlayer.prepare();
                }catch(IllegalStateException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
                mMediaPlayer.seekTo(0);
                break;
            default:
                break;
        }
    }

    //进度条变化
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // TODO Auto-generated method stub
        System.out.println("progress:"+String.valueOf(progress));
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        super.onBackPressed();
    }
}
