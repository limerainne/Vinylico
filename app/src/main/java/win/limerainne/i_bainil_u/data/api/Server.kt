package win.limerainne.i_bainil_u.data.api

import win.limerainne.i_bainil_u.data.api.request.*
import win.limerainne.i_bainil_u.data.api.request.data.*
import win.limerainne.i_bainil_u.domain.model.AlbumDetail
import win.limerainne.i_bainil_u.domain.model.Connected
import win.limerainne.i_bainil_u.domain.model.RecommendAlbum
import win.limerainne.i_bainil_u.domain.model.StoreAlbums
import win.limerainne.i_bainil_u.domain.model.TrackList
import win.limerainne.i_bainil_u.domain.model.Wishlist
import win.limerainne.i_bainil_u.domain.model.Events
import win.limerainne.i_bainil_u.domain.model.SearchResult

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
        val result = RequestConnected(userId).execute()
        val converted = dataMapper.convertConnectedRespondToDomain(userId, result)
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

    fun requestRecommendAlbum(userId: Long): RecommendAlbum {
        val resultRecommendAlbum = RequestRecommendAlbum(userId).execute()
        if (!resultRecommendAlbum.success || resultRecommendAlbum.result.albumId == null)  {
            throw Exception()
        }

        val resultAlbumDetail = RequestAlbumDetail(resultRecommendAlbum.result.albumId, userId).execute()

        return dataMapper.convertRecommendAlbumToDomain(resultRecommendAlbum.result, resultAlbumDetail)
    }

    fun requestEvents(userId: Long): Events {
        val resultEvents = RequestEventBanner(userId).execute()
        return dataMapper.convertEventsToDomain(resultEvents)
    }

    fun requestSearchResult(keyword: String, userId: Long): SearchResult  {
        val resultSearchResult = RequestSearch(keyword, userId).execute()
        return dataMapper.covertSearchResultToDomain(keyword, resultSearchResult)
    }
}