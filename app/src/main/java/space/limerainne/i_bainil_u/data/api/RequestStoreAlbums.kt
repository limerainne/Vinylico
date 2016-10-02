package space.limerainne.i_bainil_u.data.api

import com.google.gson.Gson
import com.github.salomonbrys.kotson.*

/**
 * Created by Limerainne on 2016-07-21.
 */
class RequestStoreAlbums(val userId: Long,  // MANDATORY
                         val category: String = CATEGORY_FEATURED,
                         val offset: Long = 0,
                         val limit: Long = 20,
                         val lang: String = "ko",
                         val gson: Gson = Gson()) : Request {

    // NOTE page number (=offset) is calculated by server regarding "limit" (item/page)!

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/store/albums"
        // e.g. http://www.bainil.com/api/v2/store/albums/new?userId=2&offset=0&limit=10&lang=ko

        public val CATEGORY_FEATURED = "featured"  // 추천
        public val CATEGORY_NEW = "new"            // 신규
        public val CATEGORY_TOP = "top"            // 인기
        public val CATEGORY_XSFM = "xsfm"            // XSFM 방송곡
        // ...

    }

    private fun composeURL() = "$URL/$category?userId=$userId&offset=$offset&limit=$limit&lang=$lang"

    override fun execute(): StoreAlbums {
        val storeAlbumsJsonStr = java.net.URL(composeURL()).readText()
        val albums = gson.fromJson<StoreAlbums>(storeAlbumsJsonStr)
        albums.offset = offset
        albums.limit = limit
        return albums
    }
}