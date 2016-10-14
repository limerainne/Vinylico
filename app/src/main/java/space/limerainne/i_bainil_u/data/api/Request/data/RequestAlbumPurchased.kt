package space.limerainne.i_bainil_u.data.api.request.data

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestAlbumPurchased(val albumId: Long,
                        val userId: Long,
                        val wish: Boolean) : RequestHTTPConnection() {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/purchase/request"
        // e.g. http://www.bainil.com/api/v2/purchase/request?userId=2543&albumId=2423&store=1&type=pay
    }

    override fun composeURL() = "${URL}?userId=$userId&albumId=$albumId&store=1&type=pay"

    override fun execute(): Boolean {
        // NOTE true means "purchasable"
        val responseString = getHTTPResponseString()
        return (responseString.contains("false") and responseString.contains("PURCHASE_NOT_BUY"))
    }
}