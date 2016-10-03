package space.limerainne.i_bainil_u.viewmodel.main

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_browse_list_item.view.*
import org.jetbrains.anko.custom.style
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp

import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.base.PurchaseTool
import space.limerainne.i_bainil_u.base.ShareTool
import space.limerainne.i_bainil_u.base.UserInfo
import space.limerainne.i_bainil_u.data.api.RequestAlbumPurchased
import space.limerainne.i_bainil_u.data.api.RequestToggleWish
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.domain.model.Connected
import space.limerainne.i_bainil_u.domain.model.StoreAlbums
import space.limerainne.i_bainil_u.view.webview.LoginWebviewFragment
import space.limerainne.i_bainil_u.view.MainActivity
import space.limerainne.i_bainil_u.view.webview.PurchaseWebviewFragment
import space.limerainne.i_bainil_u.view.webview.WebviewFragment
import space.limerainne.i_bainil_u.viewmodel.main.BrowserListItemViewHolder
import java.net.HttpURLConnection
import java.net.URL

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class BrowseListRecyclerViewAdapter(private val mContext: Context,
                                    private val mValues: StoreAlbums,
                                    private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<BrowserListItemViewHolder>() {

    private val loadMoreWhenRemainingLessThan = 5

    private var endlessScrollListener: EndlessScrollListener? = null
    private var lastPosition = -1

    fun setEndlessScrollListener(endlessScrollListener: EndlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowserListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_browse_list_item, parent, false)
        return BrowserListItemViewHolder(mContext, view)
    }

    override fun onBindViewHolder(holder: BrowserListItemViewHolder, position: Int) {
        holder.bind(mValues.albumEntries[position])

        if (position == getItemCount() - loadMoreWhenRemainingLessThan) {
            endlessScrollListener?.onLoadMore(position);
        }

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }

        // startAnimation(holder.mView, position)
    }

    override fun onViewDetachedFromWindow(holder: BrowserListItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.mView.animation?.cancel()
    }

    override fun getItemCount(): Int {
        return mValues.albumEntries.size
    }

    fun startAnimation(view: View, position: Int) {
        // use below library!
        // https://github.com/wasabeef/recyclerview-animators

        // animation code C/Ped from...
        // http://stackoverflow.com/questions/32815155/recyclerview-add-item-animation
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(view.context, R.anim.up_from_bottom)
            view.startAnimation(animation)
            lastPosition = position
        }
    }

    interface EndlessScrollListener {
        // http://jayjaylab.tistory.com/m/31
        fun onLoadMore(position: Int): Boolean

    }

    fun addItems(sList: StoreAlbums) {
        val prevEnd = getItemCount()
        if (sList.albumEntries.size > 0) {
            mValues.albumEntries.addAll(sList.albumEntries)
            notifyItemInserted(prevEnd)
        }
    }
}
