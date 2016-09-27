package space.limerainne.i_bainil_u.base

import android.util.Log
import space.limerainne.i_bainil_u.view.LoginWebviewFragment

/**
 * Created by Limerainne on 2016-09-28.
 */
class Cookie {
    val loginInfo: MutableMap<String, String> = mutableMapOf()

    fun parseLoginCookie(cookie: String) {
//        Log.v(LoginWebviewFragment.TAG, "parseLoginCookie: " + cookie)
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

        loginInfo["_IS_AUTO_LOGIN"] = "0"

        for (cookieEntry in cookieEntries) {
            val key = cookieEntry.groupValues[1]
            val value = cookieEntry.groupValues[2]

            when  {
                key.contains("AWSELB") -> loginInfo["AWSELB"] = value
                key.contains("JSESSIONID") -> loginInfo["JSESSIONID"] = value
                key.contains("auth_token") -> {
                    loginInfo["_IS_AUTO_LOGIN"] = "1"
                    loginInfo["auth_token"] = value
                }
                key.contains("email") -> loginInfo["email"] = value
                key.contains("remember") -> loginInfo["remember"] = value
            }
        }
    }
}