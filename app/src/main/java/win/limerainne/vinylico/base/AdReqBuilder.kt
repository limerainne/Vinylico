package win.limerainne.vinylico.base

import com.google.android.gms.ads.AdRequest
import win.limerainne.vinylico.BuildConfig

/**
 * Created by Limerainne on 2016-11-10.
 */
class AdReqBuilder {
    companion object    {
        fun getAdRequest(): AdRequest  {
            val adRequest: AdRequest = com.google.android.gms.ads.AdRequest.Builder().apply {
                if (BuildConfig.DEBUG)
                    addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            }.build()

            return adRequest
        }
    }
}