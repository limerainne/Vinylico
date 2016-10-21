package space.limerainne.i_bainil_u.data.api

import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.extension.DelegatesExt
import java.text.SimpleDateFormat
import java.util.*
import space.limerainne.i_bainil_u.domain.model.AlbumDetail as DomainAlbumDetail
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainConnectedAlbum
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainStoreAlbum
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainWishAlbum
import space.limerainne.i_bainil_u.domain.model.Connected as DomainConnected
import space.limerainne.i_bainil_u.domain.model.Fan as DomainFan
import space.limerainne.i_bainil_u.domain.model.RecommendAlbum as DomainRecommendAlbum
import space.limerainne.i_bainil_u.domain.model.StoreAlbums as DomainStoreAlbums
import space.limerainne.i_bainil_u.domain.model.Track as DomainTrack
import space.limerainne.i_bainil_u.domain.model.TrackList as DomainTrackList
import space.limerainne.i_bainil_u.domain.model.Wishlist as DomainWishlist
import space.limerainne.i_bainil_u.domain.model.Events as DomainEvents
import space.limerainne.i_bainil_u.domain.model.Event as DomainEvent
import space.limerainne.i_bainil_u.domain.model.SearchResult as DomainSearchResult
import space.limerainne.i_bainil_u.domain.model.SearchArtist as DomainSearchArtist
import space.limerainne.i_bainil_u.domain.model.SearchAlbum as DomainSearchAlbum
import space.limerainne.i_bainil_u.domain.model.SearchTrack as DomainSearchTrack

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
                    labelName ?: "-",
                    price ?: "",
                    publishId ?: -1,
                    publishName ?: "-",
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
                decorateSongName(songName),
                songOrder ?: -1,
                songPath ?: "",
                songSize ?: -1,
                saleType == "0")    // TODO if saleType == "1", can't buy per song!
    }

    val decorateEnabled: Boolean by DelegatesExt.preference(I_Bainil_UApp.AppContext, "pref_view_remove_soundtrack_parenthesis", true, "space.limerainne.i_bainil_u_preferences")
    fun decorateSongName(songName: String?): String  {
        if (songName == null)   return ""

        var decorated = songName.trim()
        if (decorateEnabled)
            decorated = re_parenthesis_with_included.replace(decorated, "")

        return decorated
    }

    companion object    {
        val re_parenthesis_with_included = Regex("""\s?\(.* 삽입.*곡?\)""")
    }

    fun convertRecommendAlbumToDomain(recommendAlbum: RecommendAlbum, albumDetail: AlbumDetail): DomainRecommendAlbum {
        if (recommendAlbum.fans == null)
            throw Exception()

        return DomainRecommendAlbum(recommendAlbum.albumId ?: -1,
                convertAlbumDetailRespondToDomain(albumDetail),
                convertFansListToDomain(recommendAlbum.fans))
    }

    private fun convertFansListToDomain(list: List<Fan>): List<DomainFan> {
        return list.map { convertFanToDomain(it) }
    }

    private fun convertFanToDomain(fan: Fan): DomainFan = with(fan) {
        DomainFan(userPic ?: "",
                userId ?: -1,
                userName ?: "",
                userRole ?: "")
    }

    fun convertEventsToDomain(events: EventResponse): DomainEvents    {
        return DomainEvents(events.total, convertEventsListToDomain(events.result))
    }

    private fun convertEventsListToDomain(list: List<Event>): List<DomainEvent> {
        return list.map { convertEventToDomain(it) }
    }

    private fun convertEventToDomain(event: Event): DomainEvent = with(event)   {
        DomainEvent(bannerImage ?: "",
                seq ?: "",
                eventUrl ?: "",
                eventName ?: "")
    }

    fun covertSearchResultToDomain(keyword: String, searchResultResponse: SearchResultResponse): DomainSearchResult  {
        return DomainSearchResult(keyword,
                convertSearchArtistListToDomain(searchResultResponse.result.artists),
                convertSearchAlbumListToDomain(searchResultResponse.result.albums),
                convertSearchTrackListToDomain(searchResultResponse.result.tracks))
    }

    private fun convertSearchArtistListToDomain(list: List<SearchArtist>): List<DomainSearchArtist>   {
        return list.map { convertSearchArtistToDomain(it) }
    }

    private fun convertSearchArtistToDomain(artist: SearchArtist) = with(artist)    {
        DomainSearchArtist(artistPicture ?: "",
                albumName ?: "",
                albumId ?: 0,
                artistId ?: 0,
                artistName ?: "",
                convertSearchTrackListToDomain(trackList)
        )
    }

    private fun convertSearchAlbumListToDomain(list: List<SearchAlbum>): List<DomainSearchAlbum>   {
        return list.map { convertSearchAlbumToDomain(it) }
    }

    private fun convertSearchAlbumToDomain(album: SearchAlbum) = with(album)    {
        DomainSearchAlbum(albumName ?: "",
                albumId ?: 0,
                albumType ?: -1,
                eventUrl ?: "",
                tracks ?: 0,
                free ?: false,
                convertDateStringInto(releaseDate),
                artistId ?: 0,
                event ?: false,
                artistName ?: "",
                convertSearchTrackListToDomain(trackList),
                jacketImage ?: ""
                )
    }

    private fun convertSearchTrackListToDomain(list: List<SearchTrack>): List<DomainSearchTrack>   {
        return list.map { convertSearchTrackToDomain(it) }
    }

    private fun convertSearchTrackToDomain(track: SearchTrack) = with(track)    {
        DomainSearchTrack(albumId ?: 0,
                artistId ?: 0,
                decorateSongName(songName),
                artistName ?: "",
                songId ?: 0,
                songOrder ?: -1
                )
    }
}
