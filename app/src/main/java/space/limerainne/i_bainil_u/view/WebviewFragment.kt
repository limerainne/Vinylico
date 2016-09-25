package space.limerainne.i_bainil_u.view

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_webview.view.*
import space.limerainne.i_bainil_u.R

/**
 * Created by Limerainne on 2016-09-22.
 */
open class WebviewFragment: Fragment() {

    var init_url = "http://www.bainil.com/bainil"
    var toolbar_title = "Bainil"

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
    }

    open protected fun onInitWebview()    {
        mWebView.loadUrl(init_url)

        // Enable Javascript
        val webSettings = mWebView.getSettings()
        webSettings.setJavaScriptEnabled(true)

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(WebViewClient())
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