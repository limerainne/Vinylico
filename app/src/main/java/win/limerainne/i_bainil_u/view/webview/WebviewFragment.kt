package win.limerainne.i_bainil_u.view.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.credential.LoginCookie
import win.limerainne.i_bainil_u.toolbox.WebviewTool
import win.limerainne.i_bainil_u.view.DataLoadable
import win.limerainne.i_bainil_u.view.HavingToolbar
import win.limerainne.i_bainil_u.view.MyFragment
import java.net.URISyntaxException

/**
 * Created by Limerainne on 2016-09-22.
 */
open class WebviewFragment: MyFragment(), HavingToolbar, DataLoadable {

    override val TargetLayout = R.layout.fragment_webview

    protected var backEnabled = true

    var init_url = ThisApp.AppContext.getString(R.string.url_bainil_about)
    var toolbar_title = ThisApp.AppContext.getString(R.string.msg_bainil_title)
    var toolbar_subtitle = ""

    private val cookie_url = ThisApp.AppContext.getString(R.string.url_cookie)

    lateinit var this_activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.this_activity = activity
    }

    @BindView(R.id.web_view)
    lateinit var mWebView: WebView
    @BindView(R.id.web_url)
    lateinit var mWebURL: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) as View
        ButterKnife.bind(this, view)

        onInitToolbar()
        onInitWebview()

        return view
    }

    override fun initToolbar()  {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        onInitToolbar()
    }

    open protected fun onInitToolbar() {
        toolbar.title = toolbar_title
        if (toolbar_subtitle.length > 0)
            toolbar.subtitle = toolbar_subtitle
    }

    open protected fun onInitWebview()    {
        mWebView.loadUrl(init_url)
        mWebURL.text = init_url

        val webSettings = mWebView.getSettings()

        // Enable Javascript
        webSettings.setJavaScriptEnabled(true)

        // userAgent
        webSettings.userAgentString = WebviewTool().getDefaultUserAgentString(context)
        print(webSettings.userAgentString)

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(MyWebViewClient(context))

        // Allow HTTPS to HTTP request (to load album image; might be a security flaw)
        // http://eclipse4j.tistory.com/220
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mWebView.getSettings().mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // TODO get auto-login cookie if exists!
        // "auth-token", "remember", "email" (?)
        val loginCookie = LoginCookie(context)
        fun setCookieTo(tag: String, value: String) {
            CookieManager.getInstance().setCookie(cookie_url, "${tag}=${value}")
        }
        if (loginCookie.haveLoginCookie) {
            setCookieTo("JSESSIONID", loginCookie.JSESSIONID)
            if (loginCookie.isAutoLogin) {

                setCookieTo("auth_token", loginCookie.auth_token)
                setCookieTo("email", loginCookie.email)
                setCookieTo("remember", loginCookie.remember)
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    open inner class MyWebViewClient(context: Context): WebViewClient()   {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

//            Log.v("Webview", "OnPageFinished: ${url}")
            mWebURL.text = url

//            if (scroll_view != null)
//                scroll_view.scrollTo(0, 0)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            mWebURL.text = url + " (${getString(R.string.msg_web_loading)})"
        }

        // http://apogenes.tistory.com/4
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null && url.startsWith("intent://")) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val existPackage = context.getPackageManager().getLaunchIntentForPackage(intent.getPackage())
                    if (existPackage != null) {
                        startActivity(intent)
                    } else {
                        val marketIntent = Intent(Intent.ACTION_VIEW)
                        marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()))
                        marketIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(marketIntent)
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else if (url != null && url.startsWith("market://")) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    if (intent != null) {
                        startActivity(intent)
                    }
                    return true
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }

            }
            view?.loadUrl(url)
            mWebURL.text = url
            return true
        }

        @SuppressLint("NewApi")
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            return this.shouldOverrideUrlLoading(view, url)
        }
    }

    fun canGoBack(): Boolean {
        if (!backEnabled)   return false

        return mWebView.canGoBack()
    }

    fun goBack()    {
//        scroll_view.scrollTo(0, 0)
        mWebView.goBack()
//        scroll_view.scrollTo(0, 0)
    }

    companion object {
        val TAG = WebviewFragment::class.java.simpleName

        fun newInstance(url: String, toolbar_title: String): WebviewFragment {
            val fragment = WebviewFragment()

            if (url.length > 0)
                fragment.init_url = url
            if (toolbar_title.length > 0)
                fragment.toolbar_title = toolbar_title

            return fragment
        }
    }
}