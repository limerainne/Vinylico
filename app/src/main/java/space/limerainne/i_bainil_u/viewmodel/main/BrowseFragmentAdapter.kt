package space.limerainne.i_bainil_u.viewmodel.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.data.api.request.data.RequestStoreAlbums
import space.limerainne.i_bainil_u.view.main.BrowseListFragment

/**
 * Created by CottonCandy on 2016-09-17.
 */
class BrowseFragmentAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
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
            0 -> return I_Bainil_UApp.AppContext.getString(R.string.browse_top)
            1 -> return I_Bainil_UApp.AppContext.getString(R.string.browse_featured)
            2 -> return I_Bainil_UApp.AppContext.getString(R.string.browse_new)
            3 -> return I_Bainil_UApp.AppContext.getString(R.string.browse_xsfm)
        }

        return super.getPageTitle(position)
    }
}