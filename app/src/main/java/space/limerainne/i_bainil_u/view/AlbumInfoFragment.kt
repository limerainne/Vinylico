package space.limerainne.i_bainil_u.view

import android.content.Context
import android.net.ConnectivityManager
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
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album_info.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.base.UserInfo
import space.limerainne.i_bainil_u.data.api.RequestAlbumPurchased
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
        toolbar.title = albumEntry?.artistName
        toolbar.subtitle = albumEntry?.albumName

        if (albumEntry?.purchased == 1) {
            fab.setImageResource(R.drawable.ic_download_white)
        }

        fab.setOnClickListener {
            /*
            http://www.bainil.com/api/v2/purchase/request?userId=2543&albumId=2423&store=1&type=pay
            http://www.bainil.com/api/v2/kakaopay/request?albumId=3276&userId=2543
             */
            val item = albumEntry ?: return@setOnClickListener
            val context = context

            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            if (networkInfo != null && networkInfo!!.isConnected) {
                // check login first
                val userInfo = UserInfo(context)

                if (userInfo.userId < 1)    {
                    context.toast("Please login first!")
                    if (context is MainActivity)
                        context.openLoginPage()

                    return@setOnClickListener
                }

                doAsync {
                    // 1. check if previously purchased
                    val success: Boolean
                    try {
                        val userInfo = UserInfo(context)

                        success = RequestAlbumPurchased(item.albumId, userInfo.userId, true).execute()
                    } catch (e: Exception) {
                        success = false
                        e.printStackTrace()
                    }

                    // 2. redirect to purchase page
                    if (success)    {
                        if (context is MainActivity)   {
                            uiThread {
                                // TODO display purchase info & cautions
                                val userInfo = UserInfo(context)

                                val webviewFragment = PurchaseWebviewFragment.newInstance(userInfo.userId, item.albumId, context)
                                context.transitToFragment(R.id.placeholder_top, webviewFragment, PurchaseWebviewFragment.TAG)
                            }
                        }
                    }   else    {
                        uiThread {
                            context.toast("Already purchased this album: ${item.albumName}")
                        }
                    }

                    // 3. TODO check if purhcase succeed (when? where?)
                }
            } else {
                context.toast("Check network connection!")
            }
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