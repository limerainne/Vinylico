package win.limerainne.vinylico.view.webview

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.toolbox.BainilLauncher
import win.limerainne.vinylico.view.MainActivity

/**
 * Created by CottonCandy on 2016-10-03.
 */
class PurchaseWebviewFragment(): WebviewFragment() {

    // NOTE old URLs (supports only KakaoPay and Android In-app purchase)
    // final val URL_PAYMENT_PREFIX = "https://www.bainil.com/api/v2/kakaopay/request"
    // final val URL_PAYMENT_RESULT = "https://www.bainil.com/api/v2/kakaopay/result"

    // new URLs (visited @ 17-09; supports credit cards, micropayment)
    val URL_PAYMENT_PREFIX = "https://www.bainil.com/payment/imp/request"
    val URL_PAYMENT_RESULT = "https://www.bainil.com/payment/complete?app=A"
    val URL_CLOSE = "www.bainil.com/payment/close"
    val URL_DOWNLOAD = "www.bainil.com/payment/download"

    val URL_ANDROID_INAPP_PAY = "www.bainil.com/payment/inapp"

    private var userId: Long = 0
    private var albumId: Long = 0
    private var seqId: Long = 0
    private var trackId: Long = 0

    private var afterBought: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        // prohibit go backward page
        backEnabled = false
    }

    fun setURL(userId: Long, albumId: Long, seqId: Long, trackId: Long = 0) {
        if (userId < 1 || albumId < 1)
            return

        this.userId = userId
        this.albumId = albumId
        this.seqId = seqId
        this.trackId = trackId

        init_url = "${URL_PAYMENT_PREFIX}?albumId=${albumId}&userId=${userId}"
        if (seqId > 0)
            init_url = "${init_url}&seqId=${seqId}"

        // OPTIONAL for purchasing single track
        if (trackId > 0)
            init_url = "${init_url}&trackId=${trackId}"
    }

    override fun onInitWebview()    {
        super.onInitWebview()

        mWebView.setWebViewClient(MyPurchaseWebViewClient(context))
    }

    inner class MyPurchaseWebViewClient(context: Context): MyWebViewClient(context)    {
        var finished = false

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
//            Log.v(PurchaseWebviewFragment.TAG, "onPageStarted: " + url)

            if (url != null)    {
                // if payment succeed/failed, then user clicks "Close" button
                if (url.endsWith(URL_CLOSE) || url.endsWith(URL_DOWNLOAD))   {
                    if (finished)   return

                    view?.stopLoading()

                    // return to previous screen
                    // NOTE BAD implementation, but leave as is...
                    val activity = this_activity
                    if (activity is MainActivity) {
                        doAsync {
                            Thread.sleep(500)
                            uiThread {
                                activity?.popBackStack()
                            }
                            if (url.endsWith(URL_DOWNLOAD))
                                activity?.runOnUiThread {
                                    afterBought()
                                }
                        }
                    }

                    finished = true
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

        fun newInstance(userId: Long, albumId: Long, seqId: Long, context: Context, afterBought: () -> Unit): PurchaseWebviewFragment {
            return newInstance(userId, albumId, seqId, 0, context, afterBought)
        }

        fun newInstance(userId: Long, albumId: Long, seqId: Long, trackId: Long = 0, context: Context, afterBought: () -> Unit): PurchaseWebviewFragment {
            val fragment = PurchaseWebviewFragment()

            fragment.init_url = "about:blank"
            fragment.toolbar_title = context.getString(R.string.msg_err_purhcase_incorrect_URL)

            fragment.afterBought = afterBought

            if (userId > 0 && albumId > 0 && checkLogin(userId, context))  {
                fragment.setURL(userId, albumId, seqId, trackId)
                if (trackId <= 0) { // purchase album
                    fragment.toolbar_title = context.getString(R.string.msg_notice_purchase_title, albumId.toString())
                    fragment.toolbar_subtitle = context.getString(R.string.msg_notice_purchase_subtitle, userId.toString())
                }   else    {   // purchase track
                    fragment.toolbar_title = context.getString(R.string.msg_notice_purchase_track_title, trackId.toString())
                    fragment.toolbar_subtitle = context.getString(R.string.msg_notice_purchase_subtitle, userId.toString())
                }
            }

            return fragment
        }

        fun checkLogin(userId: Long, context: Context): Boolean  {
            val userInfo = UserInfo(context)
            return userInfo.userId == userId
        }
    }
}