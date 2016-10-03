package space.limerainne.i_bainil_u.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.Gson
import com.github.salomonbrys.kotson.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.base.UserInfo
import space.limerainne.i_bainil_u.view.MainActivity
import java.io.*
import java.net.HttpURLConnection

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestToggleWish(val albumId: Long,
                        val userId: Long,
                        val wish: Boolean) : RequestHTTPConnection() {

    override fun composeURL() = "$URL?albumId=$albumId&userId=$userId&wish=" +
                                    if (wish) "true" else "false"

    override fun execute(): Boolean {
        return getHTTPResponseString().contains("true")
    }

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/album/wish"
        // e.g. "http://www.bainil.com/api/v2/album/wish?albumId=3276&userId=2543&wish=true"

        fun doWishTo(mContext: Context, albumId: Long, wish: Boolean) {
            // check network status
            val connMgr = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            if (networkInfo != null && networkInfo!!.isConnected) {
                // check login
                UserInfo.checkLoginThenRun(mContext, {
                    doAsync {
                        val success: Boolean
                        try {
                            success = RequestToggleWish(albumId, UserInfo.getUserIdOrExcept(mContext), wish).execute()
                        } catch (e: Exception) {
                            success = false
                            e.printStackTrace()
                        }

                        uiThread {
                            val msg: String
                            if (success)
                                if (wish)
                                    msg = "Adding album to wishlist succeed!"
                                else
                                    msg = "Removing album from wishlist succeed!"
                            else
                                if (wish)
                                    msg = "Failed to add album to wishlist..."
                                else
                                    msg = "Failed to remove album from wishlist..."
                            mContext.toast(msg)
                        }
                    }
                }, { })
            } else {
                mContext.toast("Check network connection!")
            }
        }
    }
}
