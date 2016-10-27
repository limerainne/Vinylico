package space.limerainne.i_bainil_u.view.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.credential.UserInfo
import space.limerainne.i_bainil_u.data.api.request.data.RequestAlbumPurchased
import space.limerainne.i_bainil_u.data.api.request.data.RequestStoreAlbums
import space.limerainne.i_bainil_u.data.api.Server
import space.limerainne.i_bainil_u.domain.model.AlbumDetail
import space.limerainne.i_bainil_u.domain.model.convertToAlbumEntry
import space.limerainne.i_bainil_u.view.MainActivity
import space.limerainne.i_bainil_u.view.detail.AlbumInfoFragment

/**
 * Created by Limerainne on 2016-08-07.
 */
class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_main, container, false)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar?
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val fab = view.findViewById(R.id.fab) as FloatingActionButton?
        fab!!.setOnClickListener {
            view ->
            val snackbar_instance = Snackbar.make(view, "Finding random album...", Snackbar.LENGTH_LONG).setAction("Action", null)
            snackbar_instance.show()

            doAsync {
                // 1. get new list --> get latest album ID
                val list_new = RequestStoreAlbums(UserInfo.getUserIdOr(context), RequestStoreAlbums.CATEGORY_NEW, 0, 10).execute()
                val latest_album_id: Long = run<Long> {
                    var result = 3000L
                    if (list_new.result.size > 0) {
                        result = list_new.result.maxBy { it.albumId ?: result }?.albumId ?: result
                    }
                    result
                }

                // 2. try to retrieve album information until succeed
                var target_id: Long = 0
                var trialCount = 10
                var album_info: AlbumDetail? = null

                while (--trialCount > 0)    {
                    val cand_id: Long = Math.floor(Math.random() * latest_album_id).toLong()

                    album_info = Server().requestAlbumDetail(cand_id, UserInfo.getUserIdOr(context))
                    if (album_info.albumId > 0)  {
                        target_id = cand_id
                        break
                    }
                }

                val purchased = RequestAlbumPurchased(target_id, UserInfo.getUserIdOr(context), false).execute()

                // 3. open album info activity
                uiThread {
                    snackbar_instance.dismiss()
                    if (target_id < 1)  {
                        Snackbar.make(view, "Failed to open random album...try again later!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                    }   else    {
                        val activity = activity
                        val album_info = album_info
                        if (activity is MainActivity && album_info != null) {
                            val entry = convertToAlbumEntry(album_info, if (purchased.notBought) 0 else 1)
                            val albumInfoFragment = AlbumInfoFragment.newInstance(entry)
                            activity.transitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG)
                        }
                    }
                }
            }
        }

        // link toolbar with drawer in activity
        if (activity is MainActivity && toolbar != null)
            (activity as MainActivity).linkDrawerToToolbar(toolbar)

        if (savedInstanceState == null) {

        }

        return view
    }

    override fun onResume() {
        super.onResume()

    }

    fun onBackPressed(): Boolean    {
        if (childFragmentManager.backStackEntryCount >= 1) {
            childFragmentManager.popBackStack()
            return true
        }
        return false
    }

    fun changeChildFragment(targetFragment: Fragment, fragmentTAG: String, backStack: Boolean = false)   {
        Log.d("Test", getActiveChildFragment()?.tag.toString())

        val currentChildFragmentTag = getActiveChildFragment()?.tag
        if (currentChildFragmentTag != null && currentChildFragmentTag.equals(targetFragment.tag)) {

        }   else    {
            Log.d("Test", targetFragment.toString())

            val transaction = childFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.replace(R.id.content_main, targetFragment, fragmentTAG)
            if (backStack)
                transaction.addToBackStack(fragmentTAG)
            transaction.commit()
        }
    }

    fun getActiveChildFragment(): Fragment? {
        if (childFragmentManager.backStackEntryCount === 0) {
            return childFragmentManager?.findFragmentByTag(HomeFragment.TAG)
        }

        val tag = childFragmentManager.getBackStackEntryAt(childFragmentManager.backStackEntryCount - 1).name
        return childFragmentManager.findFragmentByTag(tag)
    }

    companion object {
        val TAG = MainFragment::class.java.simpleName

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}