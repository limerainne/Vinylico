package win.limerainne.vinylico.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.data.api.AlbumDetail
import win.limerainne.vinylico.data.api.ArtistDetail
import win.limerainne.vinylico.data.api.ArtistDetailWrapper
import win.limerainne.vinylico.data.api.request.Request

/**
 * Created by Limerainne on 2016-07-21.
 */
/* @17.09.17
 - http://www.bainil.com/api/v2/artist?artistId=1005&lang=ko

{
  "result": {
    "countryName": "South Korea",
    "fans": 26,
    "artistEnglish": "Lovelyz",
    "countryNo": 112,
    "labelId": 5043,
    "homepage": "http://www.lvlz8.com/main_teaser_bi11_0831.html#5",
    "twitter": "https://twitter.com/Official_LVLZ",
    "artistPicture": "https://d3ttbrlvlwtn7f.cloudfront.net/upload/artist/1005/6851383d-45d7-4771-b300-18d4d5e9bc30_360.jpg",
    "artistId": 1005,
    "facebook": "https://www.facebook.com/lvlz8",
    "artistDescEnglish": "Eight-member girl group Lovelyz made their debut in November 2014 with their official album <Girls’ Invasion>. Like the name of the group, Lovelyz have a  lovely appearance and music, which successfully drew many people’s attention. They have performed actively and proved to be a No.1 girl group of the next generation by being voted as the best new female idol.   ",
    "youtube": "https://www.youtube.com/user/lvlz8",
    "labelName": "울림 엔터테인먼트",
    "artistDesc": "러블리즈 는 8인조 걸그룹으로 2014년 11월 정규앨범《 Girls' Invasion 》으로 데뷔했다. 러블리즈는 2015년 최고의 신인 여자 아이돌 설문조사에서 1위를 차지하며 차세대 청순 걸그룹 대표주자임을 증명하며, 활발할 활동을 펼치고 있다.",
    "artistName": "러블리즈"
  },
  "success": true
}
 */
class RequestArtistDetail(val artistId: Long,
                          val lang: String = ThisApp.LangCode,
                          val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "https://www.bainil.com/api/v2/artist"
    }

    override fun composeURL(): String {
        var url = "${URL}?artistId=$artistId"
        url += "&lang=$lang"
        return url
    }

    override fun execute(): ArtistDetailWrapper {
        val artistDetailJsonStr = getHTTPResponseString()
        return gson.fromJson<ArtistDetailWrapper>(artistDetailJsonStr)
    }
}