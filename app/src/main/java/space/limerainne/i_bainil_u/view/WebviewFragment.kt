package space.limerainne.i_bainil_u.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_webview.view.*
import space.limerainne.i_bainil_u.R
import java.net.URISyntaxException

/**
 * Created by Limerainne on 2016-09-22.
 */
open class WebviewFragment: Fragment() {

    var init_url = "http://www.bainil.com/bainil"
    var toolbar_title = "Bainil"
    var toolbar_subtitle = ""

    lateinit var this_activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.this_activity = activity
    }

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.web_view)
    lateinit var mWebView: WebView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_webview, container, false)
        ButterKnife.bind(this, view)

        onInitToolbar()
        onInitWebview()

        return view
    }

    open protected fun onInitToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = toolbar_title
        if (toolbar_subtitle.length > 0)
            toolbar.subtitle = toolbar_subtitle
    }

    open protected fun onInitWebview()    {
        mWebView.loadUrl(init_url)

        // Enable Javascript
        val webSettings = mWebView.getSettings()
        webSettings.setJavaScriptEnabled(true)

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(MyWebViewClient(context))
    }

    override fun onResume() {
        super.onResume()

    }

    open inner class MyWebViewClient(context: Context): WebViewClient()   {
        // http://apogenes.tistory.com/4
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null && url.startsWith("intent://")) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    val existPackage = context.getPackageManager().getLaunchIntentForPackage(intent.getPackage())
                    if (existPackage != null) {
                        startActivity(intent)
                    } else {
                        val marketIntent = Intent(Intent.ACTION_VIEW)
                        marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()))
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
            return false
        }

        @SuppressLint("NewApi")
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            return this.shouldOverrideUrlLoading(view, url)
        }
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