package space.limerainne.i_bainil_u.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp

import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.data.api.Request
import space.limerainne.i_bainil_u.data.api.RequestStoreAlbums
import space.limerainne.i_bainil_u.data.api.Server
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.view.dummy.DummyContent
import space.limerainne.i_bainil_u.view.dummy.DummyContent.DummyItem
import space.limerainne.i_bainil_u.viewmodel.BrowseListRecyclerViewAdapter
import space.limerainne.i_bainil_u.viewmodel.PurchasedRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class BrowseListFragment : Fragment(), BrowseListRecyclerViewAdapter.EndlessScrollListener {

    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    private lateinit var viewAdapter: BrowseListRecyclerViewAdapter

    private var category = RequestStoreAlbums.CATEGORY_NEW
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
        val view = inflater!!.inflate(R.layout.fragment_browse_list, container, false)

        // TODO get data
        doAsync() {
            nextOffset = 0L

            val s: Server = Server()
            val sList = s.requestStoreAlbums(category, I_Bainil_UApp.USER_ID, nextOffset, length)

            uiThread { if (view is RecyclerView) {
                viewAdapter = BrowseListRecyclerViewAdapter(context, sList, mListener)
                viewAdapter.setEndlessScrollListener(this@BrowseListFragment)
                view.adapter = viewAdapter

                offset = nextOffset
                nextOffset += 1
                println("offset: ${offset}, nextOffset: ${nextOffset}")
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
        val NavMenuId = R.id.nav_browse

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

    override fun onLoadMore(position: Int): Boolean {
        println("onLoadMore:" + position + ", to:o:" + nextOffset + ",l:" + length)
        doAsync {
            val s: Server = Server()
            val sList = s.requestStoreAlbums(category, I_Bainil_UApp.USER_ID, nextOffset, length)

            println(sList)

            if (sList.albumEntries.size > 0) {
                uiThread {
                    viewAdapter.addItems(sList)

                    offset = nextOffset
                    nextOffset += 1
                    println("offset: ${offset}, nextOffset: ${nextOffset}")
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
