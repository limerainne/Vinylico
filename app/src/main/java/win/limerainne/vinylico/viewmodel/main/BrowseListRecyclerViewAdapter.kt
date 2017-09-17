package win.limerainne.vinylico.viewmodel.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import win.limerainne.vinylico.R
import win.limerainne.vinylico.base.OnListFragmentInteractionListener
import win.limerainne.vinylico.domain.model.StoreAlbums

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_browse_item_album, parent, false)
        return BrowserListItemViewHolder(mContext, view, mListener)
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
