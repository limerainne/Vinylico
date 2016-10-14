package space.limerainne.i_bainil_u.data.api.request.data

/**
 * Created by Limerainne on 2016-10-14.
 */
class RequestTrackLyric(val lyricURL: String): RequestHTTPConnection() {
    override fun composeURL(): String = lyricURL

    override fun execute(): String {
        val lyric = getHTTPResponseString()

        // TODO tidy lyric text e.g. remove timestamp
        val lyric_tidy = re_lyric_timestamp.replace(lyric, "")

        return lyric_tidy
    }

    companion object    {
        val re_lyric_timestamp = Regex("""\[\d{2}:\d{2}[\.:]\d{2}\]""")
    }

}