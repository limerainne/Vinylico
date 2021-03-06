package win.limerainne.vinylico.view.detail

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
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album_info.*
import kotlinx.android.synthetic.main.fragment_album_info.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.base.OnListFragmentInteractionListener
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.data.api.Server
import win.limerainne.vinylico.domain.model.AlbumBooklet
import win.limerainne.vinylico.domain.model.AlbumDetail
import win.limerainne.vinylico.domain.model.AlbumEntry
import win.limerainne.vinylico.domain.model.TrackList
import win.limerainne.vinylico.toolbox.DownloadTool
import win.limerainne.vinylico.toolbox.PurchaseTool
import win.limerainne.vinylico.view.DataLoadable
import win.limerainne.vinylico.view.HavingToolbar
import win.limerainne.vinylico.view.MainActivity
import win.limerainne.vinylico.view.MyFragment
import win.limerainne.vinylico.viewmodel.detail.AlbumInfoRecyclerViewAdapter

/**
 * Created by Limerainne on 2016-08-11.
 */
class AlbumInfoFragment : MyFragment(), AppBarLayout.OnOffsetChangedListener, HavingToolbar, DataLoadable {

    override val TargetLayout = R.layout.fragment_album_info

    private val KEY_ID = "KEY_ID"
    private val KEY_ARTIST_NAME = "KEY_ARTIST_NAME"
    private val KEY_ALBUM_NAME = "KEY_ALBUM_NAME"

    // albumEntry from clicked entry; might not exist..
    var albumEntry: AlbumEntry? = null

    var albumId: Long = 2423

    lateinit var albumName: String
    lateinit var artistName: String

    lateinit var albumDetail: AlbumDetail
    lateinit var albumTracks: TrackList
    lateinit var albumBooklet: AlbumBooklet

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
    @BindView(R.id.toolbar_background)
    lateinit var albumCover: ImageView
    @BindView(R.id.toolbar_header_view)
    lateinit var toolbarHeaderView: HeaderView

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

        val rec_view = view.findViewById(R.id.info_recycler_view) as RecyclerView?
        rec_view?.layoutManager = LinearLayoutManager(context)

        if (albumEntry?.purchased == 1) {
            fab.setImageResource(R.drawable.ic_download_white)
        }

        fab.setOnClickListener {
            try {
                val albumEntry = albumEntry
                if (albumEntry != null) {
                    if (albumEntry.purchased == 0)
                        PurchaseTool.purchaseAlbum(context, albumEntry)   {
                            albumEntry.purchased = 1
                            albumDetail.purchased = 1

                            fab.setImageResource(R.drawable.ic_download_white)

                            rec_view?.adapter?.notifyDataSetChanged()
                        }
                    else if (albumEntry.purchased == 1) {
                        // Download album
                        DownloadTool.downloadAlbum(albumId, albumTracks, context)
                    }
                }

                else if (albumDetail.purchased == 1) {
                    // Download album
                    DownloadTool.downloadAlbum(albumId, albumTracks, context)

                } else
                    PurchaseTool.purchaseAlbum(context, albumId, albumName, albumDetail.free)   {
                        albumEntry?.purchased = 1
                        albumDetail.purchased = 1

                        fab.setImageResource(R.drawable.ic_download_white)

                        rec_view?.adapter?.notifyDataSetChanged()
                    }

            } catch (e: UninitializedPropertyAccessException)   {
                context.toast(context.getString(R.string.msg_err_loading_contents))
            }
        }

        // make loading view consumes touch
        fragView.loading?.setOnTouchListener { view, motionEvent -> true }

        if (savedInstanceState != null) {
            val args = savedInstanceState.getBundle(KEY_ID)
            albumId = args.getLong(KEY_ID)
            if (args.containsKey(KEY_ARTIST_NAME))
                artistName = args.getString(KEY_ARTIST_NAME)
            if (args.containsKey(KEY_ALBUM_NAME))
                albumName = args.getString(KEY_ALBUM_NAME)
        }

        loadData()

        return view
    }

    override fun initToolbar()  {
        attachToolbar(fragView)
    }

    private fun setToolbarTitles(title: String, subtitle: String) {
        toolbarHeaderView.bindTo(title, subtitle)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val args = Bundle()
        args.putLong(KEY_ID, albumId)
        try {
            args.putString(KEY_ARTIST_NAME, artistName)
            args.putString(KEY_ALBUM_NAME, albumName)
        } catch (e: UninitializedPropertyAccessException)   {}

        outState.putBundle(KEY_ID, args)
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

    override fun loadData() {
        val rec_view = fragView.info_recycler_view

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
//                                .noFade()
                                .into(albumCover)

                        fragView.loading?.visibility = View.INVISIBLE

                        artistName = albumDetail.artistName
                        albumName = albumDetail.albumName

                        setToolbarTitles(artistName, albumName)

                        rec_view?.adapter = AlbumInfoRecyclerViewAdapter(context, albumEntry, albumDetail, albumTracks, mListener)
//                        Log.d("Found", albumTracks.albumId.toString())
//                        Log.v("Found", albumDetail.labelName)
//                        Log.v("Found", albumTracks.tracks[0].songName)
                    }
                }

                doAsync {
                    if (albumDetail.purchased > 0)  {
                        albumBooklet = w.requestAlbumBooklet(albumId)

                        val adapter: AlbumInfoRecyclerViewAdapter? = rec_view?.adapter as AlbumInfoRecyclerViewAdapter
                        adapter?.bindBooklet(albumBooklet)
                    }
                }

            } catch (e: UninitializedPropertyAccessException)  {
                e.printStackTrace()

                // TODO show reload icon
            }
        }
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

}