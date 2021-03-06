package win.limerainne.vinylico.toolbox

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.credential.LoginCookie
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.data.api.Server
import win.limerainne.vinylico.domain.job.AnnotateWebDownloadIdCommand
import win.limerainne.vinylico.domain.model.TrackList
import java.io.File
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL
import java.net.URLDecoder

/**
 * Created by Limerainne on 2016-10-24.
 *
 * check Login (might not needed) -> check permission -> get filename -> request download (with cookies) -> write down request to app's queue (can show status / cancellable)
 *
 * refer to: http://stackoverflow.com/questions/23069965/get-file-name-from-headers-with-downloadmanager-in-android
 *
 * https://www.bainil.com/track/download?no=12657
 */
class DownloadTool(val url: String, val path: File, val title: String, val desc: String) {

    var filename: String = ""

    fun doDownload(mContext: Context)   {
        // check if 3G/LTE
        if (checkIfDataNetworkInSongDownload(mContext, true)) return

        if (checkIfPermission(mContext)) {
            getFilenameFromHeaderAsync(mContext) {
                // create path if nonexist
                path.mkdirs()

                addToQueue(mContext)
            }
        }
    }

    fun doDownload(mContext: Context, filename: String)   {
        if (checkIfPermission(mContext)) {
            this.filename = filename

            // create path if nonexist
            path.mkdirs()

            addToQueue(mContext)
        }
    }

    fun checkIfPermission(mContext: Context): Boolean {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext as Activity,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                    // Show an expanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//
//                } else {

            // No explanation needed, we can request the permission.
            mContext.toast(mContext.getString(R.string.msg_err_download_requires_permission))

            ActivityCompat.requestPermissions(mContext as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    ThisApp.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//                }

            return false    // permission denied
        }

        return true // permission granted
    }

    fun getFilenameFromHeaderAsync(context: Context, callback: () -> Unit) {
        doAsync(ThisApp.ExceptionHandler) {
            try {
                // http://stackoverflow.com/questions/16150089/how-to-handle-cookies-in-httpurlconnection-using-cookiemanager
                val filename_header = "Content-Disposition"
                val filesize_header = "Content-Length"
                val filetype_header = "Content-Type"

                val url = URL(url)

                val conn = url.openConnection() as HttpURLConnection
                conn.setReadTimeout(10000 /* milliseconds */)
                conn.setConnectTimeout(15000 /* milliseconds */)
                conn.setRequestMethod("HEAD")

                conn.useCaches = true

                val maxStale = 60 * 60 * 24 // for one day
                conn.addRequestProperty("Cache-Control", "max-stale=" + maxStale)

                // - cookie
                val loginCookie = LoginCookie(ThisApp.AppContext)
                conn.setRequestProperty("Cookie", loginCookie.getCookieStr())

                // - user agent
                conn.setRequestProperty("User-Agent", WebviewTool().getDefaultUserAgentString(ThisApp.AppContext))

                // - Accept-Language? determines filename
                conn.setRequestProperty("Accept-Language",
                if (!ThisApp.CommonPrefs.useEnglish)
                    "ko-KR,ko;q=0.8,en-US,en;q=0.3"
                else
                    "en-US,en;q=0.8,ko-KR,ko;q=0.3"
                )

                // - avoid quick succession error
                conn.setRequestProperty("Connection", "close")

                // - refefer
                conn.setRequestProperty("Referer", "https://www.bainil.com/")

                conn.setDoInput(true)
                // Starts the query
                conn.connect()

                val headers = conn.headerFields
                println(headers)
                val filenameHeader = headers[filename_header]

                val reFilename = Regex("""filename="(.+)"""")
                if (filenameHeader != null) {
                    val entry = filenameHeader.get(0)
                    filename = reFilename.find(entry)?.groupValues?.get(1) ?: ""
                    filename = URLDecoder.decode(filename, "UTF-8")
                }
//                println("Filename to download: ${filename}")

                if (filename.isNotEmpty())
                    uiThread {
                        callback()
                    }
                else
                    uiThread {
                        context.toast(context.getString(R.string.msg_err_cant_download_song))
                        throw Exception("${url}")
                    }
            }
            catch (e: Exception)    {
                e.printStackTrace()
                uiThread {
                    context.toast(context.getString(R.string.msg_err_cant_download_song))
                }
                throw e
            }
        }
    }

    fun addToQueue(context: Context)    {
        // check if 3G/LTE
        if (checkIfDataNetworkInSongDownload(context, true)) return

        doAsync(ThisApp.ExceptionHandler) {
            try {
                val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                val request = DownloadManager.Request(Uri.parse(url)).apply {
                    setTitle(title)
                    setDescription(filename)
                    setDestinationUri(Uri.fromFile(File(path, filename)))
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    allowScanningByMediaScanner()

                    // set HTTP header
                    val loginCookie = LoginCookie(ThisApp.AppContext)
                    addRequestHeader("Cookie", loginCookie.getCookieStr())
                    addRequestHeader("User-Agent", WebviewTool().getDefaultUserAgentString(ThisApp.AppContext))
                    addRequestHeader("Accept-Language",
                            if (!ThisApp.CommonPrefs.useEnglish)
                                "ko-KR,ko;q=0.8,en-US,en;q=0.3"
                            else
                                "en-US,en;q=0.8,ko-KR,ko;q=0.3"
                    )
                    addRequestHeader("Connection", "keep-alive")

                    // - refefer
                    addRequestHeader("Referer", "https://www.bainil.com/")

                    // if 3G/LTE allowed...
                    if (ThisApp.CommonPrefs.allowDataNetwork)
                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or
                            DownloadManager.Request.NETWORK_MOBILE)
                    else
                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)

                    // visible in Download app (?)
                    setVisibleInDownloadsUi(true)
                }

                manager.enqueue(request)
            } catch (e: Exception)  {
                e.printStackTrace()
                uiThread {
                    context.toast(context.getString(R.string.msg_err_cant_download_song))
                }
                throw e
            }
        }
    }

    companion object {
        private val TrackDownloadURLPrefix = "https://www.bainil.com/track/download?no="

        fun newInstance(url: String, path: File, title: String = "", desc: String = ""): DownloadTool {
            return DownloadTool(url, path, title, desc)
        }

        fun newInstance(trackId: Long, path: File, title: String, desc: String): DownloadTool {
            return newInstance(TrackDownloadURLPrefix + "${trackId}", path, title, desc)
        }

        fun newInstance(trackId: Long, songName: String): DownloadTool {
            return newInstance(trackId, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Bainil: ${songName}", "")
        }

        fun checkIfDataNetworkInSongDownload(context: Context, dismissWarning: Boolean = false, proceed: () -> Unit = {}): Boolean {
            return checkIfDataNetwork(context, "${context.getString(R.string.msg_err_cant_download_song)}\n${context.getString(R.string.msg_err_cellular_network)}", dismissWarning, proceed)
        }

        fun checkIfDataNetworkInAlbumDownload(context: Context, dismissWarning: Boolean = false, proceed: () -> Unit = {}): Boolean {
            return checkIfDataNetwork(context, "${context.getString(R.string.msg_err_cant_download_album)}\n${context.getString(R.string.msg_err_cellular_network)}", dismissWarning, proceed)
        }

        fun checkIfDataNetwork(context: Context, errorMsg: String, dismissWarning: Boolean = false, proceed: () -> Unit = {}): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connMgr.activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                if (!ThisApp.CommonPrefs.allowDataNetwork) {
                    context.toast(errorMsg)
                    return true
                }
                else if (!dismissWarning && ThisApp.CommonPrefs.askBeforeDownload)  {
                    // ask before download
                    AlertDialogBuilder(context).run {
                        title(context.getString(R.string.msg_warning_download_data_network_title))
                        message(context.getString(R.string.msg_warning_download_data_network_desc))
                        positiveButton(context.getString(android.R.string.ok), {proceed()})
                        negativeButton(context.getString(android.R.string.cancel), {})
                        show()
                    }
                }
                else
                    proceed()
            }   else    {
                proceed()
            }

            return false
        }

        fun downloadTrack(trackId: Long, albumTracks: TrackList, context: Context) {
            checkIfDataNetworkInSongDownload(context) {
                DownloadTool.downloadTrackImpl(trackId, albumTracks, context)
            }
        }
        fun downloadTrackImpl(trackId: Long, albumTracks: TrackList, context: Context) {
            if (albumTracks.downloadId == 0L)
                doAsync(ThisApp.ExceptionHandler) {
                    AnnotateWebDownloadIdCommand(albumTracks.albumId, albumTracks).execute {
                        uiThread {
                            DownloadTool.downloadTrackImpl(trackId, albumTracks, context)
                        }
                    }
                }
            else if (albumTracks.downloadId == -1L)
                context.toast("${context.getString(R.string.msg_err_cant_download_song)}\n${context.getString(R.string.msg_err_track_no_download_id)}")
            else
                for (track in albumTracks.tracks) {
                    if (track.songId == trackId) {
                        if (track.downloadId > 0L)
                            DownloadTool.newInstance(track.downloadId, track.songName).doDownload(context)
                        else
                            context.toast("${context.getString(R.string.msg_err_cant_download_song)}: ${track.songName}\n${context.getString(R.string.msg_err_track_no_download_id)}")

                        break
                    }
                }
        }

        fun downloadAlbum(albumId: Long, albumTracks: TrackList, context: Context) {
            checkIfDataNetworkInSongDownload(context) {
                DownloadTool.downloadAlbumImpl(albumId, albumTracks, context)
            }
        }
        fun downloadAlbumImpl(albumId: Long, albumTracks: TrackList, context: Context) {
            if (albumTracks.downloadId == 0L)
                doAsync(ThisApp.ExceptionHandler) {
                    AnnotateWebDownloadIdCommand(albumId, albumTracks).execute {
                        uiThread {
                            DownloadTool.downloadAlbumImpl(albumId, albumTracks, context)
                        }
                    }
                }
            else if (albumTracks.downloadId == -1L)
                context.toast("${context.getString(R.string.msg_err_cant_download_album)}\n${context.getString(R.string.msg_err_track_no_download_id)}")
            else
                for (track in albumTracks.tracks) {
                    if (track.downloadId > 0L)
                        DownloadTool.newInstance(track.downloadId, track.songName).doDownload(context)
                    else
                        context.toast("${context.getString(R.string.msg_err_cant_download_song)}: ${track.songName}\n${context.getString(R.string.msg_err_track_no_download_id)}")
                }
        }

        fun downloadAlbum(albumId: Long, context: Context) {
            checkIfDataNetworkInAlbumDownload(context) {

                doAsync(ThisApp.ExceptionHandler) {
                    try {
                        val albumTracks = Server().requestTrackList(albumId, UserInfo.getUserIdOr(context))
                        AnnotateWebDownloadIdCommand(albumId, albumTracks).execute {
                            uiThread {
                                for (track in albumTracks.tracks) {
                                    if (track.downloadId > 0L)
                                        DownloadTool.newInstance(track.downloadId, track.songName).doDownload(context)
                                    else
                                        context.toast("${context.getString(R.string.msg_err_cant_download_song)}: ${track.songName}\n${context.getString(R.string.msg_err_track_no_download_id)}")
                                }
                            }
                        }
                    } catch (e: Exception) {   // ProtocolException, IllegalStateException
                        uiThread {
                            context.toast("${context.getString(R.string.msg_err_cant_download_album)}\n${context.getString(R.string.msg_err_failed_to_retrieve_album_info)}")
                        }
                        throw e
                    }
                }
            }
        }

        fun addReceiverOnClick(context: Context)    {
            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val dm = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                    dm.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(dm)
                }
            }

//            context.registerReceiver(onComplete,
//                    IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            context.registerReceiver(onComplete,
                    IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED))
        }
    }
}