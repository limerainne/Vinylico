package space.limerainne.i_bainil_u.data.api

import space.limerainne.i_bainil_u.domain.model.Wishlist as DomainWishlist
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainWishAlbum

import space.limerainne.i_bainil_u.domain.model.Connected as DomainConnected
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainConnectedAlbum

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

    private fun convertWishAlbumListToDomain(list: List<AlbumEntry>): List<DomainWishAlbum> {
        return list.map { convertWishAlbumToDomain(it) }
    }

    private fun convertWishAlbumToDomain(albumEntry: AlbumEntry): DomainWishAlbum = with(albumEntry) {
        DomainWishAlbum(
                albumId ?: -1,
                albumName ?: "",
                albumType ?: -1,
                artistId ?: -1,
                artistName ?: "",
                event ?: false,
                feature_aac ?: false,
                feature_adult ?: false,
                feature_booklet ?: false,
                feature_hd ?: false,
                feature_lyrics ?: false,
                feature_rec ?: false,
                free ?: false,
                jacketImage ?: "",
                price ?: "",
                purchased ?: -1,
                releaseDate ?: "",
                tracks ?: -1)
    }

    fun convertConnectedRespondToDomain(userId: Long, connected: Connected) = with(connected) {
        DomainConnected(userId, convertConnectedAlbumListToDomain(result))
    }

    private fun convertConnectedAlbumListToDomain(list: List<AlbumEntry>): List<DomainConnectedAlbum> {
        return list.map { convertConnectedAlbumToDomain(it) }
    }

    private fun convertConnectedAlbumToDomain(albumEntry: AlbumEntry): DomainConnectedAlbum = with(albumEntry) {
        DomainConnectedAlbum(
                albumId ?: -1,
                albumName ?: "",
                albumType ?: -1,
                artistId ?: -1,
                artistName ?: "",
                event ?: false,
                feature_aac ?: false,
                feature_adult ?: false,
                feature_booklet ?: false,
                feature_hd ?: false,
                feature_lyrics ?: false,
                feature_rec ?: false,
                free ?: false,
                jacketImage ?: "",
                price ?: "",
                purchased ?: -1,
                releaseDate ?: "",
                tracks ?: -1)
    }

    fun convertAlbumDetailRespondToDomain(albumDetail: AlbumDetail): DomainAlbumDetail {
        if (!albumDetail.success)
            throw RuntimeException("Incorrect ID or fetch failed!")
        with(albumDetail.result) {
            return DomainAlbumDetail(albumCredit ?: "",
                    albumDesc ?: "",
                    albumId ?: -1,
                    albumName ?: "",
                    albumType ?: -1,
                    artistId ?: -1,
                    artistName ?: "",
                    backImage ?: "",
                    cdImage ?: "",
                    event ?: false,
                    eventUrl ?: "",
                    fans ?: -1,
                    feature_aac ?: false,
                    feature_adult ?: false,
                    feature_booklet ?: false,
                    feature_hd ?: false,
                    feature_lyrics ?: false,
                    feature_rec ?: false,
                    free ?: false,
                    genre ?: "",
                    genreCategory ?: "",
                    genreCode ?: "",
                    genreId ?: -1,
                    iap ?: "",
                    jacketImage ?: "",
                    labelId ?: -1,
                    labelName ?: "",
                    price ?: "",
                    publishId ?: -1,
                    publishName ?: "",
                    releaseDate ?: "",
                    tracks ?: -1,
                    wish ?: -1,
                    wishCount ?: -1)
        }
    }

    fun convertTrackListRespondToDomain(albumId: Long, trackList: TrackList) = with(trackList) {
        DomainTrackList(albumId, convertTrackListToDomain(result))
    }

    private fun convertTrackListToDomain(list: List<Track>): List<DomainTrack> {
        return list.map { convertTrackToDomain(it) }
    }

    private fun convertTrackToDomain(track: Track): DomainTrack = with(track) {
        assert(songId != null)

        DomainTrack(artistId ?: -1,
                artistName ?: "",
                bitrate ?: "",    // TODO bitrate field removed (16.08.17)
                connected_msg ?: -1,
                duration ?: -1,
                feature_aac ?: false,
                feature_adult ?: false,
                feature_hd ?: false,
                feature_lyrics ?: false,
                feature_rec ?: false,
                iap ?: "",
                lyricsPath ?: "",
                price ?: "",
                saleType ?: "",
                songId ?: -1,
                songName ?: "",
                songOrder ?: -1,
                songPath ?: "",
                songSize ?: -1)
    }
}
