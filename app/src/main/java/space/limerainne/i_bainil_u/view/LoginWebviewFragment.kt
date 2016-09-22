package space.limerainne.i_bainil_u.view

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import space.limerainne.i_bainil_u.R

/**
 * Created by Limerainne on 2016-09-23.
 */
class LoginWebviewFragment: WebviewFragment() {

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