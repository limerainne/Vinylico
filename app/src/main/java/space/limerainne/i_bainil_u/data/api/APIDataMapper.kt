package space.limerainne.i_bainil_u.data.api

import space.limerainne.i_bainil_u.domain.model.Wishlist as DomainWishlist
import space.limerainne.i_bainil_u.domain.model.WishAlbum as DomainWishAlbum

/**
 * Created by Limerainne on 2016-07-21.
 */
class APIDataMapper {

    fun convertWishlistToDomain(userId: Long, wishlist: Wishlist) = with(wishlist) {
        DomainWishlist(userId, convertWishAlbumListToDomain(result))
    }

    private fun convertWishAlbumListToDomain(list: List<WishAlbum>) : List<DomainWishAlbum> {
        return list.map { convertWishAlbumToDomain(it) }
    }

    private fun convertWishAlbumToDomain(wishAlbum: WishAlbum) : DomainWishAlbum = with(wishAlbum)    {
        DomainWishAlbum(albumEnglish,
                        albumId,
                        albumName,
                        albumType,
                        artistEnglish,
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
                        purchased,
                        releaseDate,
                        tracks)
    }
}