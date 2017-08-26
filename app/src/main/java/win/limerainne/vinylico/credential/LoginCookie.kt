package win.limerainne.vinylico.credential

import android.content.Context
import android.util.Log
import win.limerainne.vinylico.extension.DelegatesExt
import win.limerainne.vinylico.toolbox.WebviewTool
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Limerainne on 2016-09-28.
 */
class LoginCookie(val context: Context) {
    // TODO encrypt login cookie information!!!

    var AWSELB: String by DelegatesExt.preference(context, "AWSELB", "")
    var JSESSIONID: String by DelegatesExt.preference(context, "JSESSIONID", "")
    var auth_token: String by DelegatesExt.preference(context, "auth_token", "")
    var email: String by DelegatesExt.preference(context, "email", "")
    var remember: String by DelegatesExt.preference(context, "remember", "")

    var haveLoginCookie: Boolean by DelegatesExt.preference(context, "__have_login_cookie", true)
    var isAutoLogin: Boolean by DelegatesExt.preference(context, "__is_auto_login", false)

    private val cookieEntryRegex = Regex("""(.*?)=(.*?)(?:;|,(?!\s)|$)""")

    fun parseLoginCookie(cookie: String) {
        // TODO get session token: JSESSIONID
        // TODO differentiate auto login/one-time login

        /* cookie string example
        # one-time login (AWSELB, JSESSIONID)
        country_code=KR; AWSELB=blahblah; _gat=1; JSESSIONID=blahblah; _ga=GA1.2.blahblah
        # autologin (AWSELB, JSESSIONID + auth_token, email, remember=Y
        country_code=KR; AWSELB=blahblah; JSESSIONID=blahblah; remember=Y; email="hello@bainil.com"; auth_token=blahblah; _ga=GA1.2.blahblah
         */

        isAutoLogin = false // reset value

        parseLoginCookieEntry(cookie)

        haveLoginCookie = true
    }

    fun parseLoginCookieEntry(cookie: String)   {
        val cookieEntries = cookieEntryRegex.findAll(cookie)

        for (cookieEntry in cookieEntries) {
            val key = cookieEntry.groupValues[1]
            val value = cookieEntry.groupValues[2]

            when  {
                key.contains("AWSELB") -> AWSELB = value
                key.contains("JSESSIONID") -> JSESSIONID = value
                key.contains("auth_token") -> {
                    isAutoLogin = true
                    auth_token = value
                }
                key.contains("email") -> email = value
                key.contains("remember") -> remember = value
            }
        }
    }

    fun clearCookie()  {
        AWSELB = ""
        JSESSIONID = ""
        auth_token = ""
        email = ""
        remember = ""

        haveLoginCookie = false
        isAutoLogin = false
    }

    fun clearCookieWithoutAutoLogin()  {
        AWSELB = ""
        JSESSIONID = ""
        // auth_token = ""
        // email = ""
        // remember = ""

        haveLoginCookie = false
    }

    override fun toString(): String {
        return "AWSELB: " + AWSELB + ", " + "JSESSIONID: " + JSESSIONID + ", " +
            "Auto-Login? " + isAutoLogin
    }

    fun getCookieStr(addSessionHeader: Boolean = true): String {
        var cookieStr = ""
        cookieStr += "country_code=KR; "
        if (addSessionHeader) {
            cookieStr += "JSESSIONID=${JSESSIONID}; "
            cookieStr += "AWSELB=${AWSELB}; "
        }
        if (isAutoLogin) {
            cookieStr += "auth_token=${auth_token}; "
            cookieStr += "email=${email}; "
            cookieStr += "remember=${remember}; "
        }
        return cookieStr
    }

    fun getToken()  {
        // http://stackoverflow.com/questions/16150089/how-to-handle-cookies-in-httpurlconnection-using-cookiemanager
        val init_url = "https://www.bainil.com/"
        val cookies_header = "Set-Cookie";

        val url = URL(init_url)

        val conn = url.openConnection() as HttpURLConnection
        conn.setReadTimeout(10000 /* milliseconds */)
        conn.setConnectTimeout(15000 /* milliseconds */)
        conn.setRequestMethod("GET")

        // append properties
        // - auto login cookie
        if (isAutoLogin) {
            conn.setRequestProperty("Cookie", getCookieStr(addSessionHeader = false))
        }
        // - user agent
        conn.setRequestProperty("User-Agent", WebviewTool().getDefaultUserAgentString(context))

        conn.setDoInput(true)
        // Starts the query
        conn.connect()

        val headers = conn.headerFields
        val cookies = headers[cookies_header]

        if (cookies != null)    {
//            Log.v("LoginCookie", "Cookie: ${cookies}")

            for (cookie in cookies) {
                parseLoginCookieEntry(cookie)
            }

            haveLoginCookie = true
        }   else    {
            Log.d("LoginCookie", "Can't retrieve cookie!")
        }
    }

}