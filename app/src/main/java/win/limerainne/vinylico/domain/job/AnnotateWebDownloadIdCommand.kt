package win.limerainne.vinylico.domain.job

import android.widget.Toast
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.data.web.RequestWebDownloadId
import win.limerainne.vinylico.domain.commands.Command
import win.limerainne.vinylico.domain.model.TrackList

/**
 * Created by CottonCandy on 2016-10-30.
 */
class AnnotateWebDownloadIdCommand(val albumId: Long, val trackList: TrackList): Command<Unit> {
    override fun execute()  {
        execute {  }
    }

    fun execute(callback: () -> Unit) {
        doAsync(ThisApp.ExceptionHandler) {
                var toast: Toast? = null

                uiThread {
                    toast = Toast.makeText(ThisApp.AppContext, R.string.msg_web_download_id_annotating, Toast.LENGTH_SHORT).apply { show() }
                }
                trackList.downloadId = -1L

                val webIdPair = RequestWebDownloadId(albumId).execute()

                // albumId
                if (webIdPair.albumId > 0L)
                    trackList.downloadId = webIdPair.albumId

                // for each track
                for (track in trackList.tracks) {
                    track.downloadId = webIdPair.tracksIdPair[track.songId] ?: -1L
                }

                uiThread {
//                ThisApp.AppContext.toast(ThisApp.AppContext.getString(R.string.msg_web_download_id_annotated))
                    toast?.cancel()
                }

                callback()
            }
    }
}