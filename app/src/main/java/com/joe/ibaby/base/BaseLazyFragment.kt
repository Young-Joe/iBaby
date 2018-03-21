package com.joe.ibaby.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by QiaoJF on 17/4/22.
 */

abstract class BaseLazyFragment : Fragment() {

    /**
     * 是否可见状态
     */
    private var fragIsVisible: Boolean = false
    /**
     * 标志位，View已经初始化完成。
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private var isPrepared: Boolean = false
    /**
     * 是否第一次加载
     */
    var isFirstLoad = true
    /**
     * fragment根布局
     */
    var fragmentLayout: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
        // 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
        isFirstLoad = true
        fragmentLayout = onCreateContentView(inflater, container, savedInstanceState)
        isPrepared = true
        lazyLoad()
        return fragmentLayout
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            fragIsVisible = true
            onVisible()
        } else {
            fragIsVisible = false
            onInvisible()
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     * visible.
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            fragIsVisible = true
            onVisible()
        } else {
            fragIsVisible = false
            onInvisible()
        }
    }

    protected fun onVisible() {
        lazyLoad()
    }

    protected fun onInvisible() {}

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected fun lazyLoad() {

        if (isPrepared && fragIsVisible && isFirstLoad) {
            isFirstLoad = false
            createdChange2Visible()
            initFragment(fragmentLayout!!)
        }

    }

    //预加载创建view
    protected abstract fun onCreateContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View

    //懒加载 数据.在已create情况下只会init一次
    protected abstract fun initFragment(view: View)

    /**
     * 已created后,visible的change
     * 由于setUserVisibleHint的方法先于onCreate执行,涉及到view的操作需在此方法中执行
     */
    protected abstract fun createdChange2Visible()

}
