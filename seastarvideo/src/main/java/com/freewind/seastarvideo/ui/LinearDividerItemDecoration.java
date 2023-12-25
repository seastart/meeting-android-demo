/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.ui;

/**
 * @author wiatt
 * @description:
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freewind.seastarvideo.utils.DisplayUtil;

/**
 * @author wiatt
 * @description:
 */
public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration{
    public static String TAG = LinearDividerItemDecoration.class.getSimpleName();
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private int mOrientation;
    private Context mContext;
    private TextPaint mTextPaint;
    private float listDividerSize = 2;
    private int listDividerColor = Color.parseColor("#eeeeee");

    private int mDividerWidthPadding = DisplayUtil.getInstance().dip2px(16); // 分割线宽度缩进，默认缩进 16dp

    public LinearDividerItemDecoration(Context context, int orientation, int listDividerSize){
        mContext = context;
        mTextPaint = new TextPaint();
        mTextPaint.setColor(this.listDividerColor);
        this.listDividerSize = listDividerSize;
        setOrientation(orientation);
    }

    public LinearDividerItemDecoration(Context context, int orientation, int listDividerSize, int dividerWidthPadding, int listDividerColor){
        mContext = context;
        mTextPaint = new TextPaint();
        this.listDividerColor = listDividerColor;
        mTextPaint.setColor(this.listDividerColor);
        this.listDividerSize = listDividerSize;
        this.mDividerWidthPadding = dividerWidthPadding;
        setOrientation(orientation);
    }
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        super.onDraw(c, parent);
        if(mOrientation == HORIZONTAL_LIST){
            drawHorizontal(c, parent);
        }else{
            drawVertical(c, parent);
        }
    }
    /**
     * 绘制垂直列表的分割线
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        //分割线的左边界 = 子View的左padding值
        int rectLeft = parent.getPaddingLeft() + mDividerWidthPadding;
        //分割线的右边界 = 子View的宽度 - 子View的右padding值
        int rectRight = parent.getWidth() - parent.getPaddingRight() - mDividerWidthPadding;
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount - 1; i ++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            // 分割线的top = 子View的底部 + 子View的margin值
            int rectTop = child.getBottom() + layoutParams.bottomMargin;
            // 分割线的bottom = 分割线的top + 分割线的高度
            float rectBottom = rectTop + listDividerSize;
            c.drawRect(rectLeft,rectTop,rectRight,rectBottom,mTextPaint);
        }
    }

    /**
     * 绘制水平列表的分割线
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        //分割线的上边界 = 子View的上padding值
        int rectTop = parent.getPaddingTop() + mDividerWidthPadding;
        //分割线的下边界 = 子View的高度 - 子View的底部padding值
        int rectBottom = parent.getHeight() - parent.getPaddingBottom() - mDividerWidthPadding;
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i ++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            //分割线的Left = 子View的右边界 + 子View的左margin值
            int rectLeft = child.getRight() + layoutParams.rightMargin;
            //分割线的right = 分割线的Left + 分割线的宽度
            float rectRight = rectLeft + listDividerSize;
            c.drawRect(rectLeft,rectTop,rectRight,rectBottom,mTextPaint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(mOrientation == VERTICAL_LIST){
            outRect.set(0,0,0,(int)listDividerSize);
        } else{
            outRect.set(0,0,(int)listDividerSize,0);
        }
    }
}
