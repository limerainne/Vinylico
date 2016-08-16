package space.limerainne.i_bainil_u.domain.model

/**
 * Created by Limerainne on 2016-07-21.
 */

// Wishlist
data class Wishlist(val userId: Long,
                    val albumEntries: List<AlbumEntry>) {
}

// entry for each list (featured, new, ..., wishlist)
data class AlbumEntry(val albumEnglish: String,
                      val albumId: Long,
                      val albumName: String,
                      val albumType: Int,
                      val artistEnglish: String,
                      val artistId: Long,
                      val artistName: String,
                      val event: Boolean,
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
                      val tracks: Int)

// album information in album detail page
data class AlbumDetail(val albumCredit: String,
                       val albumDesc: String,
                       val albumDescEnglish: String,
                       val albumId: Long,
                       val albumName: String,
                       val albumEnglish: String,
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
                       val releaseDate: String,
                       val tracks: Int,
                       val wish: Int,
                       val wishCount: Int)

// track list
data class TrackList(val albumId: Long,
                     val tracks: List<Track>)

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