package space.limerainne.i_bainil_u.data.api

/**
 * Created by Limerainne on 2016-07-19.
 */

// wishlist
data class Wishlist(val success: Boolean,
                    val result: List<WishAlbum>)

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

