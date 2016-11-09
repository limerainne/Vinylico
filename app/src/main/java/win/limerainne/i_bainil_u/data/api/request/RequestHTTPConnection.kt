package win.limerainne.i_bainil_u.data.api.request.data

import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.credential.LoginCookie
import win.limerainne.i_bainil_u.data.api.request.Request
import win.limerainne.i_bainil_u.toolbox.WebviewTool
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Limerainne on 2016-07-21.
 */
abstract class RequestHTTPConnection() : Request {

    protected abstract fun composeURL(): String

    fun getHTTPResponseString(appendPreferLang: Boolean = true): String {
        try {
//            println(composeURL())

            val url = URL(composeURL())
            val conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 10000 /* milliseconds */
            conn.connectTimeout = 15000 /* milliseconds */
            conn.requestMethod = "GET"
            conn.doInput = true

            // append cookie
            // - cookie
            val loginCookie = LoginCookie(ThisApp.AppContext)
            conn.setRequestProperty("Cookie", loginCookie.getCookieStr())

            // - user agent
            conn.setRequestProperty("User-Agent", WebviewTool().getDefaultUserAgentString(ThisApp.AppContext))

            // XXX host is not required! it could cause HTTP 403 if differs...
//            // - host
//            conn.setRequestProperty("Host", header_host)

            // - Accept-Language? determines filename
            if (appendPreferLang) {
                conn.setRequestProperty("Accept-Language",
                        if (!ThisApp.CommonPrefs.useEnglish)
                            "ko-KR,ko;q=0.8,en-US,en;q=0.3"
                        else
                            "en-US,en;q=0.8,ko-KR,ko;q=0.3"
                )
            }

            // Connection: close
            conn.setRequestProperty("Connection", "close")

            // Starts the query
            conn.connect()
            val response = conn.getResponseCode()
//            println(response)
            val i_s = conn.getInputStream()

            // Convert the InputStream into a string
            val contentAsString = readIt(i_s)

//            println(contentAsString)

            return contentAsString
        } catch (e: Exception)  {
            e.printStackTrace()
            return ""
        }
    }

    // Reads an InputStream and converts it to a String.
    @Throws(IOException::class, UnsupportedEncodingException::class)
    fun readIt(stream: InputStream): String {
        var reader: Reader? = null
        reader = InputStreamReader(stream, "UTF-8")

        val bufReader = BufferedReader(reader)

        var res = ""
        while (true) {
            val buf = bufReader.readLine()
            if (buf == null)
                break

            //res += URLDecoder.decode(buf, "utf-8");
            res += buf + "\n"
        }

        return res
    }
}