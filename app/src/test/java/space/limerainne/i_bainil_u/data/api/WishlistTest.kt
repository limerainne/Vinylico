package space.limerainne.i_bainil_u.data.api

import org.junit.Test
import org.junit.Assert.*

import space.limerainne.i_bainil_u.domain.model.Wishlist as DomainWishlist
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainWishAlbum

/**
 * Created by Limerainne on 2016-07-21.
 */

class WishlistTest  {

    companion object    {
        val USER_ID: Long = 2543
    }

    @Test
    fun testCanGetWishlist()  {
        val wishServer = Server()
        val result: DomainWishlist = wishServer.requestWishlist(USER_ID)

        // check type of result data
        assertTrue(result is DomainWishlist)

        // check if userId matches
        assertEquals(result.userId, USER_ID)

        // check length
        assertTrue(result.albumEntries.size > 0)

        // print contents
        result.albumEntries.map { with(it)    {
            println("- $albumName [$albumId] by $artistName [$artistId] @ $releaseDate")
        }}
    }
}