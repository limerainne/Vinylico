package space.limerainne.i_bainil_u.toolbox

import android.content.Context
import android.net.ConnectivityManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.credential.UserInfo
import space.limerainne.i_bainil_u.data.api.request.data.RequestAlbumPurchased
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.view.MainActivity
import space.limerainne.i_bainil_u.view.detail.AlbumInfoFragment
import space.limerainne.i_bainil_u.view.webview.PurchaseWebviewFragment

/**
 * Created by CottonCandy on 2016-10-03.
 *
 * Per-song purchase
 * #	Result	Protocol	Host	URL	Body	Caching	Content-Type	Process	Comments	Custom
1211	200	HTTP	www.bainil.com	/api/v2/purchase/request?userId=2543&trackId=13694&store=1&type=pay	75	no-cache="set-cookie"	application/json;charset=UTF-8	qemu-system-i386:14624
#	Result	Protocol	Host	URL	Body	Caching	Content-Type	Process	Comments	Custom
1212	200	HTTP	www.bainil.com	/api/v2/kakaopay/request?albumId=3199&trackId=13694&userId=2543&seq=99714	8,194	no-cache="set-cookie"	text/html;charset=UTF-8	qemu-system-i386:14624
 */

class PurchaseTool  {
    companion object    {
        fun purchaseAlbum(mContext: Context, item: AlbumEntry) = purchaseAlbum(mContext, item.albumId, item.albumName, item.free)

        fun purchaseAlbum(mContext: Context, albumId: Long, albumName: String, free: Boolean) {
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
                        val purchaseCheckResponse: RequestAlbumPurchased.Response
                        try {
                            val userInfo = UserInfo(mContext)

                            purchaseCheckResponse = RequestAlbumPurchased(albumId, userInfo.userId, free).execute()
                        } catch (e: Exception) {
                            purchaseCheckResponse = RequestAlbumPurchased.Response(false, 0L)
                            e.printStackTrace()
                        }

                        // 2. redirect to purchase page
                        if (purchaseCheckResponse.notBought) {
                            if (mContext is MainActivity) {
                                uiThread {
                                    // TODO display purchase info & cautions
                                    val userInfo = UserInfo(mContext)

                                    val webviewFragment = PurchaseWebviewFragment.newInstance(userInfo.userId, albumId, purchaseCheckResponse.seqId, mContext)
                                    mContext.transitToFragment(R.id.placeholder_top, webviewFragment, PurchaseWebviewFragment.TAG)
                                }
                            }
                        } else {
                            uiThread {
                                if (!free)
                                    mContext.toast("${mContext.getString(R.string.msg_err_album_already_purchased)}: ${albumName}")
                                else
                                    mContext.toast("${mContext.getString(R.string.msg_notice_free_album)}: ${albumName}")
                            }
                        }

                        // 3. TODO check if purhcase succeed (when? where?)
                    }
                }, {})
            } else {
                mContext.toast(mContext.getString(R.string.msg_err_check_network_connection))
            }
        }
    }
}