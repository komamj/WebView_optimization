package com.koma.webcontainer.optimize

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.MutableContextWrapper
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.webkit.WebSettings
import android.webkit.WebView
import com.koma.webcontainer.BuildConfig
import com.koma.webcontainer.util.UTF_8
import timber.log.Timber
import java.util.Stack

object WebViewPool {
    private const val DEFAULT_POOL_SIZE = 3

    private lateinit var application: Application

    private val cachedWebView = Stack<WebView>()

    @get:JvmName("poolSize")
    val poolSize: Int = DEFAULT_POOL_SIZE

    fun init(application: Application) {
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

    private fun preload() {
        Looper.myQueue().addIdleHandler {
            if (cachedWebView.size < poolSize) {
                Timber.d("preload")
                cachedWebView.push(createWebView(application))
            }
            false
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebView(context: Context): WebView {
        val webView = WebView(MutableContextWrapper(context))
        with(webView) {
            with(settings) {
                allowFileAccessFromFileURLs = true
                allowContentAccess = true
                allowUniversalAccessFromFileURLs = true
                allowFileAccess = true
                loadsImagesAutomatically = false
                defaultTextEncodingName = UTF_8
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(false)
                displayZoomControls = false
                cacheMode = WebSettings.LOAD_DEFAULT
                domStorageEnabled = true
                databaseEnabled = true
                setAppCacheEnabled(true)
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                if (BuildConfig.DEBUG) {
                    WebView.setWebContentsDebuggingEnabled(true)
                }
            }
        }

        return webView
    }

    fun getWebView(context: Context): WebView {
        val webView = if (cachedWebView.isEmpty()) {
            createWebView(context)
        } else {
            cachedWebView.pop()
        }
        return webView.apply {
            val contextWrapper = this.context as MutableContextWrapper
            contextWrapper.baseContext = context
        }
    }
}
