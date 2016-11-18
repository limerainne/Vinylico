package win.limerainne.vinylico.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_browse.view.*
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.view.DataLoadable
import win.limerainne.vinylico.view.InteractWithMainActivity
import win.limerainne.vinylico.view.MainActivity
import win.limerainne.vinylico.view.MyFragment
import win.limerainne.vinylico.viewmodel.main.BrowseFragmentAdapter

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
        callback(ThisApp.AppName, ThisApp.AppContext.getString(BrowseFragment.NavMenuName))
    }

    override fun loadData() {
        val child = pageAdapter.currentFragment

        if (child is DataLoadable)
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