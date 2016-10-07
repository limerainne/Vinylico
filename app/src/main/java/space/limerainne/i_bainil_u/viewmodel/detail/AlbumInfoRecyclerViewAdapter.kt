package space.limerainne.i_bainil_u.viewmodel.detail

import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.view_album_info_album_desc.view.*
import kotlinx.android.synthetic.main.view_album_info_track.view.*
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.domain.model.AlbumDetail
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.domain.model.Track
import space.limerainne.i_bainil_u.domain.model.TrackList
import space.limerainne.i_bainil_u.extension.format
import space.limerainne.i_bainil_u.view.main.WishlistFragment
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by Limerainne on 2016-08-16.
 */
class AlbumInfoRecyclerViewAdapter(private val mContext: Context, private val mAlbumEntry: AlbumEntry?, private val mAlbum: AlbumDetail, private val mTracks: TrackList, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<AlbumInfoRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        // NOTE add 2 more rows for...
        // first 1: album information (released date, genre, artist, ...)
        // last 1: album description
        // last 1: album credits
        return 1 + mTracks.tracks.size + 1 + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder: ViewHolder

        val viewInflater: (Int) -> View = { LayoutInflater.from(parent.context).inflate(it, parent, false) }
        when (viewType) {
            ITEM_ALBUM_INFO ->  {
                viewHolder = AlbumInfoViewHolder(viewInflater(R.layout.view_album_info_album_desc))
            }
            ITEM_TRACK -> {
                viewHolder = TrackViewHolder(viewInflater(R.layout.view_album_info_track))
            }
            ITEM_ALBUM_DESC -> {
                viewHolder = AlbumDescViewHolder(viewInflater(R.layout.view_album_info_album_desc))
            }
            ITEM_ALBUM_CREDIT -> {
                viewHolder = AlbumCreditViewHolder(viewInflater(R.layout.view_album_info_album_desc))
            }
            else -> throw RuntimeException()
        }

        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return ITEM_ALBUM_INFO
            in 1..mTracks.tracks.size -> return ITEM_TRACK
            mTracks.tracks.size+1 -> return ITEM_ALBUM_DESC
            mTracks.tracks.size+2 -> return ITEM_ALBUM_CREDIT
            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // check invalidness & bind
        when (getItemViewType(position))    {
            ITEM_ALBUM_INFO ->  {
                if (holder is AlbumInfoViewHolder) {
                    holder.bind(mAlbum)
                    holder.mView.setOnClickListener {
                        mListener?.onListFragmentInteraction(holder.mItem)
                    }
                }
                else
                    throw RuntimeException()
            }
            ITEM_TRACK ->   {
                if (holder is TrackViewHolder) {
                    holder.bind(mTracks.tracks.get(position - 1))   // TODO better way to handle position
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
            ITEM_ALBUM_CREDIT ->  {
            if (holder is AlbumCreditViewHolder) {
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

    inner abstract class ViewHolder(open val mView: View): RecyclerView.ViewHolder(mView)

    inner class AlbumInfoViewHolder(override val mView: View): ViewHolder(mView) {
        @BindView(R.id.album_desc)
        lateinit var mAlbumDescView: TextView

        lateinit var mItem: AlbumDetail

        val tintColor: Int

        init {
            ButterKnife.bind(this, itemView)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                tintColor = mContext.resources.getColor(R.color.colorAccent)
            else
                tintColor = mContext.getColor(R.color.colorAccent)
        }

        fun bind(item: AlbumDetail)   {
            mItem = item
            // TODO currently, do nothing
            // mAlbumDescView.text = item.albumDesc
        }

        override fun toString(): String {
            return super.toString() + " '" + (mItem.albumId.toString() ?: "") + "'"
        }
    }


    inner class TrackViewHolder(override val mView: View): ViewHolder(mView) {
        @BindView(R.id.track_id)
        lateinit var mTrackIdView: TextView
        @BindView(R.id.track_title)
        lateinit var mTrackTitleView: TextView

        lateinit var mItem: Track

        val tintColor: Int

        init {
            ButterKnife.bind(this, itemView)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                tintColor = mContext.resources.getColor(R.color.colorAccent)
            else
                tintColor = mContext.getColor(R.color.colorAccent)
        }

        fun bind(item: Track)   {
            mItem = item

            mTrackIdView.text = item.songOrder.format(2, ' ')
            mTrackTitleView.text = item.songName

            if (mAlbum.artistName != item.artistName)   {
                mView.track_artist_track.visibility = View.VISIBLE
                mView.track_artist_track.text = item.artistName
            }   else
                mView.track_artist_track.visibility = View.INVISIBLE

            // create duration text
            mView.track_duration.text = getDurationText(item.duration)

            // feature icons
            setVisibility(mView.feature_lyrics, item.feature_lyrics)
            setVisibility(mView.feature_record, item.feature_rec)

            // bitrate
            mView.track_bitrate.text = if (item.bitrate.length > 0) (item.bitrate + "k") else ""

            // size
            mView.track_size.text = getSizeText(item.songSize)

            // TODO button functions
            mView.btn_track_like.setOnClickListener {
                // TODO like to track; what if user not bought this track?
                // RequestToggleLike.doLikeTo(mContext, item.albumId, item.trackId, true)
            }
            mView.btn_track_lyric.setOnClickListener {
                // TODO show lyrics
            }

            // TODO have to find a way to know if album/individual_song is already purchased
            setPriceButton(mView.song_price, item.price, mAlbumEntry?.purchased ?: 0)
            mView.song_price.setOnClickListener {
                // TODO buy/download each track
                // TODO have to get track purchase URL
                // TODO have to check if it cannot be purchased individually
            }
        }

        fun setVisibility(view: View, isVisible: Boolean) {
            view.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        fun getDurationText(duration: Int): String  {
            var result = ""

            if (duration < 0)
                return "--:--"

            val exactSecond = duration % 60
            val minute = duration / 60
            val exactMinute = minute % 60
            val hour = minute / 60
            // NOTE ignore if hrs more than 23

            if (hour > 0)
                result = "${hour.format(2)}:"
            result += "${exactMinute.format(2)}:${exactSecond.format(2)}"

            return result
        }

        fun getSizeText(size: Long): String  {
            // input's unit is in byte
            val unit: Double = 1024.0

            // into KiB
            var sizeInto = size / unit
            if (sizeInto <= unit)    {
                return "%.2f KiB".format(sizeInto)
            }

            // into MiB
            sizeInto /= unit
            return "%.2f MiB".format(sizeInto)
        }

        open fun setPriceButton(view: AppCompatButton, price: String, purchased: Int) {
            // set price
            if (price.contains("."))
                view.text = "$ $price"
            else
                view.text = price

            // if purchased, add strike to text
            if (purchased == 1)
                view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                view.paintFlags = view.paintFlags xor (view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG)

            // if purchased, change icon to...
            val btnResId: Int
            if (purchased == 1)
                btnResId = R.drawable.ic_download
            else
                btnResId = R.drawable.ic_buy

            val btnDrawable: Drawable?
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                btnDrawable = mContext.resources.getDrawable(btnResId)
            else
                btnDrawable = mContext.getDrawable(btnResId)

            val drawables = view.compoundDrawables
            val leftCompoundDrawable = drawables[0]
            btnDrawable.bounds = leftCompoundDrawable.bounds
            // NOTE above line MUST be REQUIRED to display properly!

            // http://stackoverflow.com/questions/1309629/how-to-change-colors-of-a-drawable-in-android
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                btnDrawable.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY)
            else
                btnDrawable.setTint(tintColor)

            view.setCompoundDrawables(btnDrawable, null, null, null)
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
            return super.toString() + " '" + (mItem.albumId.toString() ?: "") + "'"
        }
    }

    inner class AlbumCreditViewHolder(override val mView: View): ViewHolder(mView) {
        @BindView(R.id.album_desc)
        lateinit var mAlbumDescView: TextView

        lateinit var mItem: AlbumDetail

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: AlbumDetail)   {
            mItem = item

            mView.album_desc_title.text = "Album Credits"
            mAlbumDescView.text = item.albumCredit
        }

        override fun toString(): String {
            return super.toString() + " '" + (mItem.albumId.toString() ?: "") + "'"
        }
    }

    companion object    {
        val ITEM_ALBUM_INFO = 0
        val ITEM_TRACK = 1
        val ITEM_ALBUM_DESC = 2
        val ITEM_ALBUM_CREDIT = 3
    }
}