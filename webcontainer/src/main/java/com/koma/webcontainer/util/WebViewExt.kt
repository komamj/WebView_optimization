package com.koma.webcontainer.util

import android.webkit.WebView
import timber.log.Timber

private const val JAVASCRIPT = "javascript:"
private const val NATIVE_CALLBACK = "__NativeCallback"
private const val ASSETS_PATH = "file:///android_asset/"

fun WebView.injectJs(js: String) {
    post {
        val value = "$JAVASCRIPT$js"
        Timber.d(value)
        evaluateJavascript(value) {
            Timber.d("evaluateJavascript $it")
        }
    }
}

fun WebView.injectJsScript(js: String) {
    post {
        val script = """var newscript = document.createElement("script")
            |newscript.innerHTML = "var vConsole = new VConsole();console.log('Hello world');"
            |document.body.appendChild(newscript)
        """.trimMargin()

        val value = "$JAVASCRIPT$script"
        Timber.d(value)
        evaluateJavascript(value) {
            Timber.d("evaluateJavascript $it")
        }
    }
}

fun WebView.callJs(json: String) {
    post {
        val value = "$JAVASCRIPT${NATIVE_CALLBACK}('$json')"
        evaluateJavascript(value) {
            Timber.d("evaluateJavascript $it")
        }
    }
}
