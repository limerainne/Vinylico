package win.limerainne.vinylico.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.data.api.AlbumBooklet
import win.limerainne.vinylico.data.api.AlbumDetail
import win.limerainne.vinylico.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestAlbumBooklet(val albumId: Long,
                          val lang: String = ThisApp.LangCode,
                          val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "https://www.bainil.com/api/v2/album/booklet2"
    }

    override fun composeURL(): String {
        var url = "${URL}?albumId=$albumId"
        url += "&lang=$lang"
        return url
    }

    override fun execute(): AlbumBooklet {
        val albumBookletJsonStr = getHTTPResponseString()
        return gson.fromJson<AlbumBooklet>(albumBookletJsonStr)
    }
}