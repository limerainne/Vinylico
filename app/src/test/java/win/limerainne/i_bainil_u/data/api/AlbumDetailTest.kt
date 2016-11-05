package win.limerainne.i_bainil_u.data.api

import org.junit.Test
import org.junit.Assert.*

import win.limerainne.i_bainil_u.domain.model.AlbumDetail as DomainAlbumDetail
import win.limerainne.i_bainil_u.domain.model.TrackList as DomainTrackList

/**
 * Created by Limerainne on 2016-07-21.
 */

class AlbumDetailTest  {

    companion object    {
        val USER_ID: Long = 2543
        val ALBUM_ID: Long = 2423
        // AlbumID NOTE
        // 2423: A New Trilogy by Lovelyz
        // 1003: Love, Curse, Suicide by UMC/UW
        // 549: My Last Song by Joon
    }

    @Test
    fun testCanGetAlbumDetail()  {
        val wishServer = Server()
        val result: DomainAlbumDetail = wishServer.requestAlbumDetail(ALBUM_ID, USER_ID)

        // check type of result data
        assertTrue(result is DomainAlbumDetail)

        // check if albumId matches
        assertEquals(result.albumId, ALBUM_ID)

        // print album name
        with(result) {
            println("- $albumName by $artistName")
        }
    }

    @Test
    fun testCanGetTrackList()  {
        val wishServer = Server()
        val result: DomainTrackList = wishServer.requestTrackList(ALBUM_ID, USER_ID)

        // check type of result data
        assertTrue(result is DomainTrackList)

        // check if albumId matches
        assertEquals(result.albumId, ALBUM_ID)

        // check length
        assertTrue(result.tracks.size > 0)

        // print album name
        result.tracks.map { with(it)    {
            println("- $songOrder. $songName [$songId; $bitrate] by $artistName [$artistId]")
        }}
    }
}