package space.limerainne.i_bainil_u.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import space.limerainne.i_bainil_u.data.api.Connected
import space.limerainne.i_bainil_u.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestConnected(val userId: Long,
                       val lang: String = "ko",
                      val gson: Gson = Gson()) : Request {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/user/connected/albums"
    }

    private fun composeURL() = "${URL}?userId=$userId&lang=$lang"

    override fun execute(): Connected {
        val JsonStr = java.net.URL(composeURL()).readText()
        return gson.fromJson<Connected>(JsonStr)
    }
}