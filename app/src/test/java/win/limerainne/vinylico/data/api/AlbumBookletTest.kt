package win.limerainne.vinylico.data.api

import org.junit.Assert.*
import org.junit.Test
import win.limerainne.vinylico.domain.model.AlbumBooklet as DomainAlbumBooklet

/**
 * Created by Limerainne on 2016-07-21.
 */

class AlbumBookletTest {

    companion object    {
        val USER_ID: Long = 2
        val ALBUM_ID: Long = 2423
        // AlbumID NOTE
        // 2423: A New Trilogy by Lovelyz
        // 3872: R U Ready by Lovelyz (includes YouTube video)
        // 1003: Love, Curse, Suicide by UMC/UW
        // 549: My Last Song by Joon
        val ALBUM_WITH_VIDEO_ID: Long = 3872
    }

    @Test
    fun testCanGetAlbumBooklet()  {
        val wishServer = Server()
        val result: DomainAlbumBooklet = wishServer.requestAlbumBooklet(ALBUM_ID)

        // check type of result data
        assertTrue(result is DomainAlbumBooklet)

        // check if albumId matches
        assertEquals(result.albumId, ALBUM_ID)

        // print album description
        with(result) {
            println("- $albumDesc of $albumId")
        }

        // is there any booklet or photo?
        assertTrue(result.booklets.isNotEmpty() or result.photos.isNotEmpty())
    }

    @Test
    fun testCanGetVideoFromAlbumBooklet()  {
        val wishServer = Server()
        val result: DomainAlbumBooklet = wishServer.requestAlbumBooklet(ALBUM_WITH_VIDEO_ID)

        // check type of result data
        assertTrue(result is DomainAlbumBooklet)

        // check if albumId matches
        assertEquals(result.albumId, ALBUM_ID)

        // is there any video?
        assertTrue(result.videos.isNotEmpty())

        // get first (and only) video
        val video = result.videos[0]

        // is there any link?
        assertFalse(video.videoUrl.isNullOrEmpty())
    }
}