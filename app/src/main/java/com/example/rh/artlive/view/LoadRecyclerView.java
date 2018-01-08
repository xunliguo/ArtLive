package com.example.rh.artlive.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

/**
 * 上拉加载的RecycleView
 * Created by color on 16/5/31.
 */

public class LoadRecyclerView extends RecyclerView implements Pullable,ColorUiInterface {



    private int attr_background = -1;
    int totalItemCount; //总item数量
    int lastVisibleItem; //最后一个显示的item的位置
    private LoadListener loadListener;
    //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
    boolean isSlidingToLast = false;
    private boolean isHaveData = false; // 标示是否还有数据可加载

    Context mContext;
    RecyclerView recyclerView;


    public void setIsHaveData(boolean isHaveData) {
        this.isHaveData = isHaveData;
    }

    public void setLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public LoadRecyclerView(Context context){
        super(context);
    }

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attr_background = ViewAttributeUtil.getBackgroundAttibute(attrs);
        init(context);
    }

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.attr_background = ViewAttributeUtil.getBackgroundAttibute(attrs);
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        recyclerView = this;

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        //获取最后一个完全显示的ItemPosition
                        LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = manager.getItemCount();
                        // 判断是否滚动到底部，并且是向右滚动
                        if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                            //加载更多功能的代码
//                        Ln.e("howes right=" + manager.findLastCompletelyVisibleItemPosition());
                            if (!isHaveData) {
//                                loadListener.onLoad();
                            } else {
                                loadListener.loadFinish();
                            }
                        }
                    } else if (layoutManager instanceof GridLayoutManager) {
                        GridLayoutManager manager = (GridLayoutManager) layoutManager;
                        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = manager.getItemCount();
                        // 判断是否滚动到底部，并且是向右滚动
                        if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                            //加载更多功能的代码
                            if (!isHaveData) {
//                                loadListener.onLoad();
                            } else {
                                loadListener.loadFinish();
                            }
                        }
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {

                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        int column = staggeredGridLayoutManager.getColumnCountForAccessibility(null, null);
                        int[] positions = new int[column];
                        staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);

                        for (int i = 0; i < positions.length; i++) {
                            if (positions[i] >= staggeredGridLayoutManager.getItemCount() - column
                                    &&
                                    staggeredGridLayoutManager.findViewByPosition(positions[i]).getBottom() <= getHeight()) {
                                if (!isHaveData) {
//                                    loadListener.onLoad();
                                } else {
                                    loadListener.loadFinish();
                                }

                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    //大于0表示，正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0 表示停止或向左滚动
                    isSlidingToLast = false;
                }
            }
        });
    }

    /**
     * 判断列表是否可以上滑
     * @param mTarget
     * @return
     */
    public boolean canChildScrollUp(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * 判断列表是否滑到底部
     * @param recyclerView
     * @return
     */
    public static boolean isVisBottom(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();
        if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean canPullDown() {
        if(canChildScrollUp(recyclerView)){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean canPullUp(){

        if(isVisBottom(recyclerView)&&isHaveData){
                return true;
        }else{
                return false;
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setTheme(Resources.Theme themeId) {
        if(attr_background != -1) {
            ViewAttributeUtil.applyBackgroundDrawable(this, themeId, attr_background);
        }
    }


    public interface LoadListener {

        void onLoad();

        void loadFinish();

    }
}
