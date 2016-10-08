package space.limerainne.i_bainil_u.data.api

import space.limerainne.i_bainil_u.extension.toDateString
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import space.limerainne.i_bainil_u.domain.model.Wishlist as DomainWishlist
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainWishAlbum

import space.limerainne.i_bainil_u.domain.model.StoreAlbums as DomainStoreAlbums
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainStoreAlbum

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
                convertDateStringInto(releaseDate),
                tracks ?: -1)
    }

    fun convertDateStringInto(date: String?): String {
        if (date == null)
            return ""

        val dateFormatInBainil = "yyyy-MM-dd"
        var dateFormatted: String
        try {
            dateFormatted = convertDateInto(SimpleDateFormat(dateFormatInBainil).parse(date), date)
        } catch(e: Exception)   {
            e.printStackTrace()
            dateFormatted = date    // just put as received
        }

        return dateFormatted
    }

    fun convertUnixDateStringInto(date: Long?): String  {
        if (date == null)
            return ""

        var dateFormatted: String
        try {
            // NOTE in some environment, you have to multiply 1000L into Unix timestamp
            dateFormatted = convertDateInto(Date(date), "")
        }   catch (e: Exception)    {
            e.printStackTrace()
            dateFormatted = ""
        }

        return dateFormatted
    }

    fun convertDateInto(date: Date, textIfException: String): String {
        var dateFormatted: String
        try {
            val formatText: String
            if (SimpleDateFormat("yyyy").format(date) == SimpleDateFormat("yyyy").format(Date()))
                formatText = "MM.dd"
            else
                formatText = "yyyy.MM.dd"

            dateFormatted = SimpleDateFormat(formatText).format(date)
        } catch(e: Exception)   {
            e.printStackTrace()
            dateFormatted = textIfException
        }

        return dateFormatted
    }

    fun convertStoreAlbumsToDomain(userId: Long, category: String, storeAlbums: StoreAlbums) = with(storeAlbums) {
        DomainStoreAlbums(userId, category, storeAlbums.offset, storeAlbums.limit, convertStoreAlbumListToDomain(result))
    }

    private fun convertStoreAlbumListToDomain(list: List<AlbumEntry>): MutableList<DomainStoreAlbum> {
        return list.map { convertStoreAlbumToDomain(it) }.toMutableList()
    }

    private fun convertStoreAlbumToDomain(albumEntry: AlbumEntry): DomainStoreAlbum = with(albumEntry) {
        DomainStoreAlbum(
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
                convertDateStringInto(releaseDate),
                tracks ?: -1)
    }

    fun convertConnectedRespondToDomain(userId: Long, connected: Connected) = with(connected) {
        DomainConnected(userId, convertConnectedAlbumListToDomain(result))
    }

    private fun convertConnectedAlbumListToDomain(list: List<AlbumEntry>): List<DomainConnectedAlbum> {
        return list.map { convertConnectedAlbumToDomain(it) }
    }

    fun convertConnectedAlbumToDomain(albumEntry: AlbumEntry): DomainConnectedAlbum = with(albumEntry) {
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
                1,  // every album in this list is already purchased
                convertDateStringInto(releaseDate),
                tracks ?: -1,
                purchasedDate=convertUnixDateStringInto(purchaseDate))
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
                    convertDateStringInto(releaseDate),
                    tracks ?: -1,
                    wish ?: -1,
                    wishCount ?: -1)
        }
    }

    fun convertTrackListRespondToDomain(albumId: Long, trackList: TrackList) = with(trackList) {
        val domTrackList = DomainTrackList(albumId, convertTrackListToDomain(result), -1, "", 0, "")

        // album duration
        var albumDuration: Int = 0
        try {
            for (track in domTrackList.tracks) {
                albumDuration += track.duration
            }
            domTrackList.duration = albumDuration
        } catch (e: Exception)  {
            e.printStackTrace()
            domTrackList.duration = -1
        }

        // album bitrate?
        // TODO just get first track's
        domTrackList.bitrate = domTrackList.tracks[0].bitrate

        // album size?
        var albumSize = 0L
        for (track in domTrackList.tracks)  {
            albumSize += track.songSize
        }
        domTrackList.albumSize = albumSize

        // sum of price per song
        var sumPricePerSong: Double = 0.0
        try {
            for (track in domTrackList.tracks) {
                sumPricePerSong += track.price.toDouble()
            }
            domTrackList.priceIfPerSong = sumPricePerSong.toString()
        } catch (e: Exception)  {
            e.printStackTrace()
            domTrackList.priceIfPerSong = ""
        }

        domTrackList
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
