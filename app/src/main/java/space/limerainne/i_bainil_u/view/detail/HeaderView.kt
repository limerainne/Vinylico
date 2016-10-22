package space.limerainne.i_bainil_u.view.detail

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import space.limerainne.i_bainil_u.R

/**
 * Copied by CottonCandy on 2016-10-03.

 * https://github.com/harcoPro/SubtitleCoordinatorLayoutExample/
 */

class HeaderView : LinearLayout {

    @BindView(R.id.header_view_title)
    lateinit var title: TextView
    @BindView(R.id.header_view_sub_title)
    lateinit var subTitle: TextView

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this)
    }

    @JvmOverloads fun bindTo(title: String, subTitle: String = "") {
        hideOrSetText(this.title, title)
        hideOrSetText(this.subTitle, subTitle)
    }

    private fun hideOrSetText(tv: TextView?, text: String?) {
        if (text == null || text == "")
            tv?.visibility = View.GONE
        else {
            tv?.visibility = View.VISIBLE
            tv?.text = text
        }
    }

}
