package space.limerainne.i_bainil_u.viewmodel.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.domain.model.AlbumDetail
import space.limerainne.i_bainil_u.domain.model.Track
import space.limerainne.i_bainil_u.domain.model.TrackList
import space.limerainne.i_bainil_u.view.main.WishlistFragment
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by Limerainne on 2016-08-16.
 */
class AlbumInfoRecyclerViewAdapter(private val mAlbum: AlbumDetail, private val mTracks: TrackList, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<AlbumInfoRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return mTracks.tracks.size + 1  // TODO +1: album description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: ViewHolder

        val viewInflater: (Int) -> View = { LayoutInflater.from(parent.context).inflate(it, parent, false) }
        when (viewType) {
            ITEM_TRACK -> {
                viewHolder = TrackViewHolder(viewInflater(R.layout.view_album_info_track))
            }
            ITEM_ALBUM_DESC -> {
                viewHolder = AlbumDescViewHolder(viewInflater(R.layout.view_album_info_album_desc))
            }
            else -> throw RuntimeException()
        }

        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            in 0..mTracks.tracks.size-1 -> return ITEM_TRACK
            mTracks.tracks.size -> return ITEM_ALBUM_DESC
            else -> throw RuntimeException()
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // check invalidness & bind
        when (getItemViewType(position))    {
            ITEM_TRACK ->   {
                if (holder is TrackViewHolder) {
                    holder.bind(mTracks.tracks.get(position))
                    holder.mView.setOnClickListener {
                        mListener?.onListFragmentInteraction(holder.mItem)
                    }
                }
                else
                    throw RuntimeException()
            }
            ITEM_ALBUM_DESC ->  {
                if (holder is AlbumDescViewHolder) {
                    holder.bind(mAlbum)
                    holder.mView.setOnClickListener {
                        mListener?.onListFragmentInteraction(holder.mItem)
                    }
                }
                else
                    throw RuntimeException()
            }
        }
    }

    open inner abstract class ViewHolder(open val mView: View): RecyclerView.ViewHolder(mView)

    inner class TrackViewHolder(override val mView: View): ViewHolder(mView) {
        @BindView(R.id.track_id)
        lateinit var mTrackIdView: TextView
        @BindView(R.id.track_title)
        lateinit var mTrackTitleView: TextView

        lateinit var mItem: Track

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: Track)   {
            mItem = item
            mTrackIdView.text = item.songOrder.toString()
            mTrackTitleView.text = item.songName
        }

        override fun toString(): String {
            return super.toString() + " '" + mTrackTitleView.text + "'"
        }
    }

    inner class AlbumDescViewHolder(override val mView: View): ViewHolder(mView) {
        @BindView(R.id.album_desc)
        lateinit var mAlbumDescView: TextView

        lateinit var mItem: AlbumDetail

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: AlbumDetail)   {
            mItem = item
            mAlbumDescView.text = item.albumDesc
        }

        override fun toString(): String {
            return super.toString() + " '" + (mItem?.albumId.toString() ?: "") + "'"
        }
    }

    companion object    {
        val ITEM_TRACK = 1
        val ITEM_ALBUM_DESC = 2
    }
}