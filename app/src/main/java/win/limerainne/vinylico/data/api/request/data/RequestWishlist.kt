package win.limerainne.vinylico.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.vinylico.data.api.Wishlist
import win.limerainne.vinylico.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestWishlist(val userId: Long,
                      val wish: String = "album",
                      val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/user/wishes"
    }

    override fun composeURL() = "${URL}?userId=$userId&wish=$wish"
    // &offset=0&limit=20&lang=en // not working?

    override fun execute(): Wishlist {
        val wishlistJsonStr = getHTTPResponseString()
        return gson.fromJson<Wishlist>(wishlistJsonStr)
    }
}