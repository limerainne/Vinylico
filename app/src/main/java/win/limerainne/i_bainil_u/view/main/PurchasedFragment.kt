package win.limerainne.i_bainil_u.view.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_browse_list.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import win.limerainne.i_bainil_u.credential.LoginCookie
import win.limerainne.i_bainil_u.credential.UserInfo
import win.limerainne.i_bainil_u.data.api.Server
import win.limerainne.i_bainil_u.view.DataLoadable
import win.limerainne.i_bainil_u.view.InteractWithMainActivity
import win.limerainne.i_bainil_u.view.MainActivity
import win.limerainne.i_bainil_u.view.MyFragment
import win.limerainne.i_bainil_u.viewmodel.main.PurchasedRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class PurchasedFragment : MyFragment(), DataLoadable, UpdatingToolbar, InteractWithMainActivity {

    override val TargetLayout = R.layout.fragment_browse_list

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
        val view = super.onCreateView(inflater, container, savedInstanceState) as View

//        val toolbar = parentFragment.toolbar
//        toolbar.title = getString(R.string.nav_home)
//        toolbar.subtitle = getString(R.string.app_name)

        // TODO get data
        loadData()

        // Set the adapter
        if (view.list is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.list.layoutManager = LinearLayoutManager(context)
            } else {
                view.list.layoutManager = GridLayoutManager(context, mColumnCount)
            }
        }
        return view
    }

    override fun updateTitle(callback: (title: String, subtitle: String) -> Unit)   {
        callback(ThisApp.AppName, ThisApp.AppContext.getString(PurchasedFragment.NavMenuName))
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

        interactTo()
    }

    override fun interactTo()   {
        if (activity is MainActivity) {
            (activity as MainActivity).setNavigationViewCheckedItem(NavMenuId)
            (activity as MainActivity).setToolbarColor()
            //(activity as MainActivity).setToolbarColor(R.color.babyPink, R.color.babyPinkDark)
        }

        if (fragView.btn_reload.visibility == View.VISIBLE)   {
            doAsync {
                Thread.sleep(500)
                if (UserInfo.checkLogin(context))
                    uiThread {
                        loadData()
                    }
            }
        }
    }

    override fun onPause()  {
        super.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun loadData()  {
        val view = fragView

        UserInfo.checkLoginThenRun(context, {
            view.btn_reload.visibility = View.INVISIBLE

            doAsync() {
                println("PurchasedFragment: request data")
                val s: Server = Server()
                val pList = s.requestConnected(UserInfo.getUserIdOr(context))
                println("PurchasedFragment: got data")
                uiThread { if (view.list is RecyclerView) {
                    if (context != null) {
                        println("PurchasedFragment: showing data")
                        view.list.adapter = PurchasedRecyclerViewAdapter(context, pList, mListener)

                        view.loading.visibility = View.INVISIBLE
                        view.list.visibility = View.VISIBLE
                    }
                }
                }
            }
        }, {
            view.btn_reload.visibility = View.VISIBLE
            view.btn_reload.setOnClickListener {
                loadData()
            }
        })
    }

    companion object {
        val TAG = PurchasedFragment::class.java.simpleName
        val NavMenuId = R.id.nav_purchased
        val NavMenuName = R.string.nav_purchased

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
