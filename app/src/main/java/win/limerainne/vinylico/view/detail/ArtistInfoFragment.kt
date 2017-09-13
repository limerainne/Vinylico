package win.limerainne.vinylico.view.detail

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.base.OnListFragmentInteractionListener
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.data.api.Server
import win.limerainne.vinylico.domain.model.AlbumEntry
import win.limerainne.vinylico.domain.model.ArtistAlbumList
import win.limerainne.vinylico.domain.model.ArtistDetail
import win.limerainne.vinylico.view.DataLoadable
import win.limerainne.vinylico.view.HavingToolbar
import win.limerainne.vinylico.view.MainActivity
import win.limerainne.vinylico.view.MyFragment
import win.limerainne.vinylico.viewmodel.detail.AlbumInfoRecyclerViewAdapter

/**
 * Created by Limerainne on 2017-09-13.
 */
class ArtistInfoFragment : MyFragment(), AppBarLayout.OnOffsetChangedListener, HavingToolbar, DataLoadable {

    override val TargetLayout = R.layout.fragment_artist_info

    private val KEY_ID = "KEY_ID"
    private val KEY_ARTIST_NAME = "KEY_ARTIST_NAME"

    var artistId: Long = 1005

    lateinit var artistName: String

    lateinit var artistDetail: ArtistDetail
    lateinit var artistAlbumList: ArtistAlbumList

    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
    }

    @BindView(R.id.app_bar)
    lateinit var appBarLayout: AppBarLayout
    @BindView(R.id.toolbar_layout)
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    @BindView(R.id.toolbar_background)
    lateinit var artistImage: ImageView
    @BindView(R.id.toolbar_header_view)
    lateinit var toolbarHeaderView: HeaderView

    private var isHideToolbarView = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) as View
        ButterKnife.bind(this, view)

        view.toolbar_layout.title =""
        toolbar.title = ""

        artistName = artistName ?: ""

        setToolbarTitles(artistName, getString(R.string.artist_info_subtitle))

        appBarLayout.addOnOffsetChangedListener(this)

        val rec_view = view.findViewById(R.id.info_recycler_view) as RecyclerView?
        rec_view?.layoutManager = LinearLayoutManager(context)

        // make loading view consumes touch
        fragView.loading?.setOnTouchListener { view, motionEvent -> true }

        if (savedInstanceState != null) {
            val args = savedInstanceState.getBundle(KEY_ID)
            artistId = args.getLong(KEY_ID)
            if (args.containsKey(KEY_ARTIST_NAME))
                artistName = args.getString(KEY_ARTIST_NAME)
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
        args.putLong(KEY_ID, artistId)
        try {
            args.putString(KEY_ARTIST_NAME, artistName)
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
            artistDetail = w.requestArtistDetail(artistId)
            artistAlbumList = w.requestArtistAlbumList(artistId)

            try {
                uiThread {
                    if (context != null) {
//                        Log.v("Picasso", albumDetail.jacketImage)
                        Picasso.with(context)
                                .load(artistDetail.artistPicture)
//                                .noFade()
                                .into(artistImage)

                        fragView.loading?.visibility = View.INVISIBLE

                        artistName = artistDetail.artistName

                        setToolbarTitles(artistName, getString(R.string.artist_info_subtitle))

                        rec_view?.adapter = ArtistInfoRecyclerViewAdapter(context, artistDetail, artistAlbumList, mListener)
//                        Log.d("Found", albumTracks.albumId.toString())
//                        Log.v("Found", albumDetail.labelName)
//                        Log.v("Found", albumTracks.tracks[0].songName)
                    }
                }

            } catch (e: UninitializedPropertyAccessException)  {
                e.printStackTrace()

                // TODO show reload icon
            }
        }
    }

    companion object {
        val TAG = ArtistInfoFragment::class.java.simpleName

        fun newInstance(artistId: Long, artistName: String): ArtistInfoFragment {
            val artistInfoFragment = ArtistInfoFragment()

            artistInfoFragment.artistId = artistId
            artistInfoFragment.artistName = artistName

            return artistInfoFragment
        }

    }

}