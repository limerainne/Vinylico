package space.limerainne.i_bainil_u

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import com.tsengvn.typekit.Typekit

/**
 * Created by Limerainne on 2016-06-23.
 */
class I_Bainil_UApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Typekit.getInstance().addNormal(Typeface.DEFAULT).addBold(Typeface.DEFAULT_BOLD)
    }

    companion object {
        // val USER_ID: Long = 2543
        val USER_ID: Long = 2    // might be empty...?

        val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 0

        operator fun get(context: Context): I_Bainil_UApp {
            return context.applicationContext as I_Bainil_UApp
        }
    }
}