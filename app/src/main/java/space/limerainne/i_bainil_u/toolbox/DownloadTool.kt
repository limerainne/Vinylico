package space.limerainne.i_bainil_u.toolbox

import android.util.Log
import org.jetbrains.anko.doAsync
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.credential.LoginCookie
import space.limerainne.i_bainil_u.data.api.Track
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder

/**
 * Created by Limerainne on 2016-10-24.
 *
 * refer to: http://stackoverflow.com/questions/23069965/get-file-name-from-headers-with-downloadmanager-in-android
 *
 * http://www.bainil.com/track/download?no=12657
 */
class DownloadTool(val url: String, val path: String) {

    var filename: String = ""

    fun getFilenameFromHeaderAsync() {
        doAsync {
            try {
                // http://stackoverflow.com/questions/16150089/how-to-handle-cookies-in-httpurlconnection-using-cookiemanager
                val filename_header = "Content-Disposition"
                val filesize_header = "Content-Length"
                val filetype_header = "Content-Type"

                val header_host = "www.bainil.com"

                val url = URL(url)

                val conn = url.openConnection() as HttpURLConnection
                conn.setReadTimeout(10000 /* milliseconds */)
                conn.setConnectTimeout(15000 /* milliseconds */)
                conn.setRequestMethod("HEAD")

                // - cookie
                val loginCookie = LoginCookie(I_Bainil_UApp.AppContext)
                conn.setRequestProperty("Cookie", loginCookie.getCookieStr())

                // - user agent
                conn.setRequestProperty("User-Agent", WebviewTool().getDefaultUserAgentString(I_Bainil_UApp.AppContext))

                // - host
                conn.setRequestProperty("Host", header_host)

                // - refefer TODO

                conn.setDoInput(true)
                // Starts the query
                conn.connect()

                val headers = conn.headerFields
                println(headers)
                val filenameHeader = headers[filename_header]

                val reFilename = Regex("""filename=\"(.+)\"""")
                if (filenameHeader != null) {
                    val entry = filenameHeader.get(0)
                    filename = reFilename.find(entry)?.groupValues?.get(1) ?: ""
                }
                println("Filename: ${URLDecoder.decode(filename, "UTF-8")}")
            }
            catch (e: Exception)    {
                e.printStackTrace()
            }
        }
    }

    fun addToQueue()    {

    }

    companion object    {
        private val TrackDownloadURLPrefix = "http://www.bainil.com/track/download?no="

        fun newInstance(url: String, path: String): DownloadTool    {
            return DownloadTool(url, path)
        }

        fun newInstance(trackId: Long, path: String): DownloadTool    {
            return newInstance(TrackDownloadURLPrefix + "${trackId}", path)
        }
    }
}