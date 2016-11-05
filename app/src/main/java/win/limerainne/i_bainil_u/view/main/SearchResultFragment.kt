package win.limerainne.i_bainil_u.view.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_browse_list.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.base.OnFragmentInteractionListener
import win.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import win.limerainne.i_bainil_u.credential.UserInfo
import win.limerainne.i_bainil_u.data.api.Server
import win.limerainne.i_bainil_u.view.DataLoadable
import win.limerainne.i_bainil_u.view.InteractWithMainActivity
import win.limerainne.i_bainil_u.view.MainActivity
import win.limerainne.i_bainil_u.view.MyFragment
import win.limerainne.i_bainil_u.viewmodel.main.SearchResultRecyclerViewAdapter
import win.limerainne.i_bainil_u.viewmodel.main.WishlistRecyclerViewAdapter

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchResultFragment : MyFragment(), DataLoadable, UpdatingToolbar, InteractWithMainActivity {

    override val TargetLayout = R.layout.fragment_browse_list

    var keyword: String = ""

    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            keyword = arguments.getString(ARG_KEYWORD)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragView = super.onCreateView(inflater, container, savedInstanceState) as View

        loadData()

        return fragView
    }

    override fun updateTitle(callback: (title: String, subtitle: String) -> Unit)   {
        callback(ThisApp.AppName, ThisApp.AppContext.getString(SearchResultFragment.NavMenuName))
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
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
        if (keyword.length > 0)
            loadDataImpl()
        else
            showErrorMsg()
    }

    fun loadDataImpl()    {
        val view = fragView

        view.loading.visibility = View.VISIBLE
        view.error_container.visibility = View.INVISIBLE
        view.btn_reload.visibility = View.INVISIBLE
        view.list.visibility = View.INVISIBLE

        doAsync() {
            val w: Server = Server()
            val wResult = w.requestSearchResult(keyword, UserInfo.getUserIdOr(context))
            uiThread { if (view.list is RecyclerView) {
                if (context != null) {
                    view.list.adapter = SearchResultRecyclerViewAdapter(context, wResult, mListener)

                    view.loading.visibility = View.INVISIBLE
                    view.error_container.visibility = View.INVISIBLE
                    view.btn_reload.visibility = View.INVISIBLE
                    view.list.visibility = View.VISIBLE
                }
            }
            }
        }
    }

    fun refresh(newKeyword: String) {
        if (newKeyword.length < 1) {
            showErrorMsg()
            return
        }

        keyword = newKeyword
        loadDataImpl()
    }

    fun showErrorMsg()  {
        val view = fragView

        view.list.visibility = View.INVISIBLE
        view.btn_reload.visibility = View.INVISIBLE
        view.loading.visibility = View.INVISIBLE

        view.error_container.visibility = View.VISIBLE
        view.error_msg.text = ThisApp.AppContext.getString(R.string.msg_search_notice_type_keyword)
    }

    companion object {
        val TAG = SearchResultFragment::class.java.simpleName
        val NavMenuId = R.id.nav_search_result
        val NavMenuName = R.string.nav_search_result

        private val ARG_KEYWORD = "keyword"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param keyword to search
         * *
         * @return A new instance of fragment SearchResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(keyword: String): SearchResultFragment {
            val fragment = SearchResultFragment()

            val args = Bundle()
            args.putString(ARG_KEYWORD, keyword)

            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor