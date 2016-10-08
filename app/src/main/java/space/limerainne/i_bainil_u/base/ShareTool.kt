package space.limerainne.i_bainil_u.base

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.jetbrains.anko.toast

/**
 * Created by CottonCandy on 2016-10-03.
 */
class ShareTool {
    companion object {
        fun shareAlbumWithImageView(mContext: Context, view: ImageView, albumName: String, artistName: String, albumId: Long) {
            mContext.toast("Preparing to share with...")
            val mDrawable = view.getDrawable()
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
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Listen \"${albumName}\" by ${artistName} with Bainil!\nhttp://bainil.com/a/${albumId}")
            shareIntent.setType("image/*")

            // Launch sharing dialog for image
            mContext.startActivity(Intent.createChooser(shareIntent, "Invite Bainil with Album to"))
        }

        fun shareAlbumWithImageURL(mContext: Context, url: String, albumName: String, artistName: String, albumId: Long) {
            mContext.toast("Preparing to share with...")

            fun getLocalBitmapUri(bitmap: Bitmap): Uri {
                // TODO request permission to WRITE_EXTERNAL_STORAGE, grantUriPermission()
                val path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                        bitmap, "Bainil Album", null)
                val uri: Uri = Uri.parse(path)
                return uri
            }

            // http://stackoverflow.com/questions/16300959/android-share-image-from-url
            Picasso.with(mContext).load(url).into(object: Target {

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    // Construct a ShareIntent with link to image
                    val shareIntent = Intent()
                    shareIntent.setAction(Intent.ACTION_SEND)
                    shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap))
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Listen \"${albumName}\" by ${artistName} with Bainil!\nhttp://bainil.com/a/${albumId}")
                    shareIntent.setType("image/*")

                    // Launch sharing dialog for image
                    mContext.startActivity(Intent.createChooser(shareIntent, "Invite Bainil with Album to"))
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            });
        }
    }
}