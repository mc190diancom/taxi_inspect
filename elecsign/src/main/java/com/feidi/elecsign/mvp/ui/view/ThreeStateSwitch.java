package com.feidi.elecsign.mvp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Switch;

import com.feidi.elecsign.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 作者：wanglei on 2019/5/16.
 * 邮箱：forwlwork@gmail.com
 */
public class ThreeStateSwitch extends Switch {
    /** 打开状态 */
    public static final int STATE_ON = 0;
    /** 临界状态 */
    public static final int STATE_CRITICAL = 1;
    /** 关闭状态 */
    public static final int STATE_OFF = 2;

    private static final int TOUCH_MODE_IDLE = 0;
    private static final int TOUCH_MODE_DOWN = 1;

    private Drawable mCriticalTrackDrawable;
    private Drawable mTrackDrawable;

    private int mTouchMode;
    private OnInterceptClickListener mListener;

    @State
    int mState;

    public ThreeStateSwitch(Context context) {
        this(context, null);
    }

    public ThreeStateSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ThreeStateSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.ThreeStateSwitch);
        mCriticalTrackDrawable = t.getDrawable(R.styleable.ThreeStateSwitch_critical_track);
        mState = t.getInteger(R.styleable.ThreeStateSwitch_state, STATE_OFF);
        t.recycle();

        mTrackDrawable = getTrackDrawable();

        //初始化开关的状态
        if (mState == STATE_OFF) {
            setChecked(false);
        } else if (mState == STATE_ON) {
            setChecked(true);
        } else {
            if (mCriticalTrackDrawable != null) {
                setTrackDrawable(mCriticalTrackDrawable);
            }
            setChecked(true);
        }
    }

    /**
     * 设置开关的状态
     * <p>
     * 关闭状态 ---->> 打开状态 (√)
     * 临界状态 ---->> 打开状态（√）
     * 打开状态 ---->> 关闭状态（√）
     * 临界状态 ---->> 关闭状态（×）
     * 关闭状态 ---->> 临界状态（×）
     * 打开状态 ---->> 临界状态（√）
     *
     * @param newState 新的状态
     */
    public void setState(@State int newState) {
        if (mState != newState) {
            switch (newState) {
                case STATE_ON:
                    if (mState == STATE_OFF) {
                        //关闭状态 ---->> 打开状态
                        if (mTrackDrawable != null) {
                            setTrackDrawable(mTrackDrawable);
                        }
                        setChecked(true);
                    } else {
                        //临界状态 ---->> 打开状态
                        if (mCriticalTrackDrawable != null) {
                            setTrackDrawable(mCriticalTrackDrawable);
                        }
                    }
                    break;
                case STATE_OFF:
                    if (mState == STATE_ON) {
                        //打开状态 ---->> 关闭状态
                        setTrackDrawable(mTrackDrawable);
                        setChecked(false);
                    } else {
                        //临界状态 ---->> 关闭状态
                        throw new StateSwitchException("ThreeStateSwitch can not change state STATE_CRITICAL to STATE_OFF.");
                    }
                    break;
                case STATE_CRITICAL:
                    if (mState == STATE_OFF) {
                        //关闭状态 ---->> 临界状态
                        if (mCriticalTrackDrawable != null) {
                            setThumbDrawable(mCriticalTrackDrawable);
                        }
                        setChecked(true);
                    } else {
                        //打开状态 ---->> 临界状态
                        if (mCriticalTrackDrawable != null) {
                            setTrackDrawable(mCriticalTrackDrawable);
                        }
                    }
                    break;
                default:
                    break;
            }

            mState = newState;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isEnabled() && isClickable()) {
                    mTouchMode = TOUCH_MODE_DOWN;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchMode == TOUCH_MODE_DOWN) {
                    //只有当开关的状态处于临界状态且设置有OnInterceptClickListener的情况下才会拦截该事件
                    if (mState == STATE_CRITICAL && mListener != null) {
                        mListener.intercept();
                        return true;
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                mTouchMode = TOUCH_MODE_IDLE;
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @IntDef({STATE_ON, STATE_CRITICAL, STATE_OFF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    private static class StateSwitchException extends RuntimeException {

        StateSwitchException(String message) {
            super(message);
        }

    }

    public void setOnInterceptClickListener(OnInterceptClickListener listener) {
        this.mListener = listener;
    }

    public interface OnInterceptClickListener {
        /**
         * 当开关处于临界状态时，如果设置了该回调，在点击开关后将会回调该方法，并且拦截后续的事件
         * 主要目的是实现在临界状态点击开关后需要弹窗的一个功能
         */
        void intercept();
    }

}
