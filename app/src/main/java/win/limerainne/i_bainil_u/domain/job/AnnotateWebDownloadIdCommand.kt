package win.limerainne.i_bainil_u.domain.job

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.data.web.RequestWebDownloadId
import win.limerainne.i_bainil_u.domain.commands.Command
import win.limerainne.i_bainil_u.domain.model.TrackList

/**
 * Created by CottonCandy on 2016-10-30.
 */
class AnnotateWebDownloadIdCommand(val albumId: Long, val trackList: TrackList): Command<Unit> {
    override fun execute()  {
        execute {  }
    }

    fun execute(callback: () -> Unit) {
        doAsync {
            uiThread {
                ThisApp.AppContext.toast(ThisApp.AppContext.getString(R.string.msg_web_download_id_annotating))
            }

            val webIdPair = RequestWebDownloadId(albumId).execute()

            // albumId
            if (webIdPair.albumId != 0L)
                trackList.downloadId = webIdPair.albumId

            // for each track
            for (track in trackList.tracks) {
                track.downloadId = webIdPair.tracksIdPair[track.songId] ?: 0L
            }

            uiThread {
                ThisApp.AppContext.toast(ThisApp.AppContext.getString(R.string.msg_web_download_id_annotated))
            }

            callback()
        }
    }
}