package space.limerainne.i_bainil_u.domain.job

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.data.web.RequestWebDownloadId
import space.limerainne.i_bainil_u.domain.commands.Command
import space.limerainne.i_bainil_u.domain.model.TrackList

/**
 * Created by CottonCandy on 2016-10-30.
 */
class AnnotateWebDownloadIdCommand(val albumId: Long, val trackList: TrackList): Command<Unit> {
    override fun execute()  {
        execute {  }
    }

    fun execute(callback: () -> Unit) {
        doAsync {
            val webIdPair = RequestWebDownloadId(albumId).execute()

            // albumId
            if (webIdPair.albumId != 0L)
                trackList.downloadId = webIdPair.albumId

            // for each track
            for (track in trackList.tracks) {
                track.downloadId = webIdPair.tracksIdPair[track.songId] ?: 0L
            }

            uiThread {
                I_Bainil_UApp.AppContext.toast("Web download ID annotated!")
            }

            callback()
        }
    }
}