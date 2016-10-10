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
import com.ms.square.android.expandabletextview.ExpandableTextView
import kotlinx.android.synthetic.main.view_album_info_album_desc.view.*
import kotlinx.android.synthetic.main.view_album_info_album_summary.view.*
import kotlinx.android.synthetic.main.view_album_info_track.view.*
import org.jetbrains.anko.toast
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.base.PurchaseTool
import space.limerainne.i_bainil_u.base.ShareTool
import space.limerainne.i_bainil_u.data.api.RequestToggleWish
import space.limerainne.i_bainil_u.domain.model.AlbumDetail
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.domain.model.Track
import space.limerainne.i_bainil_u.domain.model.TrackList
import space.limerainne.i_bainil_u.extension.*

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
            ITEM_ALBUM_SUMMARY ->  {
                viewHolder = AlbumSummaryViewHolder(viewInflater(R.layout.view_album_info_album_summary))
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
            0 -> return ITEM_ALBUM_SUMMARY
            in 1..mTracks.tracks.size -> return ITEM_TRACK
            mTracks.tracks.size+1 -> return ITEM_ALBUM_DESC
            mTracks.tracks.size+2 -> return ITEM_ALBUM_CREDIT
            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // check invalidness & bind
        when (getItemViewType(position))    {
            ITEM_ALBUM_SUMMARY ->  {
                if (holder is AlbumSummaryViewHolder) {
                    holder.bind(mAlbum, mTracks)
                    holder.mView.setOnClickListener {
                        mListener?.onListFragmentInteraction(holder.mItem)
                    }
                }
                else
                    throw RuntimeException()
            }
            ITEM_TRACK ->   {
                if (holder is TrackViewHolder) {
                    holder.bind(mTracks.tracks.get(globalPosToTracksPos(position)))
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
//                    holder.mView.setOnClickListener {
//                        mListener?.onListFragmentInteraction(holder.mItem)
//                    }
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

    fun globalPosToTracksPos(globalPosition: Int) = globalPosition - 1

    inner abstract class ViewHolder(open val mView: View): RecyclerView.ViewHolder(mView)

    inner class AlbumSummaryViewHolder(override val mView: View): ViewHolder(mView) {

        lateinit var mItem: AlbumDetail

        val tintColor: Int

        init {
            ButterKnife.bind(this, itemView)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                tintColor = mContext.resources.getColor(R.color.colorAccent)
            else
                tintColor = mContext.getColor(R.color.colorAccent)
        }

        fun bind(item: AlbumDetail, tracks: TrackList)   {
            mItem = item

            mView.album_artist.text = item.artistName
            mView.album_title.text = item.albumName

            mView.album_label_publisher.text = "${item.labelName} | ${item.publishName}"

            var iconCount = 0
            iconCount += mView.feature_album_booklet.setVisibility4(item.feature_booklet)
            iconCount += mView.feature_album_lyrics.setVisibility4(item.feature_lyrics)
            iconCount += mView.feature_album_record.setVisibility4(item.feature_rec)
            mView.feature_album_container.setVisibility4(iconCount > 0)

            mView.album_num_tracks.text = item.tracks.toString()
            mView.album_date.text = item.releaseDate

            // get duration
            mView.album_duration.text = tracks.duration.toDurationText()

            // get bitrate
            mView.album_bitrate.text = tracks.bitrate.toBitrateText()

            // get album size
            mView.album_size.text = tracks.albumSize.toSizeText()

            // TODO wish/trackLove count
            // TODO trackLove count API?
            mView.album_wish_count.text = item.wishCount.thirdCommaFormat()

            // TODO buy count
            // TODO is that right?
            mView.album_buy_count.text = item.fans.thirdCommaFormat()

            // TODO comment count
            // TODO which API?
            mView.album_comment_count.text = "-"

            mView.btn_album_wish.setOnClickListener {
                RequestToggleWish.doWishTo(mContext, item.albumId, true)
            }

            mView.btn_album_share.setOnClickListener {
                ShareTool.shareAlbumWithImageURL(mContext, item.jacketImage, item.albumName, item.artistName, item.albumId)
            }

            // TODO price
            // get price comparison if required
            var priceString = item.price
            try {
                val pricePerSong = tracks.priceIfPerSong.toDouble()
                val priceAlbum = item.price.toDouble()

                if (Math.abs(pricePerSong - priceAlbum) > .5)    {
                    mView.album_price.text = fromHtml4("\$ ${priceAlbum.format(2)} (<strike>${pricePerSong.format(2)}</strike>)")
                }   else    {
                    mView.album_price.text = if (item.price.contains(".")) "$ " + item.price else item.price
                }
            }   catch (e: Exception)    {
                mView.album_price.text = if (item.price.contains(".")) "$ " + item.price else item.price
            }

            setPriceButton(mView.album_price, mAlbumEntry?.purchased ?: 0)
            mView.album_price.setOnClickListener {
                // TODO implement download function
                PurchaseTool.purchaseAlbum(mContext, item.albumId, item.albumName)
            }
        }

        open fun setPriceButton(view: AppCompatButton, purchased: Int) {
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
            mView.track_duration.text = item.duration.toDurationText()

            // feature icons
            var iconCount = 0
            iconCount += mView.feature_lyrics.setVisibility4(item.feature_lyrics)
            iconCount += mView.feature_record.setVisibility4(item.feature_rec)
            mView.feature_container.setVisibility4(iconCount > 0)

            mView.track_comment_count.text = item.connected_msg.toString()

            // bitrate
            mView.track_bitrate.text = item.bitrate.toBitrateText()

            // size
            mView.track_size.text = item.songSize.toSizeText()

            // TODO button functions
            mView.btn_track_like.setOnClickListener {
                // TODO like to track; what if user not bought this track?
                // RequestToggleLike.doLikeTo(mContext, item.albumId, item.trackId, true)
            }
            mView.btn_track_lyric.setVisibility4(item.feature_lyrics)
            mView.btn_track_lyric.setOnClickListener {
                // TODO show lyrics
            }

            // TODO have to find a way to know if album/individual_song is already purchased
            setPriceButton(mView.song_price, item.price, mAlbumEntry?.purchased ?: 0)
            if (item.saleType == "1")   {
                // NOTE this track cannot be purchased per song!
                mView.song_price.text = "-"
            }
            mView.song_price.setOnClickListener {
                // TODO buy/download each track
                // TODO have to get track purchase URL
                // TODO have to check if it cannot be purchased individually
                mContext.toast("Sorry, per-song purchase/download feature is not yet implemented...")
            }
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
        lateinit var mAlbumDescView: ExpandableTextView

        lateinit var mItem: AlbumDetail

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: AlbumDetail)   {
            mItem = item
            mAlbumDescView.text = item.albumDesc

            mAlbumDescView.setOnExpandStateChangeListener { textView, isExpanded ->
                if (!isExpanded)    {
                    mAlbumDescView.scrollTo(0, 0)
                    textView.setTextIsSelectable(false)
                    textView.isClickable = true
                }   else    {
                    textView.setTextIsSelectable(true)
                    textView.isClickable = true
                    textView.requestFocus()
                }
            }

            mView.expandable_text.setOnClickListener { v ->
                // TODO if text selected, ignore event
                (v.parent as ExpandableTextView).onClick(mAlbumDescView)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + (mItem.albumId.toString() ?: "") + "'"
        }
    }

    inner class AlbumCreditViewHolder(override val mView: View): ViewHolder(mView) {
        @BindView(R.id.album_desc)
        lateinit var mAlbumDescView: ExpandableTextView

        lateinit var mItem: AlbumDetail

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: AlbumDetail)   {
            mItem = item

            mView.album_desc_title.text = "Album Credits"
            mAlbumDescView.text = item.albumCredit

            mAlbumDescView.setOnExpandStateChangeListener { textView, isExpanded ->
                if (!isExpanded)    {
                    mAlbumDescView.scrollTo(0, 0)
                    textView.setTextIsSelectable(false)
                    textView.isClickable = true
                }   else    {
                    textView.setTextIsSelectable(true)
                    textView.isClickable = true
                    textView.requestFocus()
                }
            }

            mView.expandable_text.setOnClickListener { v ->
                // TODO if text selected, ignore event
                (v.parent as ExpandableTextView).onClick(mAlbumDescView)
            }

        }

        override fun toString(): String {
            return super.toString() + " '" + (mItem.albumId.toString() ?: "") + "'"
        }
    }

    companion object    {
        val ITEM_ALBUM_SUMMARY = 0
        val ITEM_TRACK = 1
        val ITEM_ALBUM_DESC = 2
        val ITEM_ALBUM_CREDIT = 3
    }
}