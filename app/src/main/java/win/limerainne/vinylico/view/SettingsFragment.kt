package win.limerainne.vinylico.view

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import win.limerainne.vinylico.BuildConfig
import win.limerainne.vinylico.R
import win.limerainne.vinylico.base.AdReqBuilder
import win.limerainne.vinylico.credential.UserInfo

/**
 * Created by Limerainne on 2016-10-21.
 *
 * http://sjava.net/2015/12/v7-support-preference-%eb%9d%bc%ec%9d%b4%eb%b8%8c%eb%9f%ac%eb%a6%ac-%ec%82%ac%ec%9a%a9%ed%95%98%ea%b8%b0/#respond
 * http://stackoverflow.com/questions/32070186/how-to-use-the-v7-v14-preference-support-library
 *
 * * show Admob:
 * http://stackoverflow.com/questions/4003701/how-do-i-put-an-admob-adview-in-the-settings-screen-for-a-live-wallpaper
 */
class SettingsFragment: PreferenceFragmentCompat(), HavingToolbar {

    val toolbar: Toolbar
        get() = view?.findViewById(R.id.toolbar) as Toolbar

    lateinit var settingsView: View
    lateinit var parentView: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsView = super.onCreateView(inflater, container, savedInstanceState) as View
        parentView = inflater!!.inflate(R.layout.fragment_main_ad, container, false)

        parentView.content_main.addView(settingsView)
        parentView.fab.visibility = View.GONE

        initToolbar()

        // add ad
        val mAdView: AdView = parentView.findViewById(R.id.adView) as AdView
        mAdView.loadAd(AdReqBuilder.getAdRequest())

        return parentView
    }

    override fun initToolbar()  {
        val toolbar = parentView.findViewById(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setTitle(R.string.nav_setting)
    }

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

        // set contents
        fun setSummaryAsItsString(key: String)   {
            val pref = findPreference(key)
            pref.summary = pref.sharedPreferences.getString(key, "")
        }
        fun setSummaryAsText(key: String, text: String) {
            findPreference(key).summary = text
        }
        setSummaryAsItsString("_userEmail")
        setSummaryAsItsString("_userURL")
        if (!UserInfo.checkLogin(context))
            setSummaryAsText("__is_auto_login", getString(R.string.setting_userinfo_autologin_summary))
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