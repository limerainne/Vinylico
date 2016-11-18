package win.limerainne.vinylico.data.web

/**
 * Created by CottonCandy on 2016-10-30.
 */

data class WebDownloadId(
            val albumId: Long,
            val tracksIdPair: Map<Long, Long>
)
