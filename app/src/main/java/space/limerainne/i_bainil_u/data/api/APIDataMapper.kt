package space.limerainne.i_bainil_u.data.api

import space.limerainne.i_bainil_u.domain.model.Wishlist as DomainWishlist
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainWishAlbum

import space.limerainne.i_bainil_u.domain.model.AlbumDetail as DomainAlbumDetail

import space.limerainne.i_bainil_u.domain.model.TrackList as DomainTrackList
import space.limerainne.i_bainil_u.domain.model.Track as DomainTrack

/**
 * Created by Limerainne on 2016-07-21.
 */
class APIDataMapper {

    fun convertWishlistRespondToDomain(userId: Long, wishlist: Wishlist) = with(wishlist) {
        DomainWishlist(userId, convertWishAlbumListToDomain(result))
    }

    private fun convertWishAlbumListToDomain(list: List<WishAlbum>): List<DomainWishAlbum> {
        return list.map { convertWishAlbumToDomain(it) }
    }

    private fun convertWishAlbumToDomain(wishAlbum: WishAlbum): DomainWishAlbum = with(wishAlbum) {
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

    fun convertAlbumDetailRespondToDomain(albumDetail: AlbumDetail): DomainAlbumDetail {
        if (!albumDetail.success)
            throw RuntimeException("Incorrect ID or fetch failed!")
        with(albumDetail.result[0]) {
            return DomainAlbumDetail(albumCredit,
                    albumDesc,
                    albumDescEnglish,
                    albumId,
                    albumType,
                    artistId,
                    artistName,
                    backImage,
                    cdImage,
                    event,
                    eventUrl,
                    fans,
                    feature_aac,
                    feature_adult,
                    feature_booklet,
                    feature_hd,
                    feature_lyrics,
                    feature_rec,
                    free,
                    genre,
                    genreCategory,
                    genreCode,
                    genreId,
                    iap,
                    jacketImage,
                    labelId,
                    labelName,
                    price,
                    publishId,
                    publishName,
                    releaseData,
                    tracks,
                    wish,
                    wishCount)
        }
    }

    fun convertTrackListRespondToDomain(albumId: Long, trackList: TrackList) = with(trackList) {
        DomainTrackList(albumId, convertTrackListToDomain(result))
    }

    private fun convertTrackListToDomain(list: List<Track>): List<DomainTrack> {
        return list.map { convertTrackToDomain(it) }
    }

    private fun convertTrackToDomain(track: Track): DomainTrack = with(track) {
        DomainTrack(artistId,
                artistName,
                bitrate,
                connected_msg,
                duration,
                feature_aac,
                feature_adult,
                feature_hd,
                feature_lyrics,
                feature_rec,
                iap,
                lyricsPath,
                price,
                saleType,
                songId,
                songName,
                songOrder,
                songPath,
                songSize)
    }
}
