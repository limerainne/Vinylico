package win.limerainne.vinylico.toolbox

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import win.limerainne.vinylico.R
import win.limerainne.vinylico.ThisApp
import win.limerainne.vinylico.credential.UserInfo
import win.limerainne.vinylico.data.api.Server

/**
 * Created by CottonCandy on 2016-10-03.
 */
class ShareTool {
    companion object {
        fun shareAlbumWithImageView(mContext: Context, view: ImageView, albumName: String, artistName: String, albumId: Long) {
            if (!checkIfPermission(mContext))   return
            mContext.toast(mContext.getString(R.string.msg_prepare_sharing))

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
            shareIntent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_msg, albumName, artistName, albumId.toString()))
            shareIntent.setType("image/*")

            // Launch sharing dialog for image
            mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.title_share_album)))
        }

        fun shareAlbumWithImageURL(mContext: Context, url: String, albumName: String, artistName: String, albumId: Long) {
            if (!checkIfPermission(mContext))   return
            mContext.toast(mContext.getString(R.string.msg_prepare_sharing))

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
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_msg, albumName, artistName, albumId.toString()))
                    shareIntent.setType("image/*")

                    // Launch sharing dialog for image
                    mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.title_share_album)))
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            });
        }

        fun shareAlbumWithHQImage(mContext: Context, url: String, albumName: String, artistName: String, albumId: Long) {
            if (!checkIfPermission(mContext))   return
            mContext.toast(mContext.getString(R.string.msg_prepare_sharing))

            // request URL in AlbumDetail
            doAsync(ThisApp.ExceptionHandler) {
                val albumDetail = Server().requestAlbumDetail(albumId, UserInfo.getUserIdOr(mContext))
                uiThread {
                    if (albumDetail.jacketImage != "")
                        shareAlbumWithImageURL(mContext, albumDetail.jacketImage, albumName, artistName, albumId)
                    else
                        shareAlbumWithImageURL(mContext, url, albumName, artistName, albumId)
                }
            }
        }


        fun shareArtistWithImageURL(mContext: Context, url: String, artistName: String, artistId: Long) {
            if (!checkIfPermission(mContext))   return
            mContext.toast(mContext.getString(R.string.msg_prepare_sharing))

            fun getLocalBitmapUri(bitmap: Bitmap): Uri {
                // TODO request permission to WRITE_EXTERNAL_STORAGE, grantUriPermission()
                val path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                        bitmap, "Bainil Artist", null)
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
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_artist_msg, artistName))
                    shareIntent.setType("image/*")

                    // Launch sharing dialog for image
                    mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.title_share_album)))
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            });
        }

        fun checkIfPermission(mContext: Context): Boolean {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext as Activity,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                    // Show an expanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//
//                } else {

                // No explanation needed, we can request the permission.
                mContext.toast(mContext.getString(R.string.msg_err_share_requires_permission))

                ActivityCompat.requestPermissions(mContext as Activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        ThisApp.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
//                }

                return false    // permission denied
            }

            return true // permission granted
        }
    }
}