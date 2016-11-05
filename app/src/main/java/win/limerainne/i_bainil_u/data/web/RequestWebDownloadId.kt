package win.limerainne.i_bainil_u.data.web

import win.limerainne.i_bainil_u.data.api.request.data.RequestHTTPConnection

/**
 * Created by CottonCandy on 2016-10-30.
 */
class RequestWebDownloadId(val albumId: Long): RequestHTTPConnection() {
    val reAlbumId = Regex("""album/download\?no=(\d+)""")
    val reTrackId = Regex("""data-song="(\d+)"(?:.|\n)+?href="/track/download\?no=(\d+)""")

    override fun composeURL(): String = "http://www.bainil.com/album/${albumId}"

    override fun execute(): WebDownloadId {
        println("RequestWebDownloadId.execute()")

        val response = getHTTPResponseString()

        // get albumId
        val albumId: Long = reAlbumId.find(response)?.let {
            it.groupValues[1].toLong()
        } ?: 0L

        println(albumId)

        val trackIdPairList: Map<Long, Long> = reTrackId.findAll(response).let {
            println(it)

            val list = mutableMapOf<Long, Long>()

            for (match in it)   {
                println(match)
                println(match.groupValues)
                val trackId = match.groupValues[1].toLong()
                val downloadId = match.groupValues[2].toLong()
                list.put(trackId, downloadId)
                println("${trackId}, ${downloadId}")
            }

            list
        }

        println(trackIdPairList)

        println("${albumId}\n${trackIdPairList}")

        return WebDownloadId(albumId, trackIdPairList)
    }
}