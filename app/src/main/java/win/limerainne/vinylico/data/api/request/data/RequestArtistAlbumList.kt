package win.limerainne.vinylico.data.api.request.data

import android.util.Log
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.data.api.AlbumDetail
import win.limerainne.vinylico.data.api.ArtistAlbumListWrapper
import win.limerainne.vinylico.data.api.ArtistDetail
import win.limerainne.vinylico.data.api.ArtistDetailWrapper
import win.limerainne.vinylico.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
/* @17.09.17
 - https://www.bainil.com/api/v2/artist/albums?artistId=1005&lang=ko

{
  "result": [
    {
      "albumEnglish": "Lovelyz 2nd Album Repackage 'Now We'",
      "albumName": "Lovelyz 2nd Album Repackage [지금, 우리]",
      "albumId": 4195,
      "albumType": 0,
      "artistEnglish": "Lovelyz",
      "feature_rec": false,
      "feature_hd": false,
      "tracks": 13,
      "backImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/4195/34c14851-054c-4561-bc73-a93c5c12873b_640.jpg",
      "feature_lyrics": true,
      "releaseDate": "2017-05-02",
      "price": 8.99,
      "artistId": 1005,
      "owner": 1,
      "feature_aac": false,
      "feature_booklet": false,
      "cdImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/4195/891e8986-fd0d-46a2-8ac5-f490dfbec868_570.jpg",
      "artistName": "러블리즈",
      "genreName": "Dance-pop",
      "feature_adult": false,
      "jacketImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/4195/0bb66b1a-f31c-4d93-b510-e940d43b4154_360.jpg",
      "genreId": 1
    },
    {
      "albumEnglish": "Lovelyz 2nd Album `R U Ready?`",
      "albumName": "Lovelyz 2nd Album `R U Ready?`",
      "albumId": 3872,
      "albumType": 0,
      "artistEnglish": "Lovelyz",
      "feature_rec": false,
      "feature_hd": false,
      "tracks": 11,
      "backImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/3872/b1f80974-2bbe-47ce-999f-81d865366d29_640.jpg",
      "feature_lyrics": true,
      "releaseDate": "2017-02-26",
      "price": 8.99,
      "artistId": 1005,
      "owner": 1,
      "feature_aac": false,
      "feature_booklet": false,
      "cdImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/3872/37a5d51d-2893-4357-8691-3e83202d335d_570.jpg",
      "artistName": "러블리즈",
      "genreName": "Dance-pop",
      "feature_adult": false,
      "jacketImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/3872/cc6e14f1-352b-4432-b648-7bd41d84bbe5_360.jpg",
      "genreId": 1
    },
    {
      "albumEnglish": "A New Trilogy",
      "albumName": "A New Trilogy",
      "albumId": 2423,
      "albumType": 0,
      "artistEnglish": "Lovelyz",
      "feature_rec": false,
      "feature_hd": false,
      "tracks": 7,
      "backImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/2423/d4f88082-3c72-4a12-a158-7d82cdb64f73_640.jpg",
      "feature_lyrics": true,
      "releaseDate": "2016-04-25",
      "price": 5.99,
      "artistId": 1005,
      "owner": 1,
      "feature_aac": false,
      "feature_booklet": true,
      "cdImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/2423/e523429e-9301-436b-8994-bd8a739f6647_570.jpg",
      "artistName": "러블리즈",
      "genreName": "Teen Pop",
      "feature_adult": false,
      "jacketImage": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/album/2423/92b73ea3-a5c5-415e-a71b-fc05561d806d_360.jpg",
      "genreId": 3
    }
  ],
  "success": true
}

 */
class RequestArtistAlbumList(val artistId: Long,
                             val lang: String = ThisApp.LangCode,
                             val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "https://www.bainil.com/api/v2/artist/albums"
    }

    override fun composeURL(): String {
        var url = "${URL}?artistId=$artistId"
        url += "&lang=$lang"
        return url
    }

    override fun execute(): ArtistAlbumListWrapper {
        val artistAlbumListJsonStr = getHTTPResponseString()
        Log.v("ReqArtistAlbumList", artistAlbumListJsonStr)
        return gson.fromJson<ArtistAlbumListWrapper>(artistAlbumListJsonStr)
    }
}