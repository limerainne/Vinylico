package space.limerainne.i_bainil_u.toolbox

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Created by CottonCandy on 2016-10-03.
 */
class BainilLauncher {
    companion object    {
        fun executeBainilApp(context: Context)  {
            val pkgName = "com.bainil.app"

            try {
                val existPackage = context.packageManager.getLaunchIntentForPackage(pkgName)
                if (existPackage != null) {
                    with (existPackage) {
                        addCategory(Intent.CATEGORY_LAUNCHER)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(existPackage)
                } else {
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse("market://details?id=" + pkgName)
                    context.startActivity(marketIntent)
                }
            } catch (e: Exception) {
                // just ignore error
                e.printStackTrace()
            }
        }

        fun executeBainilAppAlbumScreen(context: Context, albumId: Long) {
            // http://apogenes.tistory.com/4
            // { act=android.intent.action.VIEW dat=bainilapp://?type=A&code=2423 pkg=com.bainil.app }

            val url = """intent://?type=A&code=${albumId}#Intent;scheme=bainilapp;package=com.bainil.app;end;"""

            try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)

                val existPackage = context.packageManager.getLaunchIntentForPackage(intent.`package`)
                if (existPackage != null) {
                    context.startActivity(intent)
                } else {
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse("market://details?id=" + intent.`package`)
                    context.startActivity(marketIntent)
                }
            } catch (e: Exception) {
                // just ignore error
                e.printStackTrace()
            }
        }
    }
}