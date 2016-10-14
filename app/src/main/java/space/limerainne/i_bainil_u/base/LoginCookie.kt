package space.limerainne.i_bainil_u.base

import android.content.Context
import space.limerainne.i_bainil_u.extension.DelegatesExt

/**
 * Created by Limerainne on 2016-09-28.
 */
class LoginCookie(context: Context) {
    // TODO encrypt login cookie information!!!

    var AWSELB: String by DelegatesExt.preference(context, "AWSELB", "")
    var JSESSIONID: String by DelegatesExt.preference(context, "JSESSIONID", "")
    var auth_token: String by DelegatesExt.preference(context, "auth_token", "")
    var email: String by DelegatesExt.preference(context, "email", "")
    var remember: String by DelegatesExt.preference(context, "remember", "")

    var haveLoginCookie: Boolean by DelegatesExt.preference(context, "__have_login_cookie", true)
    var isAutoLogin: Boolean by DelegatesExt.preference(context, "__is_auto_login", false)

    fun parseLoginCookie(cookie: String) {
        // TODO get session token
        // TODO differentiate auto login/one-time login

        /* cookie string example
        # one-time login (AWSELB, JSESSIONID)
        country_code=KR; AWSELB=6B3FF9711C82CF8F3E824BE77A7EBFFD61B723DB4D3ADCBB7D9FCD63EFFC243ECA7240A80D05F62AA5C19791B6509C6705CB9AA1B86D36DD36D04A6252B5CB6893274FF0E4; _gat=1; JSESSIONID=77BB41CA86C324662A6BF05B3DF7F1E6; _ga=GA1.2.817871205.1474474291
        # autologin (AWSELB, JSESSIONID + auth_token, email, remember=Y
        country_code=KR; AWSELB=6B3FF9711C82CF8F3E824BE77A7EBFFD61B723DB4D3ADCBB7D9FCD63EFFC243ECA7240A80D05F62AA5C19791B6509C6705CB9AA1B86D36DD36D04A6252B5CB6893274FF0E4; JSESSIONID=20C88A11D12E796A948D0D0520BD466B; remember=Y; email="kjs2967@gmail.com"; auth_token=70ed644b9c71c495c818062002acca1f5d0c5ebe; _ga=GA1.2.817871205.1474474291
         */

        val cookieEntryRegex = Regex("""(.*?)=(.*?)(?:;|,(?!\s)|$)""")
        val cookieEntries = cookieEntryRegex.findAll(cookie)

        isAutoLogin = false // reset value

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

        haveLoginCookie = true
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

}