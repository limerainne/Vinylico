package win.limerainne.i_bainil_u.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.data.api.EventResponse
import win.limerainne.i_bainil_u.data.api.request.Request

/**
 * Created by CottonCandy on 2016-10-04.
 */

/*
- Request URL
http://www.bainil.com/api/v2/connected/events?userId=2&lang=en
- Response example
{"total":1,"events":[{"bannerImage":"http://cloud.bainil.com/upload/event/18/4f1eb4ba-2486-463c-ab90-522e57aae918_event.png","seq":18,"eventUrl":"http://www.bainil.com/app/v2/event/18","eventName":"주간 뮤직 스나이퍼 9월 4주차"}],"success":true}
 */

class RequestEventBanner(val userId: Long,
                         val lang: String = ThisApp.LangCode,
                         val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "http://www.bainil.com/api/v2/connected/events"
    }

    override fun composeURL(): String {
        var url = "${URL}?userId=$userId"
        url += "&lang=$lang"
        return url
    }

    override fun execute(): EventResponse {
        val EventBannerResponseStr = getHTTPResponseString()
        return gson.fromJson<EventResponse>(EventBannerResponseStr)
    }
}