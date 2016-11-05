package win.limerainne.i_bainil_u.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.view.HavingChildFragment
import win.limerainne.i_bainil_u.view.HavingToolbar
import win.limerainne.i_bainil_u.view.MyFragment

/**
 * Created by Limerainne on 2016-11-05.
 */
abstract class MyFrameFragment: MyFragment(), HavingToolbar, HavingChildFragment {

    abstract val DefaultFragmentTag: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) as View

        childFragmentManager.addOnBackStackChangedListener {
            Log.v("ChildFragStack", "Yay")
            val child = activeChildFragment as Fragment
            if (child is UpdatingToolbar)
                child.updateTitle { title, subtitle ->
                    println(title + " " + subtitle)
                    toolbar.subtitle = title
                    toolbar.title = subtitle
                }
        }
        Log.v("ChildFragStack", "registered")

        return view
    }

    fun onBackPressed(): Boolean    {
        if (childFragmentManager.backStackEntryCount >= 1) {
            childFragmentManager.popBackStack()
            return true
        }
        return false
    }

    override fun changeChildFragment(targetFragment: Fragment, fragmentTAG: String, backStack: Boolean)   {
        Log.d("Test", activeChildFragment?.tag.toString())

        val currentChildFragmentTag = activeChildFragment?.tag
        if (currentChildFragmentTag != null && currentChildFragmentTag.equals(targetFragment.tag)) {

        }   else    {
            Log.d("Test", targetFragment.toString())

            val transaction = childFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.replace(R.id.content_main, targetFragment, fragmentTAG)
            if (backStack)
                transaction.addToBackStack(fragmentTAG)
            transaction.commit()

            childFragmentManager.executePendingTransactions()

            val child = targetFragment
            if (child is UpdatingToolbar)
                child.updateTitle { title, subtitle ->
                    try {
                        toolbar.title = title
                        toolbar.subtitle = subtitle
                    } catch(e: UninitializedPropertyAccessException)    {

                    }
                }
        }
    }

    override val activeChildFragment: Fragment?
        get() {
            return childFragmentManager.findFragmentById(R.id.content_main)
    }

}