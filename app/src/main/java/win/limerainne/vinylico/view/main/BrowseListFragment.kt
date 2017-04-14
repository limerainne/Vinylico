package win.limerainne.vinylico.view.main

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_browse_list.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.base.OnListFragmentInteractionListener
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.data.api.Server
import win.limerainne.vinylico.data.api.request.data.RequestStoreAlbums
import win.limerainne.vinylico.view.DataLoadable
import win.limerainne.vinylico.view.MainActivity
import win.limerainne.vinylico.view.MyFragment
import win.limerainne.vinylico.viewmodel.main.BrowseListRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class BrowseListFragment : MyFragment(), BrowseListRecyclerViewAdapter.EndlessScrollListener, DataLoadable {

    override val TargetLayout = R.layout.fragment_browse_list

    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    private lateinit var viewAdapter: BrowseListRecyclerViewAdapter

    private var category = RequestStoreAlbums.CATEGORY_FEATURED
    private var offset = 0L;
    private var length = 20L;
    private var nextOffset = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState) as View

        // get data
        loadData()

        // Set the adapter
        if (fragView.list is RecyclerView) {
            val context = fragView.getContext()
            if (mColumnCount <= 1) {
                fragView.list.layoutManager = LinearLayoutManager(context)
            } else {
                fragView.list.layoutManager = GridLayoutManager(context, mColumnCount)
            }
        }
        return fragView
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

    override fun onPause()  {
        super.onPause()

        if (activity is MainActivity) {
            // unset color
            (activity as MainActivity).setToolbarColor()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null

        if (activity is MainActivity)
            (activity as MainActivity).setToolbarColor()
    }

    companion object {
        val TAG = BrowseListFragment::class.java.simpleName

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @SuppressWarnings("unused")
        fun newInstance(columnCount: Int, category: String = RequestStoreAlbums.CATEGORY_NEW): BrowseListFragment {
            val fragment = BrowseListFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args

            fragment.category = category

            return fragment
        }
    }

    override fun loadData() {
        val view: View = try {
            fragView
        } catch(e: UninitializedPropertyAccessException)    {
            getView() ?: return
        }

        // show loading msg
        view.loading.visibility = View.VISIBLE
        view.list.visibility = View.INVISIBLE

        doAsync(ThisApp.ExceptionHandler) {
            nextOffset = 0L

            val s: Server = Server()
            val sList = s.requestStoreAlbums(category, UserInfo.getUserIdOr(context), nextOffset, length)

            uiThread { if (view.list is RecyclerView) {
                if (context != null) {
                    viewAdapter = BrowseListRecyclerViewAdapter(context, sList, mListener)
                    viewAdapter.setEndlessScrollListener(this@BrowseListFragment)
                    view.list.adapter = viewAdapter

                    // init offset
                    offset = nextOffset
                    nextOffset += 1
//                    Log.v("BrowseList", "offset: ${offset}, nextOffset: ${nextOffset}")

                    // hide loading msg
                    view.loading.visibility = View.GONE
                    view.list.visibility = View.VISIBLE
                }
            }
            }
        }
    }

    override fun onLoadMore(position: Int): Boolean {
//        Log.v("BrowseList", "onLoadMore:" + position + ", to:o:" + nextOffset + ",l:" + length)
        doAsync(ThisApp.ExceptionHandler) {
            val s: Server = Server()
            val sList = s.requestStoreAlbums(category, UserInfo.getUserIdOr(context), nextOffset, length)

//            println(sList)

            if (sList.albumEntries.size > 0) {
                uiThread {
                    viewAdapter.addItems(sList)

                    offset = nextOffset
                    nextOffset += 1
//                    Log.v("BrowseList", "offset: ${offset}, nextOffset: ${nextOffset}")
                }
            }
        }
        return true
    }

}
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
