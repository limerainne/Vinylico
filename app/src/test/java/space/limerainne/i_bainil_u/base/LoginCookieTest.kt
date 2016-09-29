package space.limerainne.i_bainil_u.data.api

import org.junit.Test
import org.junit.Assert.*
import space.limerainne.i_bainil_u.base.LoginCookie

import space.limerainne.i_bainil_u.domain.model.Wishlist as DomainWishlist
import space.limerainne.i_bainil_u.domain.model.AlbumEntry as DomainWishAlbum

/**
 * Created by Limerainne on 2016-07-21.
 */

class LoginCookieTest {

    companion object    {
        val COOKIE: String = """
        country_code=KR; AWSELB=6B3FF9711C82CF8F3E824BE77A7EBFFD61B723DB4D3ADCBB7D9FCD63EFFC243ECA7240A80D05F62AA5C19791B6509C6705CB9AA1B86D36DD36D04A6252B5CB6893274FF0E4; _gat=1; JSESSIONID=77BB41CA86C324662A6BF05B3DF7F1E6; _ga=GA1.2.817871205.1474474291
        """
        val COOKIE_AUTO: String = """
        country_code=KR; AWSELB=6B3FF9711C82CF8F3E824BE77A7EBFFD61B723DB4D3ADCBB7D9FCD63EFFC243ECA7240A80D05F62AA5C19791B6509C6705CB9AA1B86D36DD36D04A6252B5CB6893274FF0E4; JSESSIONID=20C88A11D12E796A948D0D0520BD466B; remember=Y; email="kjs2967@gmail.com"; auth_token=70ed644b9c71c495c818062002acca1f5d0c5ebe; _ga=GA1.2.817871205.1474474291
        """
    }

//    @Test
//    fun testCanParseCookie()    {
//        val c: LoginCookie = LoginCookie()
//        c.parseLoginCookie(COOKIE)
//
//        println(c)
//    }
//
//    @Test
//    fun testCanParseCookieAutoLogin()    {
//        val c: LoginCookie = LoginCookie()
//        c.parseLoginCookie(COOKIE_AUTO)
//
//        println(c)
//    }
}