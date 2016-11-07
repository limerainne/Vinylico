package win.limerainne.i_bainil_u.view.webview

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import org.jetbrains.anko.toast
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.credential.UserInfo
import win.limerainne.i_bainil_u.toolbox.BainilLauncher
import win.limerainne.i_bainil_u.view.MainActivity

/**
 * Created by CottonCandy on 2016-10-03.
 */
class PurchaseWebviewFragment(): WebviewFragment() {

    final val URL_PAYMENT_PREFIX = "https://www.bainil.com/api/v2/kakaopay/request"
    final val URL_PAYMENT_RESULT = "https://www.bainil.com/api/v2/kakaopay/result"
    final val URL_CLOSE = "www.bainil.com/payment/close"
    final val URL_DOWNLOAD = "www.bainil.com/payment/download"

    final val URL_ANDROID_INAPP_PAY = "www.bainil.com/payment/inapp"

    private var userId: Long = 0
    private var albumId: Long = 0
    private var seqId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        // prohibit go backward page
        backEnabled = false
    }

    fun setURL(userId: Long, albumId: Long, seqId: Long) {
        if (userId < 1 || albumId < 1)
            return

        this.userId = userId
        this.albumId = albumId

        init_url = "${URL_PAYMENT_PREFIX}?albumId=${albumId}&userId=${userId}"
        if (seqId > 0)
            init_url = "${init_url}&seqId=${seqId}"
    }

    override fun onInitWebview()    {
        super.onInitWebview()

        mWebView.setWebViewClient(MyPurchaseWebViewClient(context))
    }

    inner class MyPurchaseWebViewClient(context: Context): MyWebViewClient(context)    {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
//            Log.v(PurchaseWebviewFragment.TAG, "onPageStarted: " + url)

            if (url != null)    {
                // for payment failed -> "Close" button
                if (url.endsWith(URL_CLOSE) || url.endsWith(URL_DOWNLOAD))   {
                    view?.stopLoading()

                    // return to previous screen
                    // TODO incorrect implementation
                    val activity = this_activity
                    if (activity is MainActivity) {
                        activity.popBackStack()
                    }
                }
                if (url.endsWith(URL_ANDROID_INAPP_PAY))    {
                    view?.stopLoading()

                    // notify message
                    context.toast(context.getString(R.string.msg_err_purchase_inapp_purchase_impossible))
                    // call Bainil app
                    val activity = this_activity
                    if (activity is MainActivity) {
                        activity.popBackStack()
                        BainilLauncher.executeBainilAppAlbumScreen(context, albumId)
                    }
                }
            }
        }
    }

    companion object {
        val TAG = PurchaseWebviewFragment::class.java.simpleName

        fun newInstance(userId: Long, albumId: Long, seqId: Long, context: Context): PurchaseWebviewFragment {
            val fragment = PurchaseWebviewFragment()

            fragment.init_url = "about:blank"
            fragment.toolbar_title = context.getString(R.string.msg_err_purhcase_incorrect_URL)

            if (userId > 0 && albumId > 0 && checkLogin(userId, context))  {
                fragment.setURL(userId, albumId, seqId)
                fragment.toolbar_title = context.getString(R.string.msg_notice_purchase_title, albumId)
                fragment.toolbar_subtitle = context.getString(R.string.msg_notice_purchase_subtitle, userId)
            }

            return fragment
        }

        fun checkLogin(userId: Long, context: Context): Boolean  {
            val userInfo = UserInfo(context)
            return userInfo.userId == userId
        }
    }
}