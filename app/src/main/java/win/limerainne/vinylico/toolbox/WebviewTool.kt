package win.limerainne.vinylico.toolbox

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import win.limerainne.vinylico.ThisApp

/**
 * Created by Limerainne on 2016-10-18.
 */
class WebviewTool {
    // You may uncomment next line if using Android Annotations library, otherwise just be sure to run it in on the UI thread
    // @UiThread
    fun getDefaultUserAgentString(context: Context): String {
        val userAgent: String
        if (Build.VERSION.SDK_INT >= 17) {
            userAgent = NewApiWrapper.getDefaultUserAgent(context)
        }   else {

            try {
                val constructor = WebSettings::class.java.getDeclaredConstructor(Context::class.java, WebView::class.java)
                constructor.setAccessible(true)
                try {
                    val settings = constructor.newInstance(context, null)
                    userAgent = settings.getUserAgentString()
                } finally {
                    constructor.setAccessible(false)
                }
            } catch (e: Exception) {
                userAgent = WebView(context).getSettings().getUserAgentString()
            }
        }

        userAgent += " ${ThisApp.AppName}.Android"

//        Log.v("WebviewTool", "User agent: ${userAgent}")

        return userAgent
    }

    @TargetApi(17)
    internal object NewApiWrapper {
        fun getDefaultUserAgent(context: Context): String {
            return WebSettings.getDefaultUserAgent(context)
        }
    }
}