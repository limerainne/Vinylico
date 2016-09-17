package space.limerainne.i_bainil_u.viewmodel

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import space.limerainne.i_bainil_u.data.api.RequestStoreAlbums
import space.limerainne.i_bainil_u.view.BrowseListFragment

/**
 * Created by CottonCandy on 2016-09-17.
 */
class BrowseFragmentAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment? {
        // TODO can dynamically initialize
        when (position) {
            0 -> return BrowseListFragment.newInstance(1, RequestStoreAlbums.CATEGORY_FEATURED)
            1 -> return BrowseListFragment.newInstance(1, RequestStoreAlbums.CATEGORY_NEW)
            2 -> return BrowseListFragment.newInstance(1, RequestStoreAlbums.CATEGORY_TOP)
            else -> return null
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "Featured"
            1 -> return "New"
            2 -> return "Top"
        }

        return super.getPageTitle(position)
    }
}