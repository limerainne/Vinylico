package space.limerainne.i_bainil_u.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.data.api.Server
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.viewmodel.AlbumInfoRecyclerViewAdapter

/**
 * Created by Limerainne on 2016-08-11.
 */
class AlbumInfoFragment : Fragment() {

    // albumEntry from clicked entry; might not exist..
    var albumEntry: AlbumEntry? = null

    private var mListener: WishlistFragment.OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_album_info, container, false)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar?
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val fab = view.findViewById(R.id.fab) as FloatingActionButton?
        fab!!.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        // link toolbar with drawer in activity
        if (activity is MainActivity && toolbar != null)
            (activity as MainActivity).linkDrawerToToolbar(toolbar)

        val rec_view = view.findViewById(R.id.info_recycler_view) as RecyclerView?
        rec_view?.layoutManager = LinearLayoutManager(context)

        async() {
            val albumId: Long = 2423
            val w: Server = Server()
            val wAlbumDetail = w.requestAlbumDetail(albumId, I_Bainil_UApp.USER_ID)
            val wTracks = w.requestTrackList(albumId, I_Bainil_UApp.USER_ID)
            Log.d("Found", wAlbumDetail.labelName)
            Log.d("Found", wTracks.albumId.toString())
            Log.d("Found", wTracks.tracks[0].songName)

            uiThread {
                rec_view?.adapter = AlbumInfoRecyclerViewAdapter(wAlbumDetail, wTracks, mListener)
                Log.d("Found", wAlbumDetail.labelName)
                Log.d("Found", wTracks.albumId.toString())
                Log.d("Found", wTracks.tracks[0].songName)
            }
        }

        if (savedInstanceState == null) {
            // TODO
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //        if (context instanceof OnListFragmentInteractionListener) {
        //            mListener = (OnListFragmentInteractionListener) context;
        if (context is WishlistFragment.OnListFragmentInteractionListener) {
            mListener = context as WishlistFragment.OnListFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    companion object {
        val TAG = AlbumInfoFragment::class.java.simpleName

        fun newInstance(albumEntry: AlbumEntry): AlbumInfoFragment {
            val albumInfoFragment = AlbumInfoFragment()
            albumInfoFragment.albumEntry = albumEntry

            return albumInfoFragment
        }
    }

}