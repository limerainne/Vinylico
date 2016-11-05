package space.limerainne.i_bainil_u

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import com.tsengvn.typekit.Typekit
import space.limerainne.i_bainil_u.base.CommonPrefs
import space.limerainne.i_bainil_u.credential.UserInfo
import space.limerainne.i_bainil_u.extension.DelegatesExt
import space.limerainne.i_bainil_u.extension.Preference

/**
 * Created by Limerainne on 2016-06-23.
 */
class I_Bainil_UApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Typekit.getInstance().addNormal(Typeface.DEFAULT).addBold(Typeface.DEFAULT_BOLD)

        CURRENT_USER_ID = UserInfo.getUserIdOr(applicationContext)

        AppContext = applicationContext
        CommonPrefs = CommonPrefs(AppContext)
        AppName = getString(R.string.app_name)
    }

    fun updateUserId(id: Long)  {
        CURRENT_USER_ID = id
    }

    companion object {
        // val USER_ID: Long = 2543
        val USER_ID: Long = 2    // might be empty...?

        val COOKIE_URL = "www.bainil.com"

        val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 0

        operator fun get(context: Context): I_Bainil_UApp {
            return context.applicationContext as I_Bainil_UApp
        }

        var CURRENT_USER_ID = USER_ID

        lateinit var AppContext: Context
        lateinit var CommonPrefs: CommonPrefs
        lateinit var AppName: String

        val LangCode: String
            get() = if (CommonPrefs.useEnglish) "en" else "ko"
    }
}