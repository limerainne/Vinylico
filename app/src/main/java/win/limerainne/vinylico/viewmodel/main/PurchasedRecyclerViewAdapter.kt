package win.limerainne.vinylico.viewmodel.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import win.limerainne.vinylico.R
import win.limerainne.vinylico.base.OnListFragmentInteractionListener
import win.limerainne.vinylico.domain.model.Connected

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class PurchasedRecyclerViewAdapter(private val mContext: Context,
                                   private val mValues: Connected,
                                   private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<PurchasedItemViewHolder>() {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasedItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_browse_item_album, parent, false)
        return PurchasedItemViewHolder(mContext, view, mListener)
    }

    override fun onBindViewHolder(holder: PurchasedItemViewHolder, position: Int) {
        holder.bind(mValues.albumEntries[position])

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }

        // startAnimation(holder.mView, position)
    }

    override fun onViewDetachedFromWindow(holder: PurchasedItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.mView.animation?.cancel()
    }

    override fun getItemCount(): Int {
        return mValues.albumEntries.size
    }

    fun startAnimation(view: View, position: Int)   {
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

//    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        @BindView(R.id.album_id)
//        lateinit var mIdView: TextView
//        @BindView(R.id.content)
//        lateinit var mContentView: TextView
//        @BindView(R.id.album_cover)
//        lateinit var mCoverView: ImageView
//
//        var mItem: AlbumEntry? = null
//
//        init {
//            ButterKnife.bind(this, mView)
//        }
//
//        fun bind(item: AlbumEntry)  {
//            mItem = item
//            Log.d("Picasso", item.jacketImage)
//            Picasso.with(itemView.context).load(item.jacketImage).into(itemView.album_cover)
//            itemView.album_id.text = item.albumId.toString()
//            itemView.content.text = item.albumName
//        }
//
//        override fun toString(): String {
//            return super.toString() + " '" + itemView.content.text + "'"
//        }
//    }
}
