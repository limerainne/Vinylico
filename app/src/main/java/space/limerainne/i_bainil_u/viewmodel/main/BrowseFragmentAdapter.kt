package space.limerainne.i_bainil_u.viewmodel.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import space.limerainne.i_bainil_u.data.api.RequestStoreAlbums
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
            0 -> return "New"
            1 -> return "Featured"
            2 -> return "Top"
            3 -> return "XSFM"
        }

        return super.getPageTitle(position)
    }
}