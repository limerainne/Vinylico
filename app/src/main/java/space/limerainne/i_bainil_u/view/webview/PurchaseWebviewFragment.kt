package space.limerainne.i_bainil_u.view.webview

import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebView
import org.jetbrains.anko.toast
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.base.BainilLauncher
import space.limerainne.i_bainil_u.base.UserInfo
import space.limerainne.i_bainil_u.view.MainActivity

/**
 * Created by CottonCandy on 2016-10-03.
 */
class PurchaseWebviewFragment(): WebviewFragment() {

    final val URL_PAYMENT_PREFIX = "https://www.bainil.com/api/v2/kakaopay/request"
    final val URL_CLOSE = "www.bainil.com/payment/close"

    final val URL_ANDROID_INAPP_PAY = "www.bainil.com/payment/inapp"

    private var userId: Long = 0
    private var albumId: Long = 0

    fun setURL(userId: Long, albumId: Long) {
        if (userId < 1 || albumId < 1)
            return

        this.userId = userId
        this.albumId = albumId

        init_url = "${URL_PAYMENT_PREFIX}?albumId=${albumId}&userId=${userId}"
    }

    override fun onInitWebview()    {
        super.onInitWebview()

        mWebView.setWebViewClient(MyPurchaseWebViewClient(context))
    }

    inner class MyPurchaseWebViewClient(context: Context): MyWebViewClient(context)    {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            if (url != null)    {
                // for payment failed -> "Close" button
                if (url.endsWith(URL_CLOSE))   {
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
                    context.toast("Google Play In-App purchase has to be placed in the official Bainil app!")
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

        fun newInstance(userId: Long, albumId: Long, context: Context): PurchaseWebviewFragment {
            val fragment = PurchaseWebviewFragment()

            fragment.init_url = "about:blank"
            fragment.toolbar_title = "Incorrect purchase target!"

            if (userId > 0 && albumId > 0 && checkLogin(userId, context))  {
                fragment.setURL(userId, albumId)
                fragment.toolbar_title = "Buy Album #${albumId}"
                fragment.toolbar_subtitle = "to #${userId} (You)"
            }

            return fragment
        }

        fun checkLogin(userId: Long, context: Context): Boolean  {
            val userInfo = UserInfo(context)
            return userInfo.userId == userId
        }
    }
}