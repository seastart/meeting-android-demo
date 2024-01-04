package com.freewind.seastarvideo.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.appcompat.widget.AppCompatTextView;

import com.freewind.seastarvideo.R;


/**
 * author superK
 * update_at 2019/10/24
 * description xml直接控制selector的TextView
 */
public class StateTextView extends AppCompatTextView {

    //text color
    private int mNormalTextColor = 0;
    private int mFocusTextColor = 0;
    private int mPressedTextColor = 0;
    private int mUnableTextColor = 0;
    ColorStateList mTextColorStateList;

    //animation duration
    private int mDuration = 0;

    //radius
    private float mRadius = 0;
    private float mTopLeftRadius = 0;
    private float mTopRightRadius = 0;
    private float mBottomLeftRadius = 0;
    private float mBottomRightRadius = 0;
    private boolean mRound;

    //stroke
    private float mStrokeDashWidth = 0;
    private float mStrokeDashGap = 0;
    private int mNormalStrokeWidth = 0;
    private int mFocusStrokeWidth = 0;
    private int mPressedStrokeWidth = 0;
    private int mUnableStrokeWidth = 0;
    private int mNormalStrokeColor = 0;
    private int mFocusStrokeColor = 0;
    private int mPressedStrokeColor = 0;
    private int mUnableStrokeColor = 0;

    //background color
    private int mNormalBackgroundColor = 0;
    private int mFocusBackgroundColor = 0;
    private int mPressedBackgroundColor = 0;
    private int mUnableBackgroundColor = 0;

    private GradientDrawable mNormalBackground;
    private GradientDrawable mFocusBackground;
    private GradientDrawable mPressedBackground;
    private GradientDrawable mUnableBackground;

    private int[][] states;

    StateListDrawable mStateBackground;

    public StateTextView(Context context) {
        this(context, null);
    }

    public StateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.titleTextStyle);
    }

    public StateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {

        states = new int[4][];

        Drawable drawable = getBackground();
        if(drawable instanceof StateListDrawable){
            mStateBackground = (StateListDrawable) drawable;
        }else{
            mStateBackground = new StateListDrawable();
        }

        mNormalBackground = new GradientDrawable();
        mFocusBackground = new GradientDrawable();
        mPressedBackground = new GradientDrawable();
        mUnableBackground = new GradientDrawable();

        //pressed, focused, normal, unable
        states[0] = new int[] { android.R.attr.state_enabled, android.R.attr.state_pressed };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { -android.R.attr.state_enabled};

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateTextView);

        //get original text color as default
        //set text color
        mTextColorStateList = getTextColors();
        int mDefaultPressedTextColor = mTextColorStateList.getColorForState(states[0], getCurrentTextColor());
        int mDefaultFocusTextColor = mTextColorStateList.getColorForState(states[1], getCurrentTextColor());
        int mDefaultNormalTextColor = mTextColorStateList.getColorForState(states[2], getCurrentTextColor());
        int mDefaultUnableTextColor = mTextColorStateList.getColorForState(states[3], getCurrentTextColor());
        mNormalTextColor = a.getColor(R.styleable.StateTextView_normalTextColor, mDefaultNormalTextColor);
        mFocusTextColor = a.getColor(R.styleable.StateTextView_focusTextColor, mDefaultFocusTextColor);
        mPressedTextColor = a.getColor(R.styleable.StateTextView_pressedTextColor, mDefaultPressedTextColor);
        mUnableTextColor = a.getColor(R.styleable.StateTextView_unableTextColor, mDefaultUnableTextColor);
        setTextColor();

        //set animation duration
        mDuration = a.getInteger(R.styleable.StateTextView_animationDuration, mDuration);
        mStateBackground.setEnterFadeDuration(mDuration);
        mStateBackground.setExitFadeDuration(mDuration);

        //set background color
        mNormalBackgroundColor = a.getColor(R.styleable.StateTextView_normalBackgroundColor, 0);
        mFocusBackgroundColor = a.getColor(R.styleable.StateTextView_focusBackgroundColor, 0);
        mPressedBackgroundColor = a.getColor(R.styleable.StateTextView_pressedBackgroundColor, 0);
        mUnableBackgroundColor = a.getColor(R.styleable.StateTextView_unableBackgroundColor, 0);
        mNormalBackground.setColor(mNormalBackgroundColor);
        mFocusBackground.setColor(mFocusBackgroundColor);
        mPressedBackground.setColor(mPressedBackgroundColor);
        mUnableBackground.setColor(mUnableBackgroundColor);

        //set radius
        mRadius = a.getDimensionPixelSize(R.styleable.StateTextView_stateRadius, 0);
        if (mRadius != 0) {
            mNormalBackground.setCornerRadius(mRadius);
            mFocusBackground.setCornerRadius(mRadius);
            mPressedBackground.setCornerRadius(mRadius);
            mUnableBackground.setCornerRadius(mRadius);
        } else {
            mTopLeftRadius = a.getDimensionPixelSize(R.styleable.StateTextView_stateTopLeftRadius, 0);
            mTopRightRadius = a.getDimensionPixelSize(R.styleable.StateTextView_stateTopRightRadius, 0);
            mBottomLeftRadius = a.getDimensionPixelSize(R.styleable.StateTextView_stateBottomLeftRadius, 0);
            mBottomRightRadius = a.getDimensionPixelSize(R.styleable.StateTextView_stateBottomRightRadius, 0);
            float[] radii = {mTopLeftRadius, mTopLeftRadius,
                    mTopRightRadius, mTopRightRadius,
                    mBottomRightRadius, mBottomRightRadius,
                    mBottomLeftRadius, mBottomLeftRadius
            };
            mNormalBackground.setCornerRadii(radii);
            mFocusBackground.setCornerRadii(radii);
            mPressedBackground.setCornerRadii(radii);
            mUnableBackground.setCornerRadii(radii);
        }


        mRound = a.getBoolean(R.styleable.StateTextView_stateRound, false);


        //set stroke
        mStrokeDashWidth = a.getDimensionPixelSize(R.styleable.StateTextView_strokeDashWidth, 0);
        mStrokeDashGap = a.getDimensionPixelSize(R.styleable.StateTextView_strokeDashWidth, 0);
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.StateTextView_normalStrokeWidth, 0);
        mFocusStrokeWidth = a.getDimensionPixelSize(R.styleable.StateTextView_focusStrokeWidth, 0);
        mPressedStrokeWidth = a.getDimensionPixelSize(R.styleable.StateTextView_pressedStrokeWidth, 0);
        mUnableStrokeWidth = a.getDimensionPixelSize(R.styleable.StateTextView_unableStrokeWidth, 0);
        mNormalStrokeColor = a.getColor(R.styleable.StateTextView_normalStrokeColor, 0);
        mFocusStrokeColor = a.getColor(R.styleable.StateTextView_focusStrokeColor, 0);
        mPressedStrokeColor = a.getColor(R.styleable.StateTextView_pressedStrokeColor, 0);
        mUnableStrokeColor = a.getColor(R.styleable.StateTextView_unableStrokeColor, 0);
        setStroke();

        //set background
        mStateBackground.addState(states[0], mPressedBackground);
        mStateBackground.addState(states[1], mFocusBackground);
        mStateBackground.addState(states[2], mNormalBackground);
        mStateBackground.addState(states[3], mUnableBackground);
        setBackgroundDrawable(mStateBackground);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setRound(mRound);
    }

    /****************** stroke color *********************/

    public void setNormalStrokeColor(@ColorInt int normalStrokeColor) {
        this.mNormalStrokeColor = normalStrokeColor;
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
    }

    public void setFocusStrokeColor(@ColorInt int focusStrokeColor) {
        this.mFocusStrokeColor = focusStrokeColor;
        setStroke(mFocusBackground, mFocusStrokeColor, mFocusStrokeWidth);
    }

    public void setPressedStrokeColor(@ColorInt int pressedStrokeColor) {
        this.mPressedStrokeColor = pressedStrokeColor;
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
    }

    public void setUnableStrokeColor(@ColorInt int unableStrokeColor) {
        this.mUnableStrokeColor = unableStrokeColor;
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    public void setStateStrokeColor(@ColorInt int normal, @ColorInt int focus, @ColorInt int pressed, @ColorInt int unable){
        mNormalStrokeColor = normal;
        mFocusStrokeColor = focus;
        mPressedStrokeColor = pressed;
        mUnableStrokeColor = unable;
        setStroke();
    }

    /****************** stroke width *********************/

    public void setNormalStrokeWidth(int normalStrokeWidth) {
        this.mNormalStrokeWidth = normalStrokeWidth;
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
    }

    public void setFocusStrokeWidth(int focusStrokeWidth) {
        this.mFocusStrokeWidth = focusStrokeWidth;
        setStroke(mFocusBackground, mFocusStrokeColor, mFocusStrokeWidth);
    }

    public void setPressedStrokeWidth(int pressedStrokeWidth) {
        this.mPressedStrokeWidth = pressedStrokeWidth;
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
    }

    public void setUnableStrokeWidth(int unableStrokeWidth) {
        this.mUnableStrokeWidth = unableStrokeWidth;
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    public void setStateStrokeWidth(int normal, int focus, int pressed, int unable){
        mNormalStrokeWidth = normal;
        mFocusStrokeWidth = focus;
        mPressedStrokeWidth = pressed;
        mUnableStrokeWidth= unable;
        setStroke();
    }

    public void setStrokeDash(float strokeDashWidth, float strokeDashGap) {
        this.mStrokeDashWidth = strokeDashWidth;
        this.mStrokeDashGap = strokeDashWidth;
        setStroke();
    }

    private void setStroke(){
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
        setStroke(mFocusBackground, mFocusStrokeColor, mFocusStrokeWidth);
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    private void setStroke(GradientDrawable mBackground, int mStrokeColor, int mStrokeWidth) {
        mBackground.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap);
    }

    /********************   radius  *******************************/

    public void setRadius(@FloatRange(from = 0) float radius) {
        this.mRadius = radius;
        mNormalBackground.setCornerRadius(mRadius);
        mFocusBackground.setCornerRadius(mRadius);
        mPressedBackground.setCornerRadius(mRadius);
        mUnableBackground.setCornerRadius(mRadius);
    }

    public void setRadii(@FloatRange(from = 0) float mTopLeftRadius, @FloatRange(from = 0) float mTopRightRadius,
                         @FloatRange(from = 0) float mBottomLeftRadius, @FloatRange(from = 0) float mBottomRightRadius) {
        if (mRadius == 0) {
            float[] radii = {mTopLeftRadius, mTopLeftRadius,
                    mTopRightRadius, mTopRightRadius,
                    mBottomRightRadius, mBottomRightRadius,
                    mBottomLeftRadius, mBottomLeftRadius
            };
            mNormalBackground.setCornerRadii(radii);
            mFocusBackground.setCornerRadii(radii);
            mPressedBackground.setCornerRadii(radii);
            mUnableBackground.setCornerRadii(radii);
        }
    }

    public void setRound(boolean round){
        this.mRound = round;
        int height = getMeasuredHeight();
        if(mRound){
            setRadius(height / 2f);
        }
    }

    public void setRadius(float[] radii){
        mNormalBackground.setCornerRadii(radii);
        mFocusBackground.setCornerRadii(radii);
        mPressedBackground.setCornerRadii(radii);
        mUnableBackground.setCornerRadii(radii);
    }

    /********************  background color  **********************/

    public void setStateBackgroundColor(@ColorInt int normal, @ColorInt int focus, @ColorInt int pressed, @ColorInt int unable){
        mNormalBackgroundColor = normal;
        mFocusBackgroundColor = focus;
        mPressedBackgroundColor = pressed;
        mUnableBackgroundColor = unable;
        mNormalBackground.setColor(mNormalBackgroundColor);
        mFocusBackground.setColor(mFocusBackgroundColor);
        mPressedBackground.setColor(mPressedBackgroundColor);
        mUnableBackground.setColor(mUnableBackgroundColor);
    }

    public void setNormalBackgroundColor(@ColorInt int normalBackgroundColor) {
        this.mNormalBackgroundColor = normalBackgroundColor;
        mNormalBackground.setColor(mNormalBackgroundColor);
    }

    public void setFocusBackgroundColor(@ColorInt int focusBackgroundColor) {
        this.mFocusBackgroundColor = focusBackgroundColor;
        mFocusBackground.setColor(mFocusBackgroundColor);
    }

    public void setPressedBackgroundColor(@ColorInt int pressedBackgroundColor) {
        this.mPressedBackgroundColor = pressedBackgroundColor;
        mPressedBackground.setColor(mPressedBackgroundColor);
    }

    public void setUnableBackgroundColor(@ColorInt int unableBackgroundColor) {
        this.mUnableBackgroundColor = unableBackgroundColor;
        mUnableBackground.setColor(mUnableBackgroundColor);
    }

    /*******************alpha animation duration********************/
    public void setAnimationDuration(@IntRange(from = 0)int duration){
        this.mDuration = duration;
        mStateBackground.setEnterFadeDuration(mDuration);
    }

    /***************  text color   ***********************/

    private void setTextColor() {
        int[] colors = new int[] {mPressedTextColor, mFocusTextColor, mNormalTextColor, mUnableTextColor};
        mTextColorStateList = new ColorStateList(states, colors);
        setTextColor(mTextColorStateList);
    }

    public void setStateTextColor(@ColorInt int normal, @ColorInt int focus, @ColorInt int pressed, @ColorInt int unable){
        this.mNormalTextColor = normal;
        this.mFocusTextColor = focus;
        this.mPressedTextColor = pressed;
        this.mUnableTextColor = unable;
        setTextColor();
    }

    public void setNormalTextColor(@ColorInt int normalTextColor) {
        this.mNormalTextColor = normalTextColor;
        setTextColor();
    }

    public void setFocusTextColor(@ColorInt int focusTextColor) {
        this.mFocusTextColor = focusTextColor;
        setTextColor();
    }

    public void setPressedTextColor(@ColorInt int pressedTextColor) {
        this.mPressedTextColor = pressedTextColor;
        setTextColor();
    }

    public void setUnableTextColor(@ColorInt int unableTextColor) {
        this.mUnableTextColor = unableTextColor;
        setTextColor();
    }
}

