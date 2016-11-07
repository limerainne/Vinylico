package win.limerainne.i_bainil_u.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.data.api.TrackList
import win.limerainne.i_bainil_u.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestTrackList(val albumId: Long,
                       val lang: String = ThisApp.LangCode,
                       val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/store/album/tracks"
    }

    override fun composeURL(): String {
        var url = "${URL}?albumId=$albumId"
        url += "&lang=$lang"
        return url
    }

    override fun execute(): TrackList {
        val trackListJSONStr = getHTTPResponseString()
        return gson.fromJson<TrackList>(trackListJSONStr)
    }
}