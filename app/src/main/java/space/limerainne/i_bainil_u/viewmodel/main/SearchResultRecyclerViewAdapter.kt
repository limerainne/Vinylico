package space.limerainne.i_bainil_u.viewmodel.main

import android.content.Context
import android.preference.PreferenceActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.domain.model.*
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
        rangesList.add(ItemIndices(HEADER_ARTIST, if (hasArtist) 0 else -1, if (hasArtist) 0 else -1))
        rangesList.add(ItemIndices(ITEM_ARTIST, rangesList[-1].to + 1, rangesList[-1].to + mResult.artists.size))

        rangesList.add(ItemIndices(HEADER_ALBUM, rangesList[-1].to + if (hasAlbum) 1 else 0, rangesList[-1].to + if (hasAlbum) 1 else 0))
        rangesList.add(ItemIndices(ITEM_ALBUM, rangesList[-1].to + 1, rangesList[-1].to + mResult.albums.size))

        rangesList.add(ItemIndices(HEADER_TRACK, rangesList[-1].to + if (hasTrack) 1 else 0, rangesList[-1].to + if (hasTrack) 1 else 0))
        rangesList.add(ItemIndices(ITEM_TRACK, rangesList[-1].to + 1, rangesList[-1].to + mResult.tracks.size))
    }

    override fun getItemCount(): Int    {
        var count = 0

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
            HEADER_ARTIST, HEADER_ALBUM, HEADER_TRACK ->
                    viewHolder = HeaderViewHolder(viewInflater(R.layout.view_search_header))
            ITEM_ARTIST ->
                    viewHolder = ArtistViewHolder(viewInflater(R.layout.view_search_artist))
            ITEM_ALBUM ->
                    viewHolder = ArtistViewHolder(viewInflater(R.layout.view_search_album))
            ITEM_TRACK ->
                    viewHolder = ArtistViewHolder(viewInflater(R.layout.view_search_track))
            else ->
                    throw RuntimeException()
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            HEADER_ARTIST, HEADER_ALBUM, HEADER_TRACK -> {
                if (holder !is HeaderViewHolder)
                    throw RuntimeException()
                when (viewType) {
                    HEADER_ARTIST ->
                            holder.bind("ARTIST", "")
                    HEADER_ALBUM ->
                            holder.bind("ALBUM", "")
                    HEADER_TRACK ->
                            holder.bind("TRACK", "")
                }
            }
            ITEM_ARTIST ->  {
                if (holder !is ArtistViewHolder)
                    throw RuntimeException()

                holder.bind(mResult.artists[getItemIndex(viewType, position)])
            }
            ITEM_ALBUM -> {
                if (holder !is AlbumViewHolder)
                    throw RuntimeException()

                holder.bind(mResult.albums[getItemIndex(viewType, position)])
            }
            ITEM_TRACK -> {
                if (holder !is TrackViewHolder)
                    throw RuntimeException()

                holder.bind(mResult.tracks[getItemIndex(viewType, position)])
            }
            else ->
                throw RuntimeException()
        }
    }

    companion object    {
        val HEADER_ARTIST = 0
        val ITEM_ARTIST = 1
        val HEADER_ALBUM = 2
        val ITEM_ALBUM = 3
        val HEADER_TRACK = 4
        val ITEM_TRACK = 5
    }

    inner abstract class ViewHolder(open val mView: View): RecyclerView.ViewHolder(mView)

    inner class HeaderViewHolder(override val mView: View): ViewHolder(mView)   {

        fun bind(title: String, subTitle: String)   {

        }
    }

    inner class ArtistViewHolder(override val mView: View): ViewHolder(mView)   {
        lateinit var mItem: SearchArtist

        fun bind(item: SearchArtist)   {

        }
    }

    inner class AlbumViewHolder(override val mView: View): ViewHolder(mView)   {
        lateinit var mItem: SearchAlbum

        fun bind(item: SearchAlbum)   {

        }
    }

    inner class TrackViewHolder(override val mView: View): ViewHolder(mView)   {
        lateinit var mItem: SearchTrack

        fun bind(item: SearchTrack)   {

        }
    }
}