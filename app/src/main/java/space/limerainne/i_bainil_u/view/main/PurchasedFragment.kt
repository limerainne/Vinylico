package space.limerainne.i_bainil_u.view.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp

import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.data.api.Server
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.view.MainActivity
import space.limerainne.i_bainil_u.view.dummy.DummyContent
import space.limerainne.i_bainil_u.view.dummy.DummyContent.DummyItem
import space.limerainne.i_bainil_u.viewmodel.main.PurchasedRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class PurchasedFragment : Fragment() {
    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_purchased, container, false)

//        val toolbar = parentFragment.toolbar
//        toolbar.title = getString(R.string.nav_home)
//        toolbar.subtitle = getString(R.string.app_name)

        // TODO get data
        doAsync() {
            println("PurchasedFragment: request data")
            val s: Server = Server()
            val pList = s.requestConnected(I_Bainil_UApp.USER_ID)
            println("PurchasedFragment: got data")
            uiThread { if (view is RecyclerView) {
                println("PurchasedFragment: showing data")
                view.adapter = PurchasedRecyclerViewAdapter(context, pList, mListener)
                }
            }
        }

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
        }
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //        if (context instanceof OnListFragmentInteractionListener) {
        //            mListener = (OnListFragmentInteractionListener) context;
        if (context is OnListFragmentInteractionListener) {
            mListener = context as OnListFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()

        if (activity is MainActivity) {
            (activity as MainActivity).setNavigationViewCheckedItem(NavMenuId)
            (activity as MainActivity).setToolbarColor()
            //(activity as MainActivity).setToolbarColor(R.color.babyPink, R.color.babyPinkDark)
        }
    }

    override fun onPause()  {
        super.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {
        val TAG = PurchasedFragment::class.java.simpleName
        val NavMenuId = R.id.nav_purchased

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @SuppressWarnings("unused")
        fun newInstance(columnCount: Int): PurchasedFragment {
            val fragment = PurchasedFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
