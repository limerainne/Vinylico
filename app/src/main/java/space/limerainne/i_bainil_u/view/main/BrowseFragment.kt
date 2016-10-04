package space.limerainne.i_bainil_u.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.android.synthetic.main.fragment_browse.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.viewmodel.main.BrowseFragmentAdapter

/**
 * Created by Limerainne on 2016-08-23.
 */
class BrowseFragment : Fragment() {

    lateinit var pageAdapter: BrowseFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_browse, container, false)

//        val toolbar = parentFragment.toolbar
//        toolbar.title = getString(R.string.nav_home)
//        toolbar.subtitle = getString(R.string.app_name)

        pageAdapter = BrowseFragmentAdapter(childFragmentManager)
        val pagerYay = view.pager
        pagerYay.adapter = pageAdapter
        pagerYay.setCurrentItem(1)  // "NEW" as default page

        return view
    }

    companion object {
        val TAG = BrowseFragment::class.java.simpleName
        val NavMenuId = R.id.nav_browse

        fun newInstance(): BrowseFragment {
            val fragment = BrowseFragment()
            return fragment
        }
    }
}