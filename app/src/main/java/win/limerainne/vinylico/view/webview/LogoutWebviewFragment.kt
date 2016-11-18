package win.limerainne.vinylico.view.webview

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.credential.LoginCookie
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.view.MainActivity

/**
 * Created by Limerainne on 2016-09-23.
 */
class LogoutWebviewFragment: WebviewFragment() {

    val url_login = "https://www.bainil.com/signin?returnUrl=%2Ffan%2Fprofile"
    val url_logout = "https://www.bainil.com/signout"

    val url_fan_profile = "http://www.bainil.com/fan/profile"
    val url_top = "http://www.bainil.com/browse"
    val url_bainil = "http://www.bainil.com/"

    val url_signout = "https://www.bainil.com/signout"

    val url_signin_wo_redir = "bainil.com/signin"

    private val cookie_url = "www.bainil.com"

    var touched = false

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        init_url = url_logout
//        init_url = getString(R.string.URL_Login)
//        init_url = url_signout
        toolbar_title = ThisApp.AppContext.getString(R.string.nav_logout)

        // prohibit go backward page
        backEnabled = false
    }

    val javascriptInterfaceName = "HTMLRetriever"
    val myLock = Any()
    var myLockVar: Boolean = false

    override fun onInitWebview()    {
        super.onInitWebview()

        mWebView.setWebViewClient(MyLoginWebViewClient(context))

    }

    fun parseLoginCookie(cookie: String) {
//        Log.v(TAG, "parseLoginCookie: " + cookie)
        // TODO differentiate auto login/one-time login

        val cookieParser = LoginCookie(this_activity)

        cookieParser.parseLoginCookie(cookie)

//        Log.v(TAG, "parseLoginCookie: " + cookieParser)
    }

    inner class MyLoginWebViewClient(context: Context): MyWebViewClient(context) {
        var cookieFlushed = false

        var finished = false

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
//            Log.v(TAG, "onPageFinished: " + url)

            val cookies = CookieManager.getInstance().getCookie(cookie_url)
//            Log.d(TAG, "onPageFinished: " + cookies)

            if (cookieFlushed)  return

            // clear login info
            // TODO bainil.com/signout not work as expected
            // token not expired?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                CookieManager.getInstance().removeAllCookies {  }
            else
                CookieManager.getInstance().removeAllCookie()
            LoginCookie(context).clearCookie()
            UserInfo(context).clearInfo()

            cookieFlushed = true

            if (finished)   return

            // return to previous screen
            val activity = this_activity    // TODO why we have to save initial activity reference?
            if (activity is MainActivity) {
                doAsync {
                    Thread.sleep(500)
                    uiThread {
                        activity.popBackStack()
                        activity.updateNavigationViewUserInfoArea()
                    }
                }
            }

            finished = true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
//            Log.v(TAG, "onPageStarted: " + url)

            val cookies = CookieManager.getInstance().getCookie(cookie_url)
//            Log.d(TAG, "onPageStarted: " + cookies)

            mWebView.visibility = View.VISIBLE

            when  {
                url?.contains(url_top) ?: false ->  {
                    // do nothing
                }
                url?.endsWith(url_signin_wo_redir) ?: false -> {
                    // NOTE assume that returnUrl is retained after incorrect login trial
                    if (!touched) {
                        // ???
                        view?.stopLoading()

                        mWebView.visibility = View.INVISIBLE
                        view?.loadUrl(init_url)
                    }
                }
                url?.contains("facebook") ?: false -> {}    // TODO
                url?.equals(url_bainil) ?: false -> {}
                url?.equals(url_fan_profile) ?: false -> {}
                url?.equals(init_url) ?: false ->   {
                    touched = true
                }
                url?.equals(url_signout) ?: false -> {}
                else -> {
                    view?.stopLoading()

                    mWebView.visibility = View.INVISIBLE
                    view?.loadUrl(init_url)
                }
            }
        }
    }

    companion object {
        val TAG = LogoutWebviewFragment::class.java.simpleName

        fun newInstance(): LogoutWebviewFragment {
            val fragment = LogoutWebviewFragment()

            return fragment
        }
    }
}