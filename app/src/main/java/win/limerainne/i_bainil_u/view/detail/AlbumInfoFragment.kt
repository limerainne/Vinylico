package win.limerainne.i_bainil_u.view.detail

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album_info.*
import kotlinx.android.synthetic.main.fragment_album_info.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import win.limerainne.i_bainil_u.credential.UserInfo
import win.limerainne.i_bainil_u.data.api.Server
import win.limerainne.i_bainil_u.domain.model.AlbumDetail
import win.limerainne.i_bainil_u.domain.model.AlbumEntry
import win.limerainne.i_bainil_u.domain.model.TrackList
import win.limerainne.i_bainil_u.toolbox.DownloadTool
import win.limerainne.i_bainil_u.toolbox.PurchaseTool
import win.limerainne.i_bainil_u.view.DataLoadable
import win.limerainne.i_bainil_u.view.HavingToolbar
import win.limerainne.i_bainil_u.view.MainActivity
import win.limerainne.i_bainil_u.view.MyFragment
import win.limerainne.i_bainil_u.viewmodel.detail.AlbumInfoRecyclerViewAdapter

/**
 * Created by Limerainne on 2016-08-11.
 */
class AlbumInfoFragment : MyFragment(), AppBarLayout.OnOffsetChangedListener, HavingToolbar, DataLoadable {

    override val TargetLayout = R.layout.fragment_album_info

    // albumEntry from clicked entry; might not exist..
    var albumEntry: AlbumEntry? = null

    var albumId: Long = 2423

    lateinit var albumName: String
    lateinit var artistName: String

    lateinit var albumDetail: AlbumDetail
    lateinit var albumTracks: TrackList

    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
    }

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
        val view = super.onCreateView(inflater, container, savedInstanceState) as View
        ButterKnife.bind(this, view)

//        view.toolbar.setPadding(0, (activity as MainActivity).getStatusBarHeight(), 0, 0)

        view.toolbar_layout.title =""
        toolbar.title = ""

        artistName = albumEntry?.artistName ?: ""
        albumName = albumEntry?.albumName ?: ""

        setToolbarTitles(artistName, albumName)

        appBarLayout.addOnOffsetChangedListener(this)

        if (albumEntry?.purchased == 1) {
            fab.setImageResource(R.drawable.ic_download_white)
        }

        fab.setOnClickListener {
            try {
                val albumEntry = albumEntry
                if (albumEntry != null) {
                    if (albumEntry.purchased == 0)
                        PurchaseTool.purchaseAlbum(context, albumEntry)
                    else if (albumEntry.purchased == 1) {
                        // Download album
                        DownloadTool.downloadAlbum(albumId, albumTracks, context)
                    }
                }

                else if (albumDetail.purchased == 1) {
                    // Download album
                    DownloadTool.downloadAlbum(albumId, albumTracks, context)

                } else
                    PurchaseTool.purchaseAlbum(context, albumId, albumName, albumDetail.free)

            } catch (e: UninitializedPropertyAccessException)   {
                context.toast(context.getString(R.string.msg_err_loading_contents))
            }
        }

        // link toolbar with drawer in activity
        //if (activity is MainActivity && toolbar != null)
        //    (activity as MainActivity).linkDrawerToToolbar(toolbar)

        val rec_view = view.findViewById(R.id.info_recycler_view) as RecyclerView?
        rec_view?.layoutManager = LinearLayoutManager(context)

        doAsync(ThisApp.ExceptionHandler) {
            val w: Server = Server()
            albumDetail = w.requestAlbumDetail(albumId, UserInfo.getUserIdOr(context))
            albumTracks = w.requestTrackList(albumId, UserInfo.getUserIdOr(context))

            try {
                // check if 2 vars are initialized
                albumDetail.purchased = albumEntry?.purchased ?: -1

                uiThread {
                    if (context != null) {
//                        Log.v("Picasso", albumDetail.jacketImage)
                        Picasso.with(context)
                                .load(albumDetail.jacketImage)
                                .noFade()
                                .into(toolbar_background)

                        view.loading.visibility = View.INVISIBLE

                        artistName = albumDetail.artistName
                        albumName = albumDetail.albumName

                        setToolbarTitles(artistName, albumName)

                        rec_view?.adapter = AlbumInfoRecyclerViewAdapter(context, albumEntry, albumDetail, albumTracks, mListener)
                        Log.d("Found", albumTracks.albumId.toString())
                        Log.v("Found", albumDetail.labelName)
                        Log.v("Found", albumTracks.tracks[0].songName)
                    }
                }

//                if (albumDetail.purchased == 1)
//                    AnnotateWebDownloadIdCommand(albumId, albumTracks).execute()

            } catch (e: UninitializedPropertyAccessException)  {
                e.printStackTrace()

                // TODO show reload icon
            }
        }

//        if (savedInstanceState == null) {
//            // TODO
//        }

        return view
    }

    override fun initToolbar()  {
        attachToolbar(fragView)
    }

    private fun setToolbarTitles(title: String, subtitle: String) {
        toolbarHeaderView.bindTo(title, subtitle)
        floatHeaderView.bindTo(title, subtitle)
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

    override fun onPause()  {
        super.onPause()

        Picasso.with(context)
                .cancelRequest(toolbar_background)
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
            albumInfoFragment.albumId = albumEntry.albumId

            return albumInfoFragment
        }

        fun newInstance(albumId: Long, albumName: String, artistName: String): AlbumInfoFragment {
            val albumInfoFragment = AlbumInfoFragment()

            albumInfoFragment.albumId = albumId
            albumInfoFragment.albumName = albumName
            albumInfoFragment.artistName = artistName

            return albumInfoFragment
        }

    }

    interface PurchaseSuccessListener  {
        fun purchaseSucceed()
    }

    inner class MyPurchaseSuccessListener: PurchaseSuccessListener    {
        override fun purchaseSucceed() {

        }

    }

}