package space.limerainne.i_bainil_u.data.api

import space.limerainne.i_bainil_u.domain.model.Wishlist
import space.limerainne.i_bainil_u.domain.model.StoreAlbums
import space.limerainne.i_bainil_u.domain.model.Connected
import space.limerainne.i_bainil_u.domain.model.AlbumDetail
import space.limerainne.i_bainil_u.domain.model.TrackList

/**
 * Created by Limerainne on 2016-07-21.
 */
class Server(val dataMapper: APIDataMapper = APIDataMapper()) {

    fun requestWishlist(userId: Long) : Wishlist  {
        val result = RequestWishlist(userId).execute()
        val converted = dataMapper.convertWishlistRespondToDomain(userId, result)
        return converted
    }

    fun requestConnected(userId: Long) : Connected  {
        println("API: get data from server")
        val result = RequestConnected(userId).execute()
        println("API: converting to domain")
        val converted = dataMapper.convertConnectedRespondToDomain(userId, result)
        println("API: got data..")
        return converted
    }

    fun requestAlbumDetail(albumId: Long, userId: Long) : AlbumDetail  {
        val result = RequestAlbumDetail(albumId, userId).execute()
        val converted = dataMapper.convertAlbumDetailRespondToDomain(result)
        return converted
    }

    fun requestTrackList(albumId: Long, userId: Long) : TrackList  {
        val result = RequestTrackList(albumId).execute()
        val converted = dataMapper.convertTrackListRespondToDomain(albumId, result)
        return converted
    }

    fun requestStoreAlbums(category: String, userId: Long, offset: Long = 0, limit: Long = 20) : StoreAlbums  {
        val result = RequestStoreAlbums(userId, category, offset, limit).execute()
        val converted = dataMapper.convertStoreAlbumsToDomain(userId, category, result)
        return converted
    }
}