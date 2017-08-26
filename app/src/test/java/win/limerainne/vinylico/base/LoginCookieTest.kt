package win.limerainne.vinylico.data.api

import win.limerainne.vinylico.domain.model.AlbumEntry as DomainWishAlbum
import win.limerainne.vinylico.domain.model.Wishlist as DomainWishlist

/**
 * Created by Limerainne on 2016-07-21.
 */

class LoginCookieTest {

    companion object    {
        val COOKIE: String = """
        country_code=KR; AWSELB=blahblah; _gat=1; JSESSIONID=blahblah; _ga=GA1.2.blahblah
        """
        val COOKIE_AUTO: String = """
        country_code=KR; AWSELB=blahblah; JSESSIONID=blahblah; remember=Y; email="hello@bainil.com"; auth_token=blahblah; _ga=GA1.2.blahblah
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