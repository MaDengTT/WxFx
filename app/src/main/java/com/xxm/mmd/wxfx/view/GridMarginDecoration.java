package com.xxm.mmd.wxfx.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MaDeng on 2018/3/27.
 */

public class GridMarginDecoration extends RecyclerView.ItemDecoration {

    private int vMargin;   //纵向
    private int hMargin;   //横向

    public GridMarginDecoration(int vMargin, int hMargin) {
        this.vMargin = vMargin;
        this.hMargin = hMargin;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        //判断总的数量是否可以整除
        int totalCount = layoutManager.getItemCount();                  //总数
        int surplusCount = totalCount % layoutManager.getSpanCount();   //列数
        int childPosition = parent.getChildAdapterPosition(view);       //当前
        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {//竖直方向的
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //后面几项需要bottom
                outRect.bottom = vMargin;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.bottom = vMargin;
            }
            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要右边
                outRect.right = hMargin;
                outRect.left = hMargin/2;
            }else {
                outRect.left = hMargin;
                outRect.right = hMargin/2;
            }
            outRect.top = vMargin;

        } else {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //后面几项需要右边
                outRect.right = hMargin;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.right = hMargin;
            }
            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要下边
                outRect.bottom = vMargin;
            }
            outRect.top = vMargin;
            outRect.left = hMargin;
        }

    }
}
