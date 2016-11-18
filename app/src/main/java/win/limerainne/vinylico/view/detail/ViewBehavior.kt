package win.limerainne.vinylico.view.detail

import android.content.Context
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import win.limerainne.vinylico.R

/**
 * Copied by CottonCandy on 2016-10-03.
 */

class ViewBehavior(private val mContext: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<HeaderView>() {

    private var mStartMarginLeft: Int = 0
    private var mEndMargintLeft: Int = 0
    private var mMarginRight: Int = 0
    private var mStartMarginBottom: Int = 0
    private var isHide: Boolean = false

    override fun layoutDependsOn(parent: CoordinatorLayout, child: HeaderView, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: HeaderView, dependency: View): Boolean {
        shouldInitProperties(child, dependency)

        val maxScroll = (dependency as AppBarLayout).getTotalScrollRange()
        val percentage = Math.abs(dependency.getY()) / maxScroll.toFloat()

        var childPosition = dependency.getHeight() + dependency.getY()
        -child.height
        -(toolbarHeight - child.height) * percentage / 2


        childPosition = childPosition - mStartMarginBottom * (1f - percentage)

        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        lp.leftMargin = (percentage * mEndMargintLeft).toInt() + mStartMarginLeft
        lp.rightMargin = mMarginRight
        child.layoutParams = lp

        child.y = childPosition

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (isHide && percentage < 1) {
                child.visibility = View.VISIBLE
                isHide = false
            } else if (!isHide && percentage == 1f) {
                child.visibility = View.GONE
                isHide = true
            }
            child.visibility = View.GONE
        }
        return true
    }

    private fun shouldInitProperties(child: HeaderView, dependency: View) {

        if (mStartMarginLeft == 0)
            mStartMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_start_margin_left)

        if (mEndMargintLeft == 0)
            mEndMargintLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_left)

        if (mStartMarginBottom == 0)
            mStartMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom)

        if (mMarginRight == 0)
            mMarginRight = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_right)

    }


    val toolbarHeight: Int
        get() {
            var result = 0
            val tv = TypedValue()
            if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                result = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics())
            }
            return result
        }

}
