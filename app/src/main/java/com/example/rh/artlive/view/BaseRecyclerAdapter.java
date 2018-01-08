package com.example.rh.artlive.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by color on 16/5/31.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private int mHeaderViewId=0;
    protected List<T> mHeadDatas=new ArrayList<>();

    // 华丽的分割线
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Integer position;
    private OnItemClickListener mOnItemClickListener;



    public void setHeaderView(int headerViewId) {
        mHeaderViewId = headerViewId;
        notifyItemInserted(0);
    }

    public void setHeadDatas(List<T> datas) {
        mHeadDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderViewId == 0) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    //华丽的分割线
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public BaseRecyclerAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    public void setData(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        BaseRecyclerViewHolder viewHolder;

        if(mHeaderViewId != 0 && viewType == TYPE_HEADER){// 自增
            viewHolder = BaseRecyclerViewHolder.get(mContext, null, parent, mHeaderViewId, -1);
            return viewHolder;
        }else {
            viewHolder = BaseRecyclerViewHolder.get(mContext, null, parent, mLayoutId, -1);
            setListener(parent, viewHolder, viewType);
            return viewHolder;
        }
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final BaseRecyclerViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position);
                }
            }
        });


        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {


        if(getItemViewType(position) == TYPE_HEADER){
            if(mHeadDatas.size()==0){
                return;
            }else {
                holder.updatePosition(position);
                convert(holder, mHeadDatas.get(position));
            }
        }else {
            if(mHeaderViewId>0){
                holder.updatePosition(position-1);
                convert(holder, mDatas.get(position-1));
            }else {
                holder.updatePosition(position);
                convert(holder, mDatas.get(position));
            }
        }
    }

    public abstract void convert(BaseRecyclerViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mHeaderViewId == 0 ? mDatas.size() : mDatas.size() + 1;
    }

//    protected void rotateyAnimRunHide(final View view)
//    {
//        final ObjectAnimator anim = ObjectAnimator//
//                .ofFloat(view, "zhy", 1.0F,  0.0F)//
//                .setDuration(500);//
//        anim.start();
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
//        {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation)
//            {
//                float cVal = (Float) animation.getAnimatedValue();
//                view.setAlpha(cVal);
//                view.setScaleX(cVal);
//                view.setScaleY(cVal);
//            }
//        });
//        anim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                view.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    protected void rotateyAnimRunShow(final View view)
//    {
//        final ObjectAnimator anim = ObjectAnimator//
//                .ofFloat(view, "zhy", 0.0F,  1.0F)//
//                .setDuration(500);//
//        anim.start();
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
//        {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation)
//            {
//                float cVal = (Float) animation.getAnimatedValue();
//                view.setAlpha(cVal);
//                view.setScaleX(cVal);
//                view.setScaleY(cVal);
//            }
//        });
//    }
}
