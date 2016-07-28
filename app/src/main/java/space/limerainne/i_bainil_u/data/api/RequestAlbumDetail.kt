package space.limerainne.i_bainil_u.data.api

import com.google.gson.Gson

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestAlbumDetail(val albumId: Long,
                         val userId: Long,
                         val store: Int = 1,
                         val lang: String = "ko",
                         val gson: Gson = Gson()) : Request {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/store/album"
    }

    private fun composeURL(): String {
        var url = "$URL?albumId=$albumId"
        url += "&userId=$userId"
        url += "store=$store&lang=$lang"
        return url
    }

    override fun execute(): AlbumDetail {
        val albumDetailJsonStr = java.net.URL(composeURL()).readText()
        return gson.fromJson(albumDetailJsonStr, AlbumDetail::class.java)
    }
}