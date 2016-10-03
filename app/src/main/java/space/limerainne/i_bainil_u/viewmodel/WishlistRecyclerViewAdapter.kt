package space.limerainne.i_bainil_u.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_browse_list_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import space.limerainne.i_bainil_u.I_Bainil_UApp

import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.base.UserInfo
import space.limerainne.i_bainil_u.data.api.RequestAlbumPurchased
import space.limerainne.i_bainil_u.data.api.RequestToggleWish
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.domain.model.Wishlist
import space.limerainne.i_bainil_u.view.MainActivity
import space.limerainne.i_bainil_u.view.PurchaseWebviewFragment
import space.limerainne.i_bainil_u.view.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class WishlistRecyclerViewAdapter(private val mContext: Context,
                                  private val mValues: Wishlist,
                                  private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<WishlistRecyclerViewAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_browse_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mValues.albumEntries[position])

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

//    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        @BindView(R.id.album_id)
//        lateinit var mIdView: TextView
//        @BindView(R.id.content)
//        lateinit var mContentView: TextView
//        @BindView(R.id.album_cover)
//        lateinit var mCoverView: ImageView
//
//        var mItem: AlbumEntry? = null
//
//        init {
//            ButterKnife.bind(this, mView)
//        }
//
//        fun bind(item: AlbumEntry)  {
//            mItem = item
//            Log.d("Picasso", item.jacketImage)
//            Picasso.with(itemView.context).load(item.jacketImage).into(itemView.album_cover)
//            itemView.album_id.text = item.albumId.toString()
//            itemView.content.text = item.albumName
//        }
//
//        override fun toString(): String {
//            return super.toString() + " '" + itemView.content.text + "'"
//        }
//    }
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
    val tintColor: Int

    init {
        ButterKnife.bind(this, mView)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            tintColor = mContext.resources.getColor(R.color.colorAccent)
        else
            tintColor = mContext.getColor(R.color.colorAccent)
    }

    fun bind(item: AlbumEntry)  {
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

        changeButtonDrawableLeft(R.drawable.ic_love_empty, tintColor, itemView.btn_album_wish)

        itemView.btn_album_wish.setOnClickListener {
            val connMgr = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            if (networkInfo != null && networkInfo!!.isConnected) {
                doAsync {
                    val success: Boolean
                    try {
                        success = RequestToggleWish(item.albumId, I_Bainil_UApp.USER_ID, false).execute()
                    } catch (e: Exception) {
                        success = false
                        e.printStackTrace()
                    }

                    uiThread {
                        if (success)
                            mContext.toast("Removing album from wishlist succeed!")
                        else
                            mContext.toast("Failed to remove album from wishlist...")
                    }
                }
            } else {
                mContext.toast("Check network connection!")
            }
        }

        itemView.btn_album_share.setOnClickListener {
            mContext.toast("Preparing to share with...")
            val mDrawable = itemView.album_cover.getDrawable()
            val mBitmap = (mDrawable as BitmapDrawable).getBitmap()
            // TODO get better quality album cover!

            // TODO request permission to WRITE_EXTERNAL_STORAGE, grantUriPermission()
            val path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                    mBitmap, "Image Description", null)

            val uri = Uri.parse(path)

            // Construct a ShareIntent with link to image
            val shareIntent = Intent()
            shareIntent.setAction(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Listen \"${item.albumName}\" by ${item.artistName} with Bainil!\nhttp://bainil.com/a/${item.albumId}")
            shareIntent.setType("image/*")

            // Launch sharing dialog for image
            mContext.startActivity(Intent.createChooser(shareIntent, "Invite Bainil with Album to"))
        }

        itemView.album_price.setOnClickListener {
            /*
            http://www.bainil.com/api/v2/purchase/request?userId=2543&albumId=2423&store=1&type=pay

            http://www.bainil.com/api/v2/kakaopay/request?albumId=3276&userId=2543
             */
            val connMgr = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            if (networkInfo != null && networkInfo!!.isConnected) {
                // check login first
                val userInfo = UserInfo(mContext)

                if (userInfo.userId < 1)    {
                    mContext.toast("Please login first!")
                    if (mContext is MainActivity)
                        mContext.openLoginPage()

                    return@setOnClickListener
                }

                doAsync {
                    // 1. check if previously purchased
                    val success: Boolean
                    try {
                        val userInfo = UserInfo(mContext)

                        success = RequestAlbumPurchased(item.albumId, userInfo.userId, true).execute()
                    } catch (e: Exception) {
                        success = false
                        e.printStackTrace()
                    }

                    // 2. redirect to purchase page
                    if (success)    {
                        if (mContext is MainActivity)   {
                            uiThread {
                                // TODO display purchase info & cautions
                                val userInfo = UserInfo(mContext)

                                val webviewFragment = PurchaseWebviewFragment.newInstance(userInfo.userId, item.albumId, mContext)
                                mContext.transitToFragment(R.id.placeholder_top, webviewFragment, PurchaseWebviewFragment.TAG)
                            }
                        }
                    }   else    {
                        uiThread {
                            mContext.toast("Already purchased this album: ${item.albumName}")
                        }
                    }

                    // 3. TODO check if purhcase succeed (when? where?)
                }
            } else {
                mContext.toast("Check network connection!")
            }
        }
    }

    fun setVisibility(view: View, isVisible: Boolean)   {
        if (isVisible)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.GONE
    }

    fun setPriceButton(view: Button, price: String, purchased: Int) {
        // set price
        if (price.contains("."))
            view.text = "$ " + price
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

        changeButtonDrawableLeft(btnResId, tintColor, view)
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

    override fun toString(): String {
        return super.toString() + " '" + itemView.content.text + "'"
    }
}
}
