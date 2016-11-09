package win.limerainne.i_bainil_u.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.content_main.view.*
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.base.AdReqBuilder

/**
 * Created by Limerainne on 2016-11-07.
 */
class AboutFragment: Fragment(), HavingToolbar {

    val TargetLayout: Int = R.layout.fragment_about

    protected lateinit var fragView: View
    val toolbar: Toolbar
        get() = fragView.findViewById(R.id.toolbar) as Toolbar

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragView = inflater!!.inflate(R.layout.fragment_main_ad, container, false)

        val childView = inflater!!.inflate(TargetLayout, container, false)
        fragView.content_main.addView(childView)

        if (this is HavingToolbar)
            initToolbar()

        // add ad
        val mAdView: AdView = fragView.findViewById(R.id.adView) as AdView
        mAdView.loadAd(AdReqBuilder.getAdRequest())

        return fragView
    }

    override fun initToolbar() {
        attachToolbar(fragView)
    }

    fun attachToolbar(view: View) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    companion object {
        val TAG = AboutFragment::class.java.simpleName
        val NavMenuId = R.id.nav_about

        fun newInstance(): AboutFragment = AboutFragment()
    }
}