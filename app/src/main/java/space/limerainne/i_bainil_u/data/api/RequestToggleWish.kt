package space.limerainne.i_bainil_u.data.api

import android.util.Log
import com.google.gson.Gson
import com.github.salomonbrys.kotson.*
import java.io.*
import java.net.HttpURLConnection

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestToggleWish(val albumId: Long,
                        val userId: Long,
                        val wish: Boolean) : RequestHTTPConnection() {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/album/wish"
        // e.g. "http://www.bainil.com/api/v2/album/wish?albumId=3276&userId=2543&wish=true"
    }

    override fun composeURL() = "$URL?albumId=$albumId&userId=$userId&wish=" +
                                    if (wish) "true" else "false"

    override fun execute(): Boolean {
        return getHTTPResponseString().contains("true")
    }

}