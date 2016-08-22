package space.limerainne.i_bainil_u.data.api

import org.junit.Test
import org.junit.Assert.*

import space.limerainne.i_bainil_u.domain.model.Connected as DomainConnected
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainConnectedAlbum

/**
 * Created by Limerainne on 2016-07-21.
 */

class ConnectedTest  {

    companion object    {
        val USER_ID: Long = 2543
    }

    @Test
    fun testCanGetConnected()  {
        val server = Server()
        val result: DomainConnected = server.requestConnected(USER_ID)

        // check type of result data
        assertTrue(result is DomainConnected)

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