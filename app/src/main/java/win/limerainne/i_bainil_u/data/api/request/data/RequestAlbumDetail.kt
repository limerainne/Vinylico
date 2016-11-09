package win.limerainne.i_bainil_u.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.data.api.AlbumDetail
import win.limerainne.i_bainil_u.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestAlbumDetail(val albumId: Long,
                         val userId: Long,
                         val store: Int = 1,
                         val lang: String = ThisApp.LangCode,
                         val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/store/album"
    }

    override fun composeURL(): String {
        var url = "${URL}?albumId=$albumId"
        url += "&userId=$userId"
        url += "&store=$store&lang=$lang"
        return url
    }

    override fun execute(): AlbumDetail {
        val albumDetailJsonStr = getHTTPResponseString()
        return gson.fromJson<AlbumDetail>(albumDetailJsonStr)
    }
}