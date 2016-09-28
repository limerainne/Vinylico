package space.limerainne.i_bainil_u.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.*
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

    override fun onInitWebview()    {
        super.onInitWebview()

        mWebView.addJavascriptInterface(object : Any() {

            @JavascriptInterface
            fun getHtml(html: String)   {
                val u = UserInfo(this_activity)

                u.parseInfo(html)

                println(u)
            }
        }, javascriptInterfaceName)

        mWebView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.v(TAG, "onPageFinished: " + url)

                val cookies = CookieManager.getInstance().getCookie(url)
                Log.d(TAG, "onPageFinished: " + cookies)

                if (url.equals(url_fan_profile))    {
                    // get cookie & other informations
                    // in HTML: user code, user URL, ...
                    // *http://jabstorage.tistory.com/5
                    // http://stackoverflow.com/questions/2376471/how-do-i-get-the-web-page-contents-from-a-webview
                    if (view != null) {
                        view.loadUrl("javascript:" + javascriptInterfaceName + ".getHtml(document.getElementsByTagName('html')[0].innerHTML);")
                    }

                    // in LoginCookie: login token, auto-login enabled?
                    parseLoginCookie(CookieManager.getInstance().getCookie(url))

                    // return to previous screen
                    val activity = this_activity    // TODO why we have to save initial activity reference?
                    if (activity is MainActivity) {
                        activity.popBackStack()
                        activity.updateNavigationViewUserInfoArea()
                    }
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.v(TAG, "onPageStarted: " + url)

                val cookies = CookieManager.getInstance().getCookie(url)
                Log.d(TAG, "onPageStarted: " + cookies)

                when  {
                    url.equals(url_top) ->  {
                        view?.loadUrl(url_fan_profile)
                    }
                    url?.endsWith(url_signin_wo_redir) ?: false -> {
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