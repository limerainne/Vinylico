package win.limerainne.vinylico.viewmodel.main

import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_browse_item_album.view.*
import win.limerainne.vinylico.R
import win.limerainne.vinylico.base.OnListFragmentInteractionListener
import win.limerainne.vinylico.data.api.request.RequestToggleWish
import win.limerainne.vinylico.domain.model.AlbumEntry
import win.limerainne.vinylico.domain.model.SearchArtist
import win.limerainne.vinylico.toolbox.DownloadTool
import win.limerainne.vinylico.toolbox.PurchaseTool
import win.limerainne.vinylico.toolbox.ShareTool

/**
 * Created by CottonCandy on 2016-10-03.
 */

open class BrowserListItemViewHolder(val mContext: Context, val mView: View, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.ViewHolder(mView) {
//        @BindView(R.id.album_id)
//        lateinit var mIdView: TextView
//        @BindView(R.id.content)
//        lateinit var mContentView: TextView
//        @BindView(R.id.album_cover)
//        lateinit var mCoverView: ImageView

//    @BindView(R.id.album_cover)
//    lateinit var mCoverView: ImageView
//
//    @BindView(R.id.album_artist)
//    lateinit var mArtistView: TextView
//    @BindView(R.id.album_num_tracks)
//    lateinit var mNumTracksView: TextView
//    @BindView(R.id.album_date)
//    lateinit var mDateView: TextView
//
//    @BindView(R.id.content)
//    lateinit var mContentView: TextView
//
//    @BindView(R.id.btn_album_wish)
//    lateinit var mButtonAlbumWish: AppCompatButton

    var mItem: AlbumEntry? = null
    val tintColor: Int

    init {
//        ButterKnife.bind(this, mView)

//        println(mContext)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            tintColor = mContext.resources.getColor(R.color.colorAccent)
        else
            tintColor = mContext.getColor(R.color.colorAccent)
    }

    open fun bind(item: AlbumEntry) {
        mItem = item

//        Log.d("Picasso", item.jacketImage)
        Picasso.with(itemView.context).load(item.jacketImage).into(itemView.album_cover)

        // 1st line
        itemView.album_artist.text = item.artistName
        itemView.album_num_tracks.text = item.tracks.toString()
        itemView.album_date.text = item.releaseDate

        itemView.album_artist.setOnClickListener {
            mListener?.onListFragmentInteraction(SearchArtist(
                    "",
                    item.albumName,
                    item.albumId,
                    item.artistId,
                    item.artistName,
                    listOf()
            ))
        }

        // 2nd line
        itemView.album_title.text = item.albumName

        // 3rd line
        setVisibility(itemView.feature_booklet, item.feature_booklet)
        setVisibility(itemView.feature_lyrics, item.feature_lyrics)
        setVisibility(itemView.feature_record, item.feature_rec)
        setVisibility(itemView.feature_event, item.event)

        setPriceButton(itemView.album_price, item.price, item.purchased, item.free)

        itemView.btn_album_wish.setOnClickListener {
            RequestToggleWish.doWishTo(mContext, item.albumId, true)
        }

        itemView.btn_album_share.setOnClickListener {
            ShareTool.shareAlbumWithHQImage(mContext, item.jacketImage, item.albumName, item.artistName, item.albumId)
        }

        itemView.album_price.setOnClickListener {
            if (item.purchased == 1)    {
                DownloadTool.downloadAlbum(item.albumId, mContext)
            }   else
                PurchaseTool.purchaseAlbum(mContext, item)   {
                    item.purchased = 1

                    this.bind(item)
                }
        }
    }

    fun setVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setPriceButton(view: AppCompatButton, price: String, purchased: Int, free: Boolean) {
        // set price
        if (price.contains("."))
            view.text = "$ $price"
        else
            view.text = price

        // if purchased or free, add strike to text
        if (purchased == 1 || free)
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
            // btnDrawable = mContext.resources.getDrawable(btnResId)
            btnDrawable = AppCompatResources.getDrawable(mContext, btnResId)
        else
            btnDrawable = mContext.getDrawable(btnResId)

        val drawables = view.compoundDrawables
        val leftCompoundDrawable = drawables[0]
        btnDrawable?.bounds = leftCompoundDrawable.bounds
        // NOTE above line MUST be REQUIRED to display properly!

        // http://stackoverflow.com/questions/1309629/how-to-change-colors-of-a-drawable-in-android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            btnDrawable?.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY)
        else
            btnDrawable?.setTint(tintColor)

        view.setCompoundDrawables(btnDrawable, null, null, null)
    }

    override fun toString(): String {
        return super.toString() + " '" + itemView.album_title.text + "'"
    }
}

// NOTE http://stackoverflow.com/questions/34697222/how-are-overridden-properties-handled-in-init-blocks
// arguments class constructor does not have to be declared as val/var!
class WishlistItemViewHolder(mContext: Context, mView: View, private val mListener: OnListFragmentInteractionListener?): BrowserListItemViewHolder(mContext, mView, mListener)    {
    override fun bind(item: AlbumEntry) {
        super.bind(item)

        changeButtonDrawableLeft(R.drawable.ic_love_empty, tintColor, itemView.btn_album_wish)

        itemView.btn_album_wish.setOnClickListener {
            RequestToggleWish.doWishTo(mContext, item.albumId, false)
        }
    }

    fun changeButtonDrawableLeft(btnResId: Int, tintColor: Int, view: Button) {
        val btnDrawable: Drawable?
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            // btnDrawable = mContext.resources.getDrawable(btnResId)
            btnDrawable = AppCompatResources.getDrawable(mContext, btnResId)
        else
            btnDrawable = mContext.getDrawable(btnResId)

        val drawables = view.compoundDrawables
        val leftCompoundDrawable = drawables[0]
        btnDrawable?.bounds = leftCompoundDrawable.bounds
        // NOTE above line MUST be REQUIRED to display properly!

        // http://stackoverflow.com/questions/1309629/how-to-change-colors-of-a-drawable-in-android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            btnDrawable?.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY)
        else
            btnDrawable?.setTint(tintColor)

        view.setCompoundDrawables(btnDrawable, null, null, null)
    }
}

class PurchasedItemViewHolder(mContext: Context, mView: View, private val mListener: OnListFragmentInteractionListener?): BrowserListItemViewHolder(mContext, mView, mListener)    {
    override fun bind(item: AlbumEntry)  {
        super.bind(item)

        itemView.album_price.text = item.purchasedDate
        item.purchased = 1
    }

    override fun setPriceButton(view: AppCompatButton, price: String, purchased: Int, free: Boolean) {
        super.setPriceButton(view, price, purchased, free)

        // TODO because this item is in purchased page...
        view.paintFlags = view.paintFlags xor (view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG)
    }
}