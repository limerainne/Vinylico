package win.limerainne.i_bainil_u.view.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.internal.NavigationMenu
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.base.OnFragmentInteractionListener
import win.limerainne.i_bainil_u.view.DataLoadable
import win.limerainne.i_bainil_u.view.InteractWithMainActivity
import win.limerainne.i_bainil_u.view.MainActivity
import win.limerainne.i_bainil_u.view.MyFragment

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : MyFragment(), DataLoadable, UpdatingToolbar, InteractWithMainActivity {

    override val TargetLayout = R.layout.fragment_home

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) as View

//        val toolbar = parentFragment.toolbar
//        toolbar.title = getString(R.string.nav_home)
//        toolbar.subtitle = getString(R.string.app_name)

        /*
        - recommended album
        http://www.bainil.com/api/v2/user/eml?userId=2543&&lang=ko
        - event banner
        http://www.bainil.com/api/v2/connected/events?userId=2&lang=en
         */

        view.btn_nav_browse.setOnClickListener {
            if (context is MainActivity) {
                val menu = (context as MainActivity).nav_view.menu.findItem(R.id.nav_browse)
                (context as MainActivity).onNavigationItemSelected(menu)
            }
        }
        view.btn_nav_wishlist.setOnClickListener {
            if (context is MainActivity) {
                val menu = (context as MainActivity).nav_view.menu.findItem(R.id.nav_wishlist)
                (context as MainActivity).onNavigationItemSelected(menu)
            }
        }
        view.btn_nav_purchased.setOnClickListener {
            if (context is MainActivity) {
                val menu = (context as MainActivity).nav_view.menu.findItem(R.id.nav_purchased)
                (context as MainActivity).onNavigationItemSelected(menu)
            }
        }

        return view
    }

    override fun updateTitle(callback: (title: String, subtitle: String) -> Unit)   {
        callback(ThisApp.AppName, ThisApp.AppContext.getString(NavMenuName))
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context as OnFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()

        interactTo()
    }

    override fun interactTo() {
        if (activity is MainActivity) {
            (activity as MainActivity).setNavigationViewCheckedItem(NavMenuId)
            (activity as MainActivity).setToolbarColor()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun loadData() {
        // TODO do nothing for now
    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
        val NavMenuId = R.id.nav_home
        val NavMenuName = R.string.nav_home

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
