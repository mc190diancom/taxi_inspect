package com.feidi.elecsign.mvp.ui.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：wanglei on 2019/5/17.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 适用于纵向RecyclerView列表的多功能分隔线
 */
public class MultiVeriticalItemDecoration extends RecyclerView.ItemDecoration {

    public MultiVeriticalItemDecoration() {
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }


}
