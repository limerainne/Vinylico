package win.limerainne.vinylico.view

import android.support.v4.app.Fragment

/**
 * Created by Limerainne on 2016-11-05.
 */
interface HavingChildFragment {
    val activeChildFragment: Fragment?
    fun changeChildFragment(targetFragment: Fragment, fragmentTAG: String, backStack: Boolean = false)
}