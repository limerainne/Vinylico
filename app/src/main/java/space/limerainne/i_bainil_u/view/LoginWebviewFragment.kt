package space.limerainne.i_bainil_u.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.*
import space.limerainne.i_bainil_u.R
import java.net.CookieHandler
import java.net.CookiePolicy
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Limerainne on 2016-09-23.
 */
class LoginWebviewFragment: WebviewFragment() {

    val url_fan_profile = "http://www.bainil.com/fan/profile"
    val url_top = "http://www.bainil.com/browse"

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        init_url = getString(R.string.URL_Login)
        toolbar_title = "Login"
    }

    override fun onInitWebview()    {
        super.onInitWebview()

        mWebView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.v(TAG, "onPageFinished: " + url)

                val cookies = CookieManager.getInstance().getCookie(url)
                Log.d(TAG, "onPageFinished: " + cookies)

                if (url.equals(url_fan_profile))    {
                    this_activity.onBackPressed()
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.v(TAG, "onPageStarted: " + url)

                if (url.equals(url_top)) {
                    val cookies = CookieManager.getInstance().getCookie(url)
                    Log.d(TAG, "onPageStarted: " + cookies)

                    view?.loadUrl(url_fan_profile)
                }
            }

            @SuppressLint("NewApi")
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                Log.v(TAG, "shouldOverrideLoading: " + request?.url)
                view?.loadUrl(request?.url.toString());
                return true;
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // NOTE from API 24, this method is deprecated
                Log.v(TAG, "shouldOverrideLoading: " + url)
                view?.loadUrl(url);
                return true;
            }
        })
    }

    companion object {
        val TAG = LoginWebviewFragment::class.java.simpleName

        fun newInstance(): LoginWebviewFragment {
            val fragment = LoginWebviewFragment()

            return fragment
        }
    }
}