package space.limerainne.i_bainil_u.viewmodel

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_wishlist_item.*
import kotlinx.android.synthetic.main.fragment_wishlist_item.view.*

import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.domain.model.WishAlbum
import space.limerainne.i_bainil_u.domain.model.Wishlist
import space.limerainne.i_bainil_u.view.WishlistFragment.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.view.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class WishlistRecyclerViewAdapter(private val mValues: Wishlist, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<WishlistRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_wishlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mValues.albums[position])

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }
    }

    override fun getItemCount(): Int {
        return mValues.albums.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        @BindView(R.id.album_id)
        lateinit var mIdView: TextView
        @BindView(R.id.content)
        lateinit var mContentView: TextView
        @BindView(R.id.album_cover)
        lateinit var mCoverView: ImageView

        var mItem: WishAlbum? = null

        init {
            ButterKnife.bind(this, mView)
        }

        fun bind(item: WishAlbum)  {
            mItem = item
            Log.d("Picasso", item.jacketImage)
            Picasso.with(itemView.context).load(item.jacketImage).into(itemView.album_cover)
            itemView.album_id.text = item.albumId.toString()
            itemView.content.text = item.albumName
        }

        override fun toString(): String {
            return super.toString() + " '" + itemView.content.text + "'"
        }
    }
}
