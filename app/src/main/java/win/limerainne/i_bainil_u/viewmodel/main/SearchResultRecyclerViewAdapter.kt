package win.limerainne.i_bainil_u.viewmodel.main

import android.content.Context
import android.preference.PreferenceActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_search_album.view.*
import kotlinx.android.synthetic.main.view_search_artist.view.*
import kotlinx.android.synthetic.main.view_search_header.view.*
import kotlinx.android.synthetic.main.view_search_track.view.*
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import win.limerainne.i_bainil_u.domain.model.*
import win.limerainne.i_bainil_u.extension.setVisibility4
import java.util.*

/**
 * Created by Limerainne on 2016-10-19.
 */
class SearchResultRecyclerViewAdapter(private val mContext: Context,
                                      private val mResult: SearchResult,
                                      private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder>() {

    val hasArtist: Boolean
    val hasAlbum: Boolean
    val hasTrack: Boolean

    data class ItemIndices(val type: Int, val from: Int, val to: Int)
    val rangesList: MutableList<ItemIndices>

    init {
        // get ...
        hasArtist = mResult.artists.size > 0
        hasAlbum = mResult.albums.size > 0
        hasTrack = mResult.tracks.size > 0

        rangesList = mutableListOf()

        rangesList.add(ItemIndices(HEADER_KEYWORD, 0, 0))

        rangesList.add(ItemIndices(HEADER_ARTIST, rangesList.last().to + if (hasArtist) 1 else 0, rangesList.last().to + if (hasArtist) 1 else 0))
        rangesList.add(ItemIndices(ITEM_ARTIST, rangesList.last().to + 1, rangesList.last().to + mResult.artists.size))

        rangesList.add(ItemIndices(HEADER_ALBUM, rangesList.last().to + if (hasAlbum) 1 else 0, rangesList.last().to + if (hasAlbum) 1 else 0))
        rangesList.add(ItemIndices(ITEM_ALBUM, rangesList.last().to + 1, rangesList.last().to + mResult.albums.size))

        rangesList.add(ItemIndices(HEADER_TRACK, rangesList.last().to + if (hasTrack) 1 else 0, rangesList.last().to + if (hasTrack) 1 else 0))
        rangesList.add(ItemIndices(ITEM_TRACK, rangesList.last().to + 1, rangesList.last().to + mResult.tracks.size))
        println(rangesList)
    }

    override fun getItemCount(): Int    {
        var count = 0

        count += 1

        fun countMore(targetList: List<Any>): Int = targetList.size + if (targetList.size > 0) 1 else 0

        count += countMore(mResult.artists)
        count += countMore(mResult.albums)
        count += countMore(mResult.tracks)

        return count
    }

    fun getItemIndex(viewType: Int, position: Int): Int  {
        for (i in 0..rangesList.size)   {
            val range = rangesList[i]
            if (range.type == viewType && range.from <= position && position <= range.to) {
                return position - range.from
            }
        }
        throw Exception()
    }

    override fun getItemViewType(position: Int): Int {
        for (i in 0..rangesList.size)   {
            val range = rangesList[i]
            if (range.from <= position && position <= range.to) {
                return range.type
            }
        }

        throw RuntimeException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder: ViewHolder

        val viewInflater: (Int) -> View = { LayoutInflater.from(parent.context).inflate(it, parent, false) }
        when (viewType) {
            HEADER_KEYWORD, HEADER_ARTIST, HEADER_ALBUM, HEADER_TRACK ->
                    viewHolder = HeaderViewHolder(viewInflater(R.layout.view_search_header))
            ITEM_ARTIST ->
                    viewHolder = ArtistViewHolder(viewInflater(R.layout.view_search_artist))
            ITEM_ALBUM ->
                    viewHolder = AlbumViewHolder(viewInflater(R.layout.view_search_album))
            ITEM_TRACK ->
                    viewHolder = TrackViewHolder(viewInflater(R.layout.view_search_track))
            else ->
                    throw RuntimeException()
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        println("#${position} as ${viewType}: ${holder}")

        when (viewType) {
            HEADER_KEYWORD, HEADER_ARTIST, HEADER_ALBUM, HEADER_TRACK -> {
                if (holder !is HeaderViewHolder)
                    throw RuntimeException()
                when (viewType) {
                    // TODO extract string into resource
                    HEADER_KEYWORD ->
                        holder.bind(mContext.getString(R.string.search_keyword_header, mResult.keyword), mContext.getString(R.string.search_keyword_desc, mResult.artists.size, mResult.albums.size, mResult.tracks.size))
                    HEADER_ARTIST ->
                        holder.bind(mContext.getString(R.string.search_artist_header, mResult.artists.size), mContext.getString(R.string.search_artist_desc))
                    HEADER_ALBUM ->
                        holder.bind(mContext.getString(R.string.search_album_header, mResult.albums.size), mContext.getString(R.string.search_album_desc))
                    HEADER_TRACK ->
                        holder.bind(mContext.getString(R.string.search_track_header, mResult.tracks.size), mContext.getString(R.string.search_track_desc))
                }
            }
            ITEM_ARTIST ->  {
                if (holder !is ArtistViewHolder)
                    throw RuntimeException()

                holder.bind(mResult.artists[getItemIndex(viewType, position)])

                holder.mView.setOnClickListener {
                    mListener?.onListFragmentInteraction(holder.mItem)
                }
            }
            ITEM_ALBUM -> {
                if (holder !is AlbumViewHolder)
                    throw RuntimeException()

                holder.bind(mResult.albums[getItemIndex(viewType, position)])

                holder.mView.setOnClickListener {
                    mListener?.onListFragmentInteraction(holder.mItem)
                }
            }
            ITEM_TRACK -> {
                if (holder !is TrackViewHolder)
                    throw RuntimeException()

                holder.bind(mResult.tracks[getItemIndex(viewType, position)])

                holder.mView.setOnClickListener {
                    mListener?.onListFragmentInteraction(holder.mItem)
                }
            }
            else ->
                throw RuntimeException()
        }
    }

    companion object    {
        val HEADER_KEYWORD = -1
        val HEADER_ARTIST = 0
        val ITEM_ARTIST = 1
        val HEADER_ALBUM = 2
        val ITEM_ALBUM = 3
        val HEADER_TRACK = 4
        val ITEM_TRACK = 5
    }

    inner abstract class ViewHolder(open val mView: View): RecyclerView.ViewHolder(mView)

    inner class HeaderViewHolder(override val mView: View): ViewHolder(mView)   {

        fun bind(title: String, description: String)   {
            mView.header_title.text = title
            mView.header_description.text = description
        }
    }

    inner class ArtistViewHolder(override val mView: View): ViewHolder(mView)   {
        lateinit var mItem: SearchArtist

        fun bind(item: SearchArtist)   {
            mItem = item

            Picasso.with(mView.context).load(item.artistPicture).into(mView.artist_image)

            mView.artist_name.text = item.artistName
            mView.album_name.text = item.albumName

            // TODO buttons/functions
        }
    }

    inner class AlbumViewHolder(override val mView: View): ViewHolder(mView)   {
        lateinit var mItem: SearchAlbum

        fun bind(item: SearchAlbum)   {
            mItem = item

            Picasso.with(mView.context).load(item.jacketImage).into(mView.album_cover)

            mView.album_artist.text = item.artistName
            mView.album_num_tracks.text = item.tracks.toString()
            mView.album_date.text = item.releaseDate

            mView.album_title.text = item.albumName

            // TODO buttons/functions
        }
    }

    inner class TrackViewHolder(override val mView: View): ViewHolder(mView)   {
        lateinit var mItem: SearchTrack

        fun bind(item: SearchTrack)   {
            mItem = item

            mView.track_artist.text = item.artistName

            if (item.songOrder < 1)
                mView.track_id.visibility = View.INVISIBLE
            mView.track_id.text = item.songOrder.toString()
            mView.track_title.text = item.songName

            // TODO buttons/functions
        }
    }
}