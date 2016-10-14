package space.limerainne.i_bainil_u.data.api.request.data

import space.limerainne.i_bainil_u.data.api.request.Request
import java.io.*
import java.net.HttpURLConnection

/**
 * Created by Limerainne on 2016-07-21.
 */
abstract class RequestHTTPConnection() : Request {

    protected abstract fun composeURL(): String

    fun getHTTPResponseString(): String {
        println(composeURL())

        val url = java.net.URL(composeURL())
        val conn = url.openConnection() as HttpURLConnection
        conn.setReadTimeout(10000 /* milliseconds */)
        conn.setConnectTimeout(15000 /* milliseconds */)
        conn.setRequestMethod("GET")
        conn.setDoInput(true)
        // Starts the query
        conn.connect()
        val response = conn.getResponseCode()
        val i_s = conn.getInputStream()

        // Convert the InputStream into a string
        val contentAsString = readIt(i_s)

//        println(contentAsString)

        return contentAsString
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