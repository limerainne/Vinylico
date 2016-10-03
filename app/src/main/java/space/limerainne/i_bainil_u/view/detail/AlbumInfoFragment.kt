package space.limerainne.i_bainil_u.view.detail

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album_info.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.base.PurchaseTool
import space.limerainne.i_bainil_u.base.UserInfo
import space.limerainne.i_bainil_u.data.api.RequestAlbumPurchased
import space.limerainne.i_bainil_u.data.api.Server
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.view.MainActivity
import space.limerainne.i_bainil_u.view.webview.PurchaseWebviewFragment
import space.limerainne.i_bainil_u.viewmodel.detail.AlbumInfoRecyclerViewAdapter

/**
 * Created by Limerainne on 2016-08-11.
 */
class AlbumInfoFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {

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

    @BindView(R.id.app_bar)
    lateinit var appBarLayout: AppBarLayout
    @BindView(R.id.toolbar_layout)
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    @BindView(R.id.toolbar_header_view)
    lateinit var toolbarHeaderView: HeaderView
    @BindView(R.id.float_header_view)
    lateinit var floatHeaderView: HeaderView

    private var isHideToolbarView = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_album_info, container, false)
        ButterKnife.bind(this, view)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        collapsingToolbarLayout.title =""
        toolbar.title = ""

        val title = albumEntry?.artistName ?: ""
        val subtitle = albumEntry?.albumName ?: ""

        toolbarHeaderView.bindTo(title, subtitle)
        floatHeaderView.bindTo(title, subtitle)

        appBarLayout.addOnOffsetChangedListener(this)

        if (albumEntry?.purchased == 1) {
            fab.setImageResource(R.drawable.ic_download_white)
        }

        fab.setOnClickListener {
            val albumEntry = albumEntry
            if (albumEntry != null)
                PurchaseTool.purchaseAlbum(context, albumEntry)
        }

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
                Picasso.with(activity)
                        .load(wAlbumDetail.jacketImage)
                        .noFade()
                        .into(toolbar_background)

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

    override fun onResume() {
        super.onResume()

        if (activity is MainActivity) {
            (activity as MainActivity).unsetNavigationViewCheckedItem()
            (activity as MainActivity).setToolbarColor()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

//        if (percentage == 1f && isHideToolbarView) {
//            toolbarHeaderView.visibility = View.VISIBLE
//            isHideToolbarView = !isHideToolbarView
//
//        } else if (percentage < 1f && !isHideToolbarView) {
//            toolbarHeaderView.visibility = View.GONE
//            isHideToolbarView = !isHideToolbarView
//        }
        toolbarHeaderView.visibility = View.VISIBLE
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