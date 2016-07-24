package space.limerainne.i_bainil_u.viewmodel

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_wishlist, parent, false)
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
        val mIdView: TextView
        val mContentView: TextView
        var mItem: WishAlbum? = null

        init {
            mIdView = mView.findViewById(R.id.id) as TextView
            mContentView = mView.findViewById(R.id.content) as TextView
        }

        fun bind(item: WishAlbum)  {
            mItem = item
            mIdView.text = item.albumId.toString()
            mContentView.text = item.albumName
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}