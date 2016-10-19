package space.limerainne.i_bainil_u.domain.model

/**
 * Created by Limerainne on 2016-07-21.
 */

// Wishlist
data class Wishlist(val userId: Long,
                    val albumEntries: List<AlbumEntry>) {
}

// Store albums list
data class StoreAlbums(val userId: Long,
                       val category: String,
                       var offset: Long,
                       var limit: Long,
                       val albumEntries: MutableList<AlbumEntry>)

// entry for each list (featured, new, ..., wishlist)
data class AlbumEntry(val albumId: Long,
                      val albumName: String,
                      val albumType: Int,
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
                      var purchased: Int,
                      val releaseDate: String,
                      val tracks: Int,

                      val songId: Int = -1,
                      val songName: String = "",
                      val songPath: String = "",

                      val purchasedDate: String = ""
                      )

// Connected
data class Connected(val userId: Long,
                    val albumEntries: List<AlbumEntry>) {
}

// album information in album detail page
data class AlbumDetail(val albumCredit: String,
                       val albumDesc: String,
                       val albumId: Long,
                       val albumName: String,
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
                       val wishCount: Int,

                       var purchased: Int = -1
                       )

fun convertToAlbumEntry(albumDetail: AlbumDetail, purchased: Int = 0): AlbumEntry = with (albumDetail) {
    AlbumEntry(
        albumId,
        albumName,
        albumType,
        artistId,
        artistName,
        event,
        feature_aac,
        feature_adult,
        feature_booklet,
        feature_hd,
        feature_lyrics,
        feature_rec,
        free,
        jacketImage,
        price,
        purchased,  // no purchase info -_-
        releaseDate,
        tracks,

        songId = -1,
        songName = "",
        songPath = "",

        purchasedDate = ""
    )
}

// track list
data class TrackList(val albumId: Long,
                     val tracks: List<Track>,
                     var duration: Int,
                     var bitrate: String,
                     var albumSize: Long,
                     var priceIfPerSong: String)

// track information
data class Track(val artistId: Long,
                 val artistName: String,
                 val bitrate: String,   // TODO just for MP3?
                 val connected_msg: Int,    // TODO #msg? or msg itself?
                 val duration: Int, // in second unit
                 val feature_aac: Boolean,  // TODO is there any AAC music?
                 val feature_adult: Boolean,
                 val feature_hd: Boolean,   // TODO is there any FLAC music?
                 val feature_lyrics: Boolean,
                 val feature_rec: Boolean,
                 val iap: String,   // TODO ??
                 val lyricsPath: String,    // can download lyrics!
                 val price: String,     // in dollar; flag for per-song buy
                 val saleType: String,  // TODO ?
                 val songId: Long,      // could be used to get preview
                 val songName: String,
                 val songOrder: Int,    // track number
                 val songPath: String,  // download path
                 val songSize: Long, // in bit unit

                 val perSongPayable: Boolean,
                 var lyricLoaded: Boolean = false,
                 var lyric_text: String = ""
)

data class RecommendAlbum(val albumId: Long,
                          val albumDetail: AlbumDetail,
                          val fans: List<Fan>)

data class Fan(val userPic: String,
               val userId: Long,
               val userName: String,
               val userRole: String)

data class Events(val count: Int,
                  val events: List<Event>)

data class Event(val bannerImage: String,
                 val seq: String,
                 val eventUrl: String,
                 val eventName: String)

data class SearchResult(val keyword: String,
                        val artists: List<SearchArtist>,
                        val albums: List<SearchAlbum>,
                        val tracks: List<SearchTrack>)

data class SearchArtist(val artistPicture: String,
                        val albumName: String,
                        val albumId: Long,
                        val artistId: Long,
                        val artistName: String,
                        val trackList: List<SearchTrack>)

data class SearchAlbum(val albumName: String,
                       val albumId: Long,
                       val albumType: Int,
                       val eventUrl: String,
                       val tracks: Int,
                       val free: Boolean,
                       val releaseDate: String,
                       val artistId: Long,
                       val event: Boolean,
                       val artistName: String,
                       val trackList: List<SearchTrack>,
                       val jacketImage: String)

data class SearchTrack(val albumId: Long,
                       val artistId: Long,
                       val songName: String,
                       val artistName: String,
                       val songId: Long,
                       val songOrder: Int)