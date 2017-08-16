package win.limerainne.vinylico

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import com.tsengvn.typekit.Typekit
import win.limerainne.vinylico.base.CommonPrefs
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.toolbox.DownloadTool
import java.io.File

/**
 * Created by Limerainne on 2016-06-23.
 */
class ThisApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Typekit.getInstance().addNormal(Typeface.DEFAULT).addBold(Typeface.DEFAULT_BOLD)

        CURRENT_USER_ID = UserInfo.getUserIdOr(applicationContext)

        AppContext = applicationContext
        CommonPrefs = CommonPrefs(AppContext)
        AppName = getString(R.string.app_name)

        // TODO XXX could cause high memory usage
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        DownloadTool.addReceiverOnClick(AppContext)

        activateHttpResponseCache()
    }

    fun updateUserId(id: Long)  {
        CURRENT_USER_ID = id
    }

    fun activateHttpResponseCache() {
        try {
            val httpCacheSize = 10 * 1024 * 1024   // 10 MiB
            val httpCacheDir: File = File(cacheDir, "http")

            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File::class.java, Long::class.java)
                    .invoke(null, httpCacheDir, httpCacheSize)
        } catch (httpResponseCacheNotAvailable: Exception)  {
            Log.d(AppName, "HTTP response cache did not initialized")
        }
    }

    companion object {
        // val USER_ID: Long = 2543
        val USER_ID: Long = 2    // might be empty...?

        val COOKIE_URL: String
            get() = AppContext.getString(R.string.url_cookie)

        val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 0

        operator fun get(context: Context): ThisApp {
            return context.applicationContext as ThisApp
        }

        var CURRENT_USER_ID = USER_ID

        lateinit var AppContext: Context
        lateinit var CommonPrefs: CommonPrefs
        lateinit var AppName: String

        val LangCode: String
            get() = if (CommonPrefs.useEnglish) "en" else "ko"

        val ExceptionHandler = { e: Throwable ->
            e.printStackTrace()
            FirebaseCrash.report(e)
        }
    }
}