package win.limerainne.vinylico.viewmodel.detail

import android.content.Context
import android.graphics.Paint
import android.opengl.Visibility
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.ms.square.android.expandabletextview.ExpandableTextView
import kotlinx.android.synthetic.main.view_album_info_album_desc.view.*
import kotlinx.android.synthetic.main.view_artist_info_artist_summary.view.*
import kotlinx.android.synthetic.main.view_artist_info_discography_header.view.*
import kotlinx.android.synthetic.main.view_browse_item_album.view.*
import kotlinx.android.synthetic.main.view_search_artist.view.*
import win.limerainne.vinylico.R
import win.limerainne.vinylico.domain.model.ArtistAlbumList
import win.limerainne.vinylico.domain.model.ArtistDetail
import win.limerainne.vinylico.base.OnListFragmentInteractionListener
import win.limerainne.vinylico.domain.model.AlbumEntry
import win.limerainne.vinylico.extension.openURL
import win.limerainne.vinylico.viewmodel.main.BrowserListItemViewHolder
import win.limerainne.vinylico.viewmodel.main.SearchResultRecyclerViewAdapter

/**
 * Created by Limerainne on 2017-09-17.
 */
class ArtistInfoRecyclerViewAdapter(private val mContext: Context, private val mArtistDetail: ArtistDetail, private val mArtistAlbumList: ArtistAlbumList, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val CountItemHeader = 4

    override fun getItemCount(): Int {
        // NOTE add more rows for...
        // very first 1: for smoothAppBarLayout dummy header
        // first 1: artist summary
        // first 1: artist description
        // first 1: artist album header

        val header = CountItemHeader
        val tracks = mArtistAlbumList.albums.size

        return header + tracks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder

        val viewInflater: (Int) -> View = { LayoutInflater.from(parent.context).inflate(it, parent, false) }
        when (viewType) {
            ITEM_DUMMY_HEADER -> {
                viewHolder = DummyHolder(viewInflater(R.layout.view_album_info_dummy_header))
            }
            ITEM_ARTIST_SUMMARY -> {
                viewHolder = ArtistSummaryViewHolder(viewInflater(R.layout.view_artist_info_artist_summary))
            }
            ITEM_ARTIST_DESC -> {
                viewHolder = ArtistDescViewHolder(viewInflater(R.layout.view_album_info_album_desc))
            }
            ITEM_ALBUM_HEADER -> {
                viewHolder = AlbumHeaderViewHolder(viewInflater(R.layout.view_artist_info_discography_header))
            }
            ITEM_ALBUM -> {
                viewHolder = ArtistAlbumViewHolder(viewInflater(R.layout.view_browse_item_album))
            }
            else -> throw RuntimeException()
        }

        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return ITEM_DUMMY_HEADER

            1 -> return ITEM_ARTIST_SUMMARY
            2 -> return ITEM_ARTIST_DESC
            3 -> return ITEM_ALBUM_HEADER

            in 4..(mArtistAlbumList.albums.size+3) -> return ITEM_ALBUM

            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // check invalidness & bind
        when (getItemViewType(position))    {
            ITEM_DUMMY_HEADER -> {
                // do nothing
            }
            ITEM_ARTIST_SUMMARY ->  {
                if (holder is ArtistSummaryViewHolder) {
                    holder.bind(mArtistDetail, mArtistAlbumList)
                    holder.mView.setOnClickListener {
                        mListener?.onListFragmentInteraction(holder.mItem)
                    }
                }
                else
                    throw RuntimeException()
            }
            ITEM_ARTIST_DESC ->  {
                if (holder is ArtistDescViewHolder) {
                    holder.bind(mArtistDetail)
                }
                else
                    throw RuntimeException()
            }
            ITEM_ALBUM_HEADER ->  {
                if (holder is AlbumHeaderViewHolder) {
                    holder.bind(mArtistAlbumList)
                }
                else
                    throw RuntimeException()
            }
            ITEM_ALBUM -> {
                if (holder is ArtistAlbumViewHolder) {
                    holder.bind(mArtistAlbumList.albums.get(globalPosToAlbumsPos(position)))
                    holder.mView.setOnClickListener {
                        holder.mItem?.let {
                            mListener?.onListFragmentInteraction(it)
                        }
                    }
                }
                else
                    throw RuntimeException()
            }
        }
    }

    fun globalPosToAlbumsPos(globalPosition: Int) = globalPosition - CountItemHeader  // header + summary

    inner abstract class ViewHolder(open val mView: View): RecyclerView.ViewHolder(mView)

    inner class DummyHolder(override val mView: View): ViewHolder(mView)   {
        // do nothing
    }

    inner class ArtistSummaryViewHolder(override val mView: View): ViewHolder(mView) {

        lateinit var mItem: ArtistDetail
        lateinit var mAlbums: ArtistAlbumList

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: ArtistDetail, albums: ArtistAlbumList)   {
            mItem = item
            mAlbums = albums

            mView.artist_title.text = item.artistName
            mView.artist_fan_count.text = item.fans.toString()
            mView.artist_label.text = item.labelName
            mView.artist_album_count.text = albums.albums.size.toString()

            treatLinkButton(mView.btn_link_homepage, item.homepage)
            treatLinkButton(mView.btn_link_facebook, item.facebook)
            treatLinkButton(mView.btn_link_twitter, item.twitter)
            treatLinkButton(mView.btn_link_youtube, item.youtube)
        }

        fun treatLinkButton(buttonView: Button, url: String)    {
            with (buttonView)  {
                if (url.isEmpty())    {
                    visibility = View.GONE
                }   else    {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        url.openURL(mContext)
                    }
                }
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + (mItem.artistId.toString() ?: "") + "'"
        }
    }

    inner class ArtistDescViewHolder(override val mView: View): ViewHolder(mView) {
        @BindView(R.id.album_desc)
        lateinit var mAlbumDescView: ExpandableTextView
        @BindView(R.id.expandable_text)
        lateinit var mAlbumDescTextView: TextView

        lateinit var mItem: ArtistDetail

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: ArtistDetail)   {
            mItem = item

            mView.album_desc_title.text = mContext.getString(R.string.artist_info_description)
            mAlbumDescView.text = item.artistDesc

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

            mAlbumDescTextView.setOnClickListener { v ->
                // TODO if text selected, ignore event
                (v as TextView).setTextIsSelectable(false)
                (v.parent as ExpandableTextView).onClick(mAlbumDescView)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + (mItem.artistId.toString() ?: "") + "'"
        }
    }

    inner class AlbumHeaderViewHolder(override val mView: View): ViewHolder(mView) {

        lateinit var mItem: ArtistAlbumList

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: ArtistAlbumList)   {
            mItem = item

            mView.artist_discography_title.text = mContext.getString(R.string.artist_info_discography_title)
            mView.artist_discography_album_count.text = mItem.albums.size.toString()
        }

        override fun toString(): String {
            return super.toString() + " '" + (mItem.albums.toString() ?: "") + "'"
        }
    }

    inner class ArtistAlbumViewHolder(mView: View): BrowserListItemViewHolder(mContext, mView, mListener)    {
        override fun bind(item: AlbumEntry)  {
            super.bind(item)

            // unset OnClickListener of artist name
            mView.album_artist.setOnClickListener(null)
            mView.album_artist.isClickable = false
        }
    }

    companion object    {
        val ITEM_DUMMY_HEADER = -1
        val ITEM_ARTIST_SUMMARY = 0
        val ITEM_ARTIST_DESC = 1
        val ITEM_ALBUM_HEADER = 2
        val ITEM_ALBUM = 3
    }
}