package space.limerainne.i_bainil_u.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import kotlinx.android.synthetic.main.fragment_webview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.LoginCookie
import space.limerainne.i_bainil_u.base.UserInfo
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

    val url_signout = "https://www.bainil.com/signout"

    val url_signin_wo_redir = "bailil.com/signin"

    val loginInfo: MutableMap<String, String> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        init_url = getString(R.string.URL_Login)
//        init_url = url_signout
        toolbar_title = "Login"
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

        mWebView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.v(TAG, "onPageFinished: " + url)

                val cookies = CookieManager.getInstance().getCookie(url)
                Log.d(TAG, "onPageFinished: " + cookies)

                if (url.equals(url_fan_profile))    {
                    mWebView.visibility = View.INVISIBLE

                        // get cookie & other informations
                    // in HTML: user code, user URL, ...
                    // *http://jabstorage.tistory.com/5
                    // http://stackoverflow.com/questions/2376471/how-do-i-get-the-web-page-contents-from-a-webview

                    // in LoginCookie: login token, auto-login enabled?
                    parseLoginCookie(CookieManager.getInstance().getCookie(url))

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
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.v(TAG, "onPageStarted: " + url)

                val cookies = CookieManager.getInstance().getCookie(url)
                Log.d(TAG, "onPageStarted: " + cookies)

                mWebView.visibility = View.VISIBLE

                when  {
                    url?.contains(url_top) ?: false ->  {
                        mWebView.visibility = View.INVISIBLE
                        view?.loadUrl(url_fan_profile)
                    }
                    url?.endsWith(url_signin_wo_redir) ?: false -> {
                        mWebView.visibility = View.INVISIBLE
                        view?.loadUrl(init_url)
                    }
                    url?.contains("facebook") ?: false -> {}    // TODO
                    url?.equals(url_fan_profile) ?: false -> {}
                    url?.equals(init_url) ?: false ->   {}
                    url?.equals(url_signout) ?: false -> {}
                    else -> {
                        mWebView.visibility = View.INVISIBLE
                        view?.loadUrl(init_url)
                    }
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

    fun parseLoginCookie(cookie: String) {
        Log.v(TAG, "parseLoginCookie: " + cookie)
        // TODO differentiate auto login/one-time login

        val cookieParser = LoginCookie(this_activity)

        cookieParser.parseLoginCookie(cookie)

        Log.v(TAG, "parseLoginCookie: " + cookieParser)
    }

    companion object {
        val TAG = LoginWebviewFragment::class.java.simpleName

        fun newInstance(): LoginWebviewFragment {
            val fragment = LoginWebviewFragment()

            return fragment
        }
    }
}