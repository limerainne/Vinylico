package space.limerainne.i_bainil_u.base

import android.content.Context
import android.net.ConnectivityManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.data.api.RequestAlbumPurchased
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.view.MainActivity
import space.limerainne.i_bainil_u.view.webview.PurchaseWebviewFragment

/**
 * Created by CottonCandy on 2016-10-03.
 */

class PurchaseTool  {
    companion object    {
        fun purchaseAlbum(mContext: Context, item: AlbumEntry) {
            /*
            http://www.bainil.com/api/v2/purchase/request?userId=2543&albumId=2423&store=1&type=pay

            http://www.bainil.com/api/v2/kakaopay/request?albumId=3276&userId=2543
             */
            val connMgr = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            if (networkInfo != null && networkInfo!!.isConnected) {
                // check login first
                UserInfo.checkLoginThenRun(mContext, {
                    doAsync {
                        // 1. check if previously purchased
                        val success: Boolean
                        try {
                            val userInfo = UserInfo(mContext)

                            success = RequestAlbumPurchased(item.albumId, userInfo.userId, true).execute()
                        } catch (e: Exception) {
                            success = false
                            e.printStackTrace()
                        }

                        // 2. redirect to purchase page
                        if (success) {
                            if (mContext is MainActivity) {
                                uiThread {
                                    // TODO display purchase info & cautions
                                    val userInfo = UserInfo(mContext)

                                    val webviewFragment = PurchaseWebviewFragment.newInstance(userInfo.userId, item.albumId, mContext)
                                    mContext.transitToFragment(R.id.placeholder_top, webviewFragment, PurchaseWebviewFragment.TAG)
                                }
                            }
                        } else {
                            uiThread {
                                mContext.toast("Already purchased this album: ${item.albumName}")
                            }
                        }

                        // 3. TODO check if purhcase succeed (when? where?)
                    }
                }, {})
            } else {
                mContext.toast("Check network connection!")
            }
        }
    }
}