package win.limerainne.vinylico.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.data.api.AlbumDetail
import win.limerainne.vinylico.data.api.ArtistDetail
import win.limerainne.vinylico.data.api.ArtistDetailWrapper
import win.limerainne.vinylico.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestArtistDetail(val artistId: Long,
                          val lang: String = ThisApp.LangCode,
                          val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "https://www.bainil.com/api/v2/artist"
    }

    override fun composeURL(): String {
        var url = "${URL}?artistId=$artistId"
        url += "&lang=$lang"
        return url
    }

    override fun execute(): ArtistDetailWrapper {
        val artistDetailJsonStr = getHTTPResponseString()
        return gson.fromJson<ArtistDetailWrapper>(artistDetailJsonStr)
    }
}