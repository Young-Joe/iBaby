package com.joe.ibaby.helper

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author qiaojianfeng on 18/3/22.
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration {

    var topSpace = 0

    constructor(topSpace: Int) : super() {
        this.topSpace = topSpace
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent?.getChildAdapterPosition(view) != 0) {
            outRect?.top = topSpace
        }
    }

}