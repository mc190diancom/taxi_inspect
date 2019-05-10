package com.miu360.legworkwrit.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.jess.arms.mvp.IPresenter;
import com.miu30.common.base.BaseMvpActivity;

/**
 * 作者：wanglei on 2018/9/21.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 文书预览所对应的Activity基类
 * 主要实现缩放以及拖动功能
 */
public abstract class BasePreviewActivity<P extends IPresenter> extends BaseMvpActivity<P> {
    /**
     * 手势缩放
     */
    private ScaleGestureDetector scaleGestureDetector;
    /**
     * 手势拖动
     */
    private GestureDetector dragGestureDetecot;

    private float mScaleFactor = 1.0f;
    private float mPreScaleFactor = 1.0f;

    private float lastX;
    private float lastY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scaleGestureDetector = new ScaleGestureDetector(self, new ScaleListener());
        dragGestureDetecot = new GestureDetector(self, new DragListener());
    }

    /**
     * 需要对其操作的View
     *
     * @return View
     */
    protected abstract View getPreView();

    private boolean flag = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e("aaa", "action_move");
                if (event.getPointerCount() == 1) {
                    if (flag) {
                        dragGestureDetecot.onTouchEvent(event);
                    } else {
                        flag = true;
                    }
                } else {
                    scaleGestureDetector.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                flag = false;
                Log.e("aaa", "action_pointer_up");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //mScaleFactor = 1.0f;
                //Log.e("aaa", "action_pointer_down");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("aaa", "action_up");
                lastX = 0f;
                lastY = 0f;
                break;
            default:
                break;
        }

        return false;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.e("aaa", "onScaleBegin");
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.e("aaa", "onScale");

            if (detector.getPreviousSpan() > 0) {
                mScaleFactor = mScaleFactor * (detector.getCurrentSpan() / detector.getPreviousSpan());
            } else {
                mScaleFactor = mPreScaleFactor;
            }

            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 2.0f));

            getPreView().setScaleX(mScaleFactor);
            getPreView().setScaleY(mScaleFactor);

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            Log.e("aaa", "onScaleEnd");
            mPreScaleFactor = detector.getScaleFactor();
        }
    }

    private class DragListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            getPreView().offsetLeftAndRight((int) (e2.getRawX() - lastX));
            getPreView().offsetTopAndBottom((int) (e2.getRawY() - lastY));

            lastX = e2.getRawX();
            lastY = e2.getRawY();
            return true;
        }
    }

}
