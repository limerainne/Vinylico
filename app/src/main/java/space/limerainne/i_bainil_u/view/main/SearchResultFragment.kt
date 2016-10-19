package space.limerainne.i_bainil_u.view.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.doAsync
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnFragmentInteractionListener
import space.limerainne.i_bainil_u.data.api.Server
import space.limerainne.i_bainil_u.view.MainActivity

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchResultFragment : Fragment() {

    // TODO: Rename and change types of parameters
    var keyword: String = ""

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            keyword = arguments.getString(ARG_KEYWORD)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)

        doAsync {
            Server()
        }

        return view
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

        if (activity is MainActivity) {
            (activity as MainActivity).setNavigationViewCheckedItem(NavMenuId)
            (activity as MainActivity).setToolbarColor()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {
        val TAG = SearchResultFragment::class.java.simpleName
        val NavMenuId = R.id.nav_search_result

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
