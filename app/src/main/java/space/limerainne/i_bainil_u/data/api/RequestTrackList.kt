package space.limerainne.i_bainil_u.data.api

import com.google.gson.Gson
import com.github.salomonbrys.kotson.*

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestTrackList(val albumId: Long,
                         val lang: String = "ko",
                         val gson: Gson = Gson()) : Request {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/store/album/tracks"
    }

    private fun composeURL(): String {
        var url = "$URL?albumId=$albumId"
        url += "&lang=$lang"
        return url
    }

    override fun execute(): TrackList {
        val trackListJSONStr = java.net.URL(composeURL()).readText()
        return gson.fromJson<TrackList>(trackListJSONStr)
    }
}