package win.limerainne.i_bainil_u.view.webview

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.credential.LoginCookie
import win.limerainne.i_bainil_u.credential.UserInfo
import win.limerainne.i_bainil_u.view.MainActivity

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

        mWebView.addJavascriptInterface(object : Any() {

            @JavascriptInterface
            fun getHtml(html: String)   {
                val u = UserInfo(this_activity)

                u.parseInfo(html)

                println(u)

                synchronized(myLock) {
                    myLockVar = true
                    try {
                        (myLock as java.lang.Object).notifyAll()
                    } catch (e: IllegalMonitorStateException)   {
                        e.printStackTrace()
                    }
                }
            }
        }, javascriptInterfaceName)

        mWebView.setWebViewClient(MyLoginWebViewClient(context))

    }

    fun parseLoginCookie(cookie: String) {
        Log.v(TAG, "parseLoginCookie: " + cookie)
        // TODO differentiate auto login/one-time login

        val cookieParser = LoginCookie(this_activity)

        cookieParser.parseLoginCookie(cookie)

        Log.v(TAG, "parseLoginCookie: " + cookieParser)
    }

    inner class MyLoginWebViewClient(context: Context): MyWebViewClient(context) {
        var cookieFlushed = false

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.v(TAG, "onPageFinished: " + url)

            val cookies = CookieManager.getInstance().getCookie(cookie_url)
            Log.d(TAG, "onPageFinished: " + cookies)

            if (url.equals(url_fan_profile))    {
                mWebView.visibility = View.INVISIBLE

                // get cookie & other informations
                // in HTML: user code, user URL, ...
                // *http://jabstorage.tistory.com/5
                // http://stackoverflow.com/questions/2376471/how-do-i-get-the-web-page-contents-from-a-webview

                // in LoginCookie: login token, auto-login enabled?
                parseLoginCookie(CookieManager.getInstance().getCookie(cookie_url))

                if (view != null) {
                    myLockVar = false

                    view.loadUrl("javascript:" + javascriptInterfaceName + ".getHtml(document.getElementsByTagName('html')[0].innerHTML);")

                    synchronized(myLock)    {   // wait till javascript method called
                        if (!myLockVar)
                            (myLock as java.lang.Object).wait(5000)
                    }
                }

                // return to previous screen
                val activity = this_activity    // TODO why we have to save initial activity reference?
                if (activity is MainActivity) {
                    activity.popBackStack()
                    activity.updateNavigationViewUserInfoArea()
                }
            }   else    {
                mWebView.visibility = View.VISIBLE
            }

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

            // return to previous screen
            val activity = this_activity    // TODO why we have to save initial activity reference?
            if (activity is MainActivity) {
                activity.popBackStack()
                activity.updateNavigationViewUserInfoArea()
            }
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.v(TAG, "onPageStarted: " + url)

            val cookies = CookieManager.getInstance().getCookie(cookie_url)
            Log.d(TAG, "onPageStarted: " + cookies)

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