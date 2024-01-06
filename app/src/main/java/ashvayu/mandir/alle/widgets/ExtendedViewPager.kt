package ashvayu.mandir.alle.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import ashvayu.mandir.alle.widgets.TouchView

class ExtendedViewPager : ViewPager {
  constructor(context: Context?) : super(context!!)
  constructor(context: Context?, attrs: AttributeSet?) : super(
    context!!, attrs
  )

  override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
    return if (v is TouchView) { //
      // canScrollHorizontally is not supported for Api < 14. To get around this issue,
      // ViewPager is extended and canScrollHorizontallyFroyo, a wrapper around
      // canScrollHorizontally supporting Api >= 8, is called.
      //
      v.canScrollHorizontallyFroyo(-dx)
    } else {
      super.canScroll(v, checkV, dx, x, y)
    }
  }
}