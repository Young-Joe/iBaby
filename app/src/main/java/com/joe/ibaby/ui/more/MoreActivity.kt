package com.joe.ibaby.ui.more

import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseActivity
import kotlinx.android.synthetic.main.activity_more.*

class MoreActivity : BaseActivity() {
    override fun setContentLayout(): Int {
        return R.layout.activity_more
    }

    override fun initView() {
        webview!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if(newProgress == 100) {
                    progressBar.visibility = View.GONE
                }
            }
        }

    }

    override fun initData() {
        webview.loadUrl("https://github.com/Young-Joe/iBaby/blob/master/README.md")
    }

}
