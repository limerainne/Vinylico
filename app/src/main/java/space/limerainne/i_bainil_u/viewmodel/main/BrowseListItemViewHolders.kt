package space.limerainne.i_bainil_u.viewmodel.main

import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_browse_list_item.view.*
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.PurchaseTool
import space.limerainne.i_bainil_u.base.ShareTool
import space.limerainne.i_bainil_u.data.api.RequestToggleWish
import space.limerainne.i_bainil_u.domain.model.AlbumEntry

/**
 * Created by CottonCandy on 2016-10-03.
 */

open class BrowserListItemViewHolder(val mContext: Context, val mView: View) : RecyclerView.ViewHolder(mView) {
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

        println(mContext)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            tintColor = mContext.resources.getColor(R.color.colorAccent)
        else
            tintColor = mContext.getColor(R.color.colorAccent)
    }

    open fun bind(item: AlbumEntry) {
        mItem = item

        Log.d("Picasso", item.jacketImage)
        Picasso.with(itemView.context).load(item.jacketImage).into(itemView.album_cover)

        // 1st line
        itemView.album_artist.text = item.artistName
        itemView.album_num_tracks.text = item.tracks.toString()
        itemView.album_date.text = item.releaseDate

        // 2nd line
        itemView.content.text = item.albumName

        // 3rd line
        setVisibility(itemView.feature_booklet, item.feature_booklet)
        setVisibility(itemView.feature_lyrics, item.feature_lyrics)
        setVisibility(itemView.feature_record, item.feature_rec)

        setPriceButton(itemView.album_price, item.price, item.purchased)

        itemView.btn_album_wish.setOnClickListener {
            RequestToggleWish.doWishTo(mContext, item.albumId, true)
        }

        itemView.btn_album_share.setOnClickListener {
            ShareTool.shareAlbumWithImageView(mContext, itemView.album_cover, item.albumName, item.artistName, item.albumId)
        }

        itemView.album_price.setOnClickListener {
            // TODO implement download function
            PurchaseTool.purchaseAlbum(mContext, item)
        }
    }

    fun setVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
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
        return super.toString() + " '" + itemView.content.text + "'"
    }
}

// NOTE http://stackoverflow.com/questions/34697222/how-are-overridden-properties-handled-in-init-blocks
// arguments class constructor does not have to be declared as val/var!
class WishlistItemViewHolder(mContext: Context, mView: View): BrowserListItemViewHolder(mContext, mView)    {
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
}

class PurchasedItemViewHolder(mContext: Context, mView: View): BrowserListItemViewHolder(mContext, mView)    {
    override fun bind(item: AlbumEntry)  {
        super.bind(item)

        // TODO because purchased page..
        itemView.album_price.text = item.purchasedDate

        itemView.album_price.setOnClickListener {
            PurchaseTool.purchaseAlbum(mContext, item)
        }
    }

    override fun setPriceButton(view: AppCompatButton, price: String, purchased: Int) {
        super.setPriceButton(view, price, purchased)

        // TODO because this item is in purchased page...
        view.paintFlags = view.paintFlags xor (view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG)
    }
}