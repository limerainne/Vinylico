package space.limerainne.i_bainil_u.domain.model

/**
 * Created by Limerainne on 2016-07-21.
 */

// Wishlist
data class Wishlist(val userId: Long,
                    val albums: List<WishAlbum>) {
}

data class WishAlbum(val albumEnglish: String,
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