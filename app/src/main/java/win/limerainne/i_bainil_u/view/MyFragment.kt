package win.limerainne.i_bainil_u.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import win.limerainne.i_bainil_u.R

/**
 * Created by Limerainne on 2016-11-05.
 */
abstract class MyFragment: Fragment(), DataLoadable {

    abstract val TargetLayout: Int

    protected lateinit var fragView: View
    val toolbar: Toolbar
        get() = fragView.findViewById(R.id.toolbar) as Toolbar

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragView = inflater!!.inflate(TargetLayout, container, false)

        if (this is HavingToolbar)
            initToolbar()

        return fragView
    }

    open fun attachToolbar(view: View)  {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun loadData()  {}

}