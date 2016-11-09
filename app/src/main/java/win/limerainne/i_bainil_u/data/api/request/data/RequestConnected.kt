package win.limerainne.i_bainil_u.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.data.api.Connected
import win.limerainne.i_bainil_u.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestConnected(val userId: Long,
                       val lang: String = ThisApp.LangCode,
                       val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/user/connected/albums"
    }

    override fun composeURL() = "${URL}?userId=$userId&lang=$lang"

    override fun execute(): Connected {
        val JsonStr = getHTTPResponseString()
        return gson.fromJson<Connected>(JsonStr)
    }
}