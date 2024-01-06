package ashvayu.mandir.alle.utils

import android.view.View

fun View.hideView() {
  visibility = View.GONE
}

fun View.showView() {
  visibility = View.VISIBLE
}

inline fun <T, R> List<T>?.isListNotNullOrEmpty(block: (List<T>) -> R, elseBlock: () -> R) {
  if (this?.isEmpty() == false) {
    block(this)
  } else {
    elseBlock()
  }
}