package com.example.rh.artlive.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.VideoBean;
import com.example.rh.artlive.bean.WormBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.MediaController;
import com.example.rh.artlive.util.SuperVideoPlayer;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/11.
 */

public class VideoAdapter extends BaseRecyclerAdapter<VideoBean> {

    private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;

    public VideoAdapter(Context context, int layoutId, List<VideoBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, final VideoBean s) {

        TextView title=holder.getView(R.id.video_title);
        mSuperVideoPlayer=holder.getView(R.id.video_player_item_1);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        mPlayBtnView=holder.getView(R.id.play_btn);
        title.setText(s.getVideo_title());

        /**
         * 点击播放
         */
        mPlayBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayBtnView.setVisibility(View.GONE);
                mSuperVideoPlayer.setVisibility(View.VISIBLE);
                mSuperVideoPlayer.setAutoHideController(false);
                Uri uri = Uri.parse(s.getVideo_url());//http://bos.nj.bpc.baidu.com/tieba-smallvideo/11772_3c435014fb2dd9a5fd56a57cc369f6a0.mp4
                //http://192.168.5.199:8080/fileOpre/FileUpload?fileName=11772_3c435014fb2dd9a5fd56a57cc369f6a0.mp4
                mSuperVideoPlayer.loadAndPlay(uri,0);
            }
        });

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
//            if (mContext.getRequestedOrientation() == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
//                Log.e("竖屏");
//            } else {
//                setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
//                Log.e("横屏");
//            }
        }

        /**
         * 播放完成回调
         */
        @Override
        public void onPlayFinish() {

        }
    };
}
