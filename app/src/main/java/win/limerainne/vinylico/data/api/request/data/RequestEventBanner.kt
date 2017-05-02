package win.limerainne.vinylico.data.api.request.data

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.data.api.EventResponse
import win.limerainne.vinylico.data.api.request.Request

/**
 * Created by CottonCandy on 2016-10-04.
 */

/*
- Request URL
https://www.bainil.com/api/v2/connected/events?userId=2&lang=en
- Response example
{"total":1,"events":[{"bannerImage":"http://cloud.bainil.com/upload/event/18/4f1eb4ba-2486-463c-ab90-522e57aae918_event.png","seq":18,"eventUrl":"https://www.bainil.com/app/v2/event/18","eventName":"주간 뮤직 스나이퍼 9월 4주차"}],"success":true}
 */

class RequestEventBanner(val userId: Long,
                         val lang: String = ThisApp.LangCode,
                         val gson: Gson = Gson()) : RequestHTTPConnection() {

    companion object    {
        private val URL = "https://www.bainil.com/api/v2/connected/events"
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