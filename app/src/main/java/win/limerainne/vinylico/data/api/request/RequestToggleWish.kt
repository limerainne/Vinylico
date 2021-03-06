package win.limerainne.vinylico.data.api.request

import android.content.Context
import android.net.ConnectivityManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.data.api.request.data.RequestHTTPConnection

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestToggleWish(val albumId: Long,
                        val userId: Long,
                        val wish: Boolean) : RequestHTTPConnection() {

    override fun composeURL() = "${URL}?albumId=$albumId&userId=$userId&wish=" +
                                    if (wish) "true" else "false"

    override fun execute(): Boolean {
        return getHTTPResponseString().contains("true")
    }

    companion object    {
        private val URL = "https://www.bainil.com/api/v2/album/wish"
        // e.g. "https://www.bainil.com/api/v2/album/wish?albumId=3276&userId=2543&wish=true"

        fun doWishTo(mContext: Context, albumId: Long, wish: Boolean) {
            // check network status
            val connMgr = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                // check login
                UserInfo.checkLoginThenRun(mContext, {
                    doAsync(ThisApp.ExceptionHandler) {
                        var success: Boolean
                        try {
                            success = RequestToggleWish(albumId, UserInfo.Companion.getUserIdOrExcept(mContext), wish).execute()
                        } catch (e: Exception) {
                            success = false
                            e.printStackTrace()
                        }

                        uiThread {
                            val msg: String
                            if (success)
                                if (wish)
                                    msg = ThisApp.AppContext.getString(R.string.msg_wishlist_add_succeed)
                                else
                                    msg = ThisApp.AppContext.getString(R.string.msg_wishlist_remove_succeed)
                            else
                                if (wish)
                                    msg = ThisApp.AppContext.getString(R.string.msg_wishlist_add_failed)
                                else
                                    msg = ThisApp.AppContext.getString(R.string.msg_wishlist_remove_failed)
                            mContext.toast(msg)
                        }
                    }
                }, { })
            } else {
                mContext.toast(ThisApp.AppContext.getString(R.string.msg_err_check_network_connection))
            }
        }
    }
}
