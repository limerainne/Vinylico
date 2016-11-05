package space.limerainne.i_bainil_u.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.data.api.StoreAlbums
import space.limerainne.i_bainil_u.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestStoreAlbums(val userId: Long,  // MANDATORY
                         val category: String = CATEGORY_FEATURED,
                         val offset: Long = 0,
                         val limit: Long = 20,
                         val lang: String = I_Bainil_UApp.LangCode,
                         val gson: Gson = Gson()) : Request {

    // NOTE page number (=offset) is calculated by server regarding "limit" (item/page)!

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/store/albums"
        // e.g. http://www.bainil.com/api/v2/store/albums/new?userId=2&offset=0&limit=10&lang=ko

        val CATEGORY_FEATURED = "featured"  // 추천
        val CATEGORY_NEW = "new"            // 신규
        val CATEGORY_TOP = "top"            // 인기
        val CATEGORY_XSFM = "xsfm"            // XSFM 방송곡
        // ...

    }

    private fun composeURL() = "${URL}/$category?userId=$userId&offset=$offset&limit=$limit&lang=$lang"

    override fun execute(): StoreAlbums {
        val storeAlbumsJsonStr = java.net.URL(composeURL()).readText()
        val albums = gson.fromJson<StoreAlbums>(storeAlbumsJsonStr)
        albums.offset = offset
        albums.limit = limit
        return albums
    }
}