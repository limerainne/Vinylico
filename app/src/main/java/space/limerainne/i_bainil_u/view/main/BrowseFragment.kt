package space.limerainne.i_bainil_u.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_browse.view.*
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.view.DataLoadable
import space.limerainne.i_bainil_u.view.InteractWithMainActivity
import space.limerainne.i_bainil_u.view.MainActivity
import space.limerainne.i_bainil_u.view.MyFragment
import space.limerainne.i_bainil_u.viewmodel.main.BrowseFragmentAdapter

/**
 * Created by Limerainne on 2016-08-23.
 */
class BrowseFragment : MyFragment(), DataLoadable, UpdatingToolbar, InteractWithMainActivity {

    override val TargetLayout = R.layout.fragment_browse

    lateinit var pageAdapter: BrowseFragmentAdapter
    lateinit var pagerYay: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = super.onCreateView(inflater, container, savedInstanceState) as View

//        val toolbar = parentFragment.toolbar
//        toolbar.title = getString(R.string.nav_home)
//        toolbar.subtitle = getString(R.string.app_name)

        pageAdapter = BrowseFragmentAdapter(childFragmentManager)
        pagerYay = view.pager
        pagerYay.adapter = pageAdapter
        pagerYay.currentItem = 1  // "NEW" as default page

        return view
    }

    override fun onResume() {
        super.onResume()

        interactTo()
    }

    override fun interactTo()   {
        if (activity is MainActivity) {
            (activity as MainActivity).setNavigationViewCheckedItem(NavMenuId)
            (activity as MainActivity).setToolbarColor()
            //(activity as MainActivity).setToolbarColor(R.color.babyPink, R.color.babyPinkDark)
        }
    }


    override fun updateTitle(callback: (title: String, subtitle: String) -> Unit)   {
        callback(I_Bainil_UApp.AppName, I_Bainil_UApp.AppContext.getString(BrowseFragment.NavMenuName))
    }

    override fun loadData() {
        val childId = pagerYay.currentItem
        val child = pageAdapter.getItem(childId) as Fragment

        if (child is MyFragment && child is DataLoadable)
            child.loadData()
    }

    companion object {
        val TAG = BrowseFragment::class.java.simpleName
        val NavMenuId = R.id.nav_browse
        val NavMenuName = R.string.nav_browse

        fun newInstance(): BrowseFragment {
            val fragment = BrowseFragment()
            return fragment
        }
    }
}