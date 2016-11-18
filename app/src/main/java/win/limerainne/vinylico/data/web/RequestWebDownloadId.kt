package win.limerainne.vinylico.data.web

import win.limerainne.vinylico.data.api.request.data.RequestHTTPConnection

/**
 * Created by CottonCandy on 2016-10-30.
 */
class RequestWebDownloadId(val albumId: Long): RequestHTTPConnection() {
    val reAlbumId = Regex("""album/download\?no=(\d+)""")
    val reTrackId = Regex("""data-song="(\d+)"(?:.|\n)+?href="/track/download\?no=(\d+)""")

    override fun composeURL(): String = "http://www.bainil.com/album/${albumId}"

    override fun execute(): WebDownloadId {
//        println("RequestWebDownloadId.execute()")

        val response = getHTTPResponseString()

        // get albumId
        val albumId: Long = reAlbumId.find(response)?.let {
            it.groupValues[1].toLong()
        } ?: 0L

//        println(albumId)

        val trackIdPairList: Map<Long, Long> = reTrackId.findAll(response).let {

            val list = mutableMapOf<Long, Long>()

            for (match in it)   {
                val trackId = match.groupValues[1].toLong()
                val downloadId = match.groupValues[2].toLong()
                list.put(trackId, downloadId)
            }

            list
        }

//        println("${albumId}\n${trackIdPairList}")

        return WebDownloadId(albumId, trackIdPairList)
    }
}