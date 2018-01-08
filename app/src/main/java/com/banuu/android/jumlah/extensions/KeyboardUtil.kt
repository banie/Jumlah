package com.banuu.android.jumlah.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by banie on 2018-01-03.
 */

class KeyboardUtil {
  companion object {
    fun showKeyboard(view: View) {
      val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.showKeyboard()
    }

    fun hideKeyboard(view: View) {
      val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideKeyboard(view)
    }
  }
}

fun InputMethodManager.showKeyboard() {
  toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun InputMethodManager.hideKeyboard(view: View) {
  hideSoftInputFromWindow(view.windowToken, 0)
}