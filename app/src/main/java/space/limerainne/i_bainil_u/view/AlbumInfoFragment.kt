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
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album_info.*
import kotlinx.android.synthetic.main.fragment_purchased_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.data.api.Server
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.viewmodel.AlbumInfoRecyclerViewAdapter

/**
 * Created by Limerainne on 2016-08-11.
 */
class AlbumInfoFragment : Fragment() {

    // albumEntry from clicked entry; might not exist..
    var albumEntry: AlbumEntry? = null

    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
    }

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.fab)
    lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_album_info, container, false)
        ButterKnife.bind(this, view)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = albumEntry?.albumName

        fab.setOnClickListener { view -> Snackbar.make(view, "Loading album information... please wait...", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        // link toolbar with drawer in activity
        //if (activity is MainActivity && toolbar != null)
        //    (activity as MainActivity).linkDrawerToToolbar(toolbar)

        val rec_view = view.findViewById(R.id.info_recycler_view) as RecyclerView?
        rec_view?.layoutManager = LinearLayoutManager(context)

        doAsync() {
            val albumId: Long = albumEntry?.albumId ?: 2423

            val w: Server = Server()
            val wAlbumDetail = w.requestAlbumDetail(albumId, I_Bainil_UApp.USER_ID)
            val wTracks = w.requestTrackList(albumId, I_Bainil_UApp.USER_ID)

            uiThread {
                Log.d("Picasso", wAlbumDetail.jacketImage)
                Picasso.with(activity).load(wAlbumDetail.jacketImage).into(toolbar_background)

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
        if (context is OnListFragmentInteractionListener) {
            mListener = context as OnListFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // DEPRECATED this method is never called...
        when (item.getItemId()) {
            android.R.id.home -> {
                activity.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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