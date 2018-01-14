package com.banuu.android.jumlah.extensions

import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by banie on 2018-01-14.
 */

fun InputMethodManager.showKeyboard() {
  toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun InputMethodManager.hideKeyboard(view: View) {
  hideSoftInputFromWindow(view.windowToken, 0)
}