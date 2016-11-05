package win.limerainne.i_bainil_u.data.api

import org.junit.Test
import org.junit.Assert.*
import win.limerainne.i_bainil_u.data.api.request.data.RequestStoreAlbums

import win.limerainne.i_bainil_u.domain.model.StoreAlbums as DomainStoreAlbums
import win.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainStoreAlbum

/**
 * Created by Limerainne on 2016-07-21.
 */

class StoreAlbumsTest  {

    companion object    {
        val USER_ID: Long = 2543
        val category: String = RequestStoreAlbums.CATEGORY_FEATURED
        val offset = 0L
        val limit = 20L
    }

    @Test
    fun testCanGetStoreAlbums()  {
        val server = Server()
        val result: DomainStoreAlbums = server.requestStoreAlbums(category, USER_ID, offset, limit)

        // check type of result data
        assertTrue(result is DomainStoreAlbums)

        // check if userId matches
        assertEquals(result.userId, USER_ID)
        assertEquals(result.category, category)

        // check length
        assertTrue(result.albumEntries.size > 0)

        // print contents
        result.albumEntries.map { with(it)    {
            println("- $albumName [$albumId] by $artistName [$artistId] @ $releaseDate")
        }}
    }
}