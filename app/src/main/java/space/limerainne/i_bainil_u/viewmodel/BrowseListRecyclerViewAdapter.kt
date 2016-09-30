package space.limerainne.i_bainil_u.viewmodel

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_browse_list_item.view.*

import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.domain.model.Connected
import space.limerainne.i_bainil_u.domain.model.StoreAlbums

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class BrowseListRecyclerViewAdapter(private val mValues: StoreAlbums, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<BrowseListRecyclerViewAdapter.ViewHolder>() {

    private val loadMoreWhenRemainingLessThan = 5

    private var endlessScrollListener: EndlessScrollListener? = null
    private var lastPosition = -1

    fun setEndlessScrollListener(endlessScrollListener: EndlessScrollListener)  {
        this.endlessScrollListener = endlessScrollListener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_browse_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mValues.albumEntries[position])

        if (position == getItemCount() - loadMoreWhenRemainingLessThan) {
            endlessScrollListener?.onLoadMore(position);
        }

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }

        startAnimation(holder.mView, position)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
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

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        @BindView(R.id.album_id)
//        lateinit var mIdView: TextView
//        @BindView(R.id.content)
//        lateinit var mContentView: TextView
//        @BindView(R.id.album_cover)
//        lateinit var mCoverView: ImageView

        @BindView(R.id.album_cover)
        lateinit var mCoverView: ImageView

        @BindView(R.id.album_artist)
        lateinit var mArtistView: TextView
        @BindView(R.id.album_num_tracks)
        lateinit var mNumTracksView: TextView
        @BindView(R.id.album_date)
        lateinit var mDateView: TextView

        @BindView(R.id.content)
        lateinit var mContentView: TextView


        var mItem: AlbumEntry? = null

        init {
            ButterKnife.bind(this, mView)
        }

        fun bind(item: AlbumEntry)  {
            mItem = item

            Log.d("Picasso", item.jacketImage)
            Picasso.with(itemView.context).load(item.jacketImage).into(itemView.album_cover)

            itemView.album_artist.text = item.artistName
            itemView.album_num_tracks.text = item.tracks.toString()
            itemView.album_date.text = item.releaseDate

            itemView.content.text = item.albumName
        }

        override fun toString(): String {
            return super.toString() + " '" + itemView.content.text + "'"
        }
    }

    interface EndlessScrollListener {
        // http://jayjaylab.tistory.com/m/31
        fun onLoadMore(position: Int): Boolean

    }

    fun  addItems(sList: StoreAlbums) {
        mValues.albumEntries.addAll(sList.albumEntries)
    }
}
