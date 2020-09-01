package com.koma.webcontainer.optimize

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.webkit.WebView
import java.util.Stack

class WebViewPool constructor(application: Application) {
    init {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                preload()
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
                preload()
            }
        })
    }

    private lateinit var application: Application

    private val cachedWebView = Stack<WebView>()

    fun preload() {

    }

    companion object {
        private const val DEFAULT_POOL_SIZE = 3
    }
}
