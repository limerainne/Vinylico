package space.limerainne.i_bainil_u.data.api

import space.limerainne.i_bainil_u.domain.model.Wishlist as Wishlist

/**
 * Created by Limerainne on 2016-07-21.
 */
class WishlistServer(val dataMapper: APIDataMapper = APIDataMapper()) {

    fun requestWishlist(userId: Long) : Wishlist  {
        val result = RequestWishlist(userId).execute()
        val converted = dataMapper.convertWishlistToDomain(userId, result)
        return converted
    }
}