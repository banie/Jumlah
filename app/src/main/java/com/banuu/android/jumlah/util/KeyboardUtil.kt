package com.banuu.android.jumlah.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.banuu.android.jumlah.extensions.hideKeyboard
import com.banuu.android.jumlah.extensions.showKeyboard

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
