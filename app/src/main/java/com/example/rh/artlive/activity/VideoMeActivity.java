package com.example.rh.artlive.activity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.MediaController;
import com.example.rh.artlive.util.SuperVideoPlayer;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by rh on 2017/12/20.
 */

public class VideoMeActivity extends BaseFragmentActivity implements View.OnClickListener{

    private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;
    private ImageView mImageView;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melt);


        mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        mPlayBtnView = findViewById(R.id.play_btn);
        mPlayBtnView.setOnClickListener(this);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        mImageView=(ImageView)findViewById(R.id.image);
//        final Uri uril = Uri.parse("http://bos.nj.bpc.baidu.com/tieba-smallvideo/11772_3c435014fb2dd9a5fd56a57cc369f6a0.mp4");
//        //获取视频的第一帧http://192.168.159.2:8080/Four/play.action
//        FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
//        try {
//            fmmr.setDataSource(String.valueOf(uril));
//            bitmap = fmmr.getFrameAtTime();
//            if (bitmap != null) {
//                Bitmap b2 = fmmr.getFrameAtTime(4000000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);//时间戳附近
//                if (b2 != null) {
//                    bitmap = b2;
//                }
//                if (bitmap.getWidth() > 640) {// 如果图片宽度规格超过640px,则进行压缩
//                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 640, 480, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//                }
//            }
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//        } finally {
//            fmmr.release();
//            //释放对象
//        }
//        mImageView.setImageBitmap(bitmap);

    }


    /**
     * 播放器的回调函数
     */
    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        /**
         * 播放器关闭按钮回调
         */
        @Override
        public void onCloseVideo() {
            mSuperVideoPlayer.close();//关闭VideoView
            mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
//            resetPageToPortrait();
        }

        /**
         * 播放器横竖屏切换回调
         */
        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
                Log.e("竖屏");
            } else {
                setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
                Log.e("横屏");
            }
        }

        /**
         * 播放完成回调
         */
        @Override
        public void onPlayFinish() {

        }
    };

    @Override
    public void onClick(View view) {
        mPlayBtnView.setVisibility(View.GONE);
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        mSuperVideoPlayer.setAutoHideController(false);
        Uri uri = Uri.parse("http://bos.nj.bpc.baidu.com/tieba-smallvideo/11772_3c435014fb2dd9a5fd56a57cc369f6a0.mp4");//http://bos.nj.bpc.baidu.com/tieba-smallvideo/11772_3c435014fb2dd9a5fd56a57cc369f6a0.mp4
        //http://192.168.5.199:8080/fileOpre/FileUpload?fileName=11772_3c435014fb2dd9a5fd56a57cc369f6a0.mp4
        mSuperVideoPlayer.loadAndPlay(uri,0);
    }
}
