package win.limerainne.i_bainil_u.viewmodel.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.data.api.request.data.RequestStoreAlbums
import win.limerainne.i_bainil_u.view.main.BrowseListFragment

/**
 * Created by CottonCandy on 2016-09-17.
 */
class BrowseFragmentAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    lateinit var currentFragment: Fragment
        private set

    override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any?) {
        if (`object` is Fragment)
            currentFragment = `object`
        super.setPrimaryItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment? {
        // TODO can dynamically initialize
        when (position) {
            0 -> return BrowseListFragment.newInstance(1, RequestStoreAlbums.CATEGORY_NEW)
            1 -> return BrowseListFragment.newInstance(1, RequestStoreAlbums.CATEGORY_FEATURED)
            2 -> return BrowseListFragment.newInstance(1, RequestStoreAlbums.CATEGORY_TOP)
            3 -> return BrowseListFragment.newInstance(1, RequestStoreAlbums.CATEGORY_XSFM)
            else -> return null
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return ThisApp.AppContext.getString(R.string.browse_new)
            1 -> return ThisApp.AppContext.getString(R.string.browse_featured)
            2 -> return ThisApp.AppContext.getString(R.string.browse_top)
            3 -> return ThisApp.AppContext.getString(R.string.browse_xsfm)
        }

        return super.getPageTitle(position)
    }
}