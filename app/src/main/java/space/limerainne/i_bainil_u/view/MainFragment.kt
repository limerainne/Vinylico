package space.limerainne.i_bainil_u.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import space.limerainne.i_bainil_u.R

/**
 * Created by Limerainne on 2016-08-07.
 */
class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_main, container, false)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar?
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val fab = view.findViewById(R.id.fab) as FloatingActionButton?
        fab!!.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        // link toolbar with drawer in activity
        if (activity is MainActivity && toolbar != null)
            (activity as MainActivity).linkDrawerToToolbar(toolbar)

        if (savedInstanceState == null) {
            val homeFragment = HomeFragment.newInstance("1", "1")
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().add(R.id.content_main, homeFragment, HomeFragment.TAG).commit()
            (activity as MainActivity).fragments.put(R.id.nav_home, homeFragment)
        }

        return view
    }

    fun onBackPressed(): Boolean    {
        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
            return true
        }
        return false
    }

    fun changeChildFragment(targetFragment: Fragment, fragmentTAG: String)   {
        Log.d("Test", getActiveChildFragment()?.tag.toString())

        val currentChildFragmentTag = getActiveChildFragment()?.tag
        if (currentChildFragmentTag != null && currentChildFragmentTag.equals(targetFragment.tag)) {

        }   else    {
            Log.d("Test", targetFragment.toString())

            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.content_main, targetFragment, fragmentTAG).addToBackStack(fragmentTAG)
            transaction.commit()
        }
    }

    fun getActiveChildFragment(): Fragment? {
        if (childFragmentManager.backStackEntryCount === 0) {
            return childFragmentManager?.findFragmentByTag(HomeFragment.TAG)
        }

        val tag = childFragmentManager.getBackStackEntryAt(childFragmentManager.backStackEntryCount - 1).name
        return childFragmentManager.findFragmentByTag(tag)
    }

    companion object {
        val TAG = MainFragment::class.java.simpleName

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}