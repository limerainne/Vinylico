package space.limerainne.i_bainil_u.data.api

/**
 * Created by Limerainne on 2016-07-19.
 */

// wishlist
// ref: /api/v2/user/wishes?userId=()&offset=0&limit=20&lang=ko[/en]
data class Wishlist(val success: Boolean,
                    val result: List<AlbumEntry>)

// entry for each list (featured, new, ..., wishlist)
// - song** are not in wishlist
data class AlbumEntry(val albumEnglish: String,
                      val albumId: Long,
                      val albumName: String,
                      val albumType: Int,
                      val artistEnglish: String,
                      val artistId: Long,
                      val artistName: String,
                      val event: Boolean,
                      val eventUrl: String,
                      val feature_aac: Boolean,
                      val feature_adult: Boolean,
                      val feature_booklet: Boolean,
                      val feature_hd: Boolean,
                      val feature_lyrics: Boolean,
                      val feature_rec: Boolean,
                      val free: Boolean,
                      val jacketImage: String,
                      val price: String,
                      val purchased: Int,
                      val releaseDate: String,
                      val songEnglish: String,
                      val songId: Long,
                      val songName: String,
                      val songPath: String,
                      val tracks: Int)

// respond from album detail page request
// ref: /api/v2/store/album?albumId=()&userId()&store=1&lang=ko
data class AlbumDetail(val success: Boolean,
                       val result: List<Album>)

// album information in album detail page
data class Album(val albumCredit: String,
                 val albumDesc: String,
                 val albumDescEnglish: String,
                 val albumId: Long,
                 val albumType: Int,
                 val artistId: Long,
                 val artistName: String,
                 val backImage: String,
                 val cdImage: String,
                 val event: Boolean,
                 val eventUrl: String,
                 val fans: Int,
                 val feature_aac: Boolean,
                 val feature_adult: Boolean,
                 val feature_booklet: Boolean,
                 val feature_hd: Boolean,
                 val feature_lyrics: Boolean,
                 val feature_rec: Boolean,
                 val free: Boolean,
                 val genre: String,
                 val genreCategory: String,
                 val genreCode: String,
                 val genreId: Int,
                 val iap: String,
                 val jacketImage: String,
                 val labelId: Long,
                 val labelName: String,
                 val price: String,
                 val publishId: Long,
                 val publishName: String,
                 val releaseData: String,
                 val tracks: Int,
                 val wish: Int,
                 val wishCount: Int)

// track list
// ref: /api/v2/store/album/tracks?albumId=()&lang=ko
data class TrackList(val success: Boolean,
                       val result: List<Track>)

// track information
data class Track(val artistId: Long,
                 val artistName: String,
                 val bitrate: String,
                 val connected_msg: Int,    // TODO #msg? or msg itself?
                 val duration: Int,
                 val feature_aac: Boolean,
                 val feature_adult: Boolean,
                 val feature_hd: Boolean,
                 val feature_lyrics: Boolean,
                 val feature_rec: Boolean,
                 val iap: String,
                 val lyricsPath: String,
                 val price: String,
                 val saleType: String,
                 val songId: Long,
                 val songName: String,
                 val songOrder: Int,
                 val songPath: String,
                 val songSize: Long)
