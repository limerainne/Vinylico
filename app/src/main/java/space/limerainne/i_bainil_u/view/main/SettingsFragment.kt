package space.limerainne.i_bainil_u.view.main

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.view.MainActivity

/**
 * Created by Limerainne on 2016-10-21.
 *
 * http://sjava.net/2015/12/v7-support-preference-%eb%9d%bc%ec%9d%b4%eb%b8%8c%eb%9f%ac%eb%a6%ac-%ec%82%ac%ec%9a%a9%ed%95%98%ea%b8%b0/#respond
 * http://stackoverflow.com/questions/32070186/how-to-use-the-v7-v14-preference-support-library
 *
 * * show Admob:
 * http://stackoverflow.com/questions/4003701/how-do-i-put-an-admob-adview-in-the-settings-screen-for-a-live-wallpaper
 */
class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_settings)
    }

    override fun onResume() {
        super.onResume()

        if (activity is MainActivity) {
            (activity as MainActivity).unsetNavigationViewCheckedItem()
            (activity as MainActivity).setToolbarColor()
            //(activity as MainActivity).setToolbarColor(R.color.babyPink, R.color.babyPinkDark)
        }
    }


    companion object {
        val TAG = SettingsFragment::class.java.simpleName
        val NavMenuId = R.id.nav_setting

        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()

            return fragment
        }
    }
}