package com.koma.webcontainer.util

import java.net.URLConnection

object Utils {
    private const val DEFAULT_MIME_TYPE = "text/plain"

    fun guessMimeType(filePath: String): String {
        val mimeType = URLConnection.guessContentTypeFromName(filePath)
        return mimeType ?: DEFAULT_MIME_TYPE
    }
}
