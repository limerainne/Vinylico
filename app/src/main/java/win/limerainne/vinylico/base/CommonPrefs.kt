package win.limerainne.vinylico.base

import android.content.Context
import win.limerainne.vinylico.extension.DelegatesExt

/**
 * Created by Limerainne on 2016-11-03.
 */
class CommonPrefs(appContext: Context) {

    private val prefCabinet = appContext.packageName + "_preferences"

    val showMenu: Boolean by DelegatesExt.preference(appContext, "pref_view_open_navigation_on_start", true, prefCabinet)

    val decorateEnabled: Boolean by DelegatesExt.preference(appContext, "pref_view_remove_soundtrack_parenthesis", true, prefCabinet)
    val useEnglish: Boolean by DelegatesExt.preference(appContext, "pref_view_use_english", false, prefCabinet)

    val allowDataNetwork: Boolean by DelegatesExt.preference(appContext, "pref_download_allow_data_network", false, prefCabinet)
    val askBeforeDownload: Boolean by DelegatesExt.preference(appContext, "pref_download_ask_before_download", true, prefCabinet)
}