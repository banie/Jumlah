package com.banuu.android.jumlah.input.view

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.banuu.android.jumlah.R
import com.banuu.android.jumlah.extensions.KeyboardUtil
import io.reactivex.Emitter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.input_money_container.*

/**
 * Created by banie on 2017-12-09.
 */

class InputFragment : Fragment() {

  lateinit var cashInputStream: Observable<List<Pair<String, Double>>>
  private lateinit var cashInputArray: Array<View>
  private lateinit var cashInputEmitter: Emitter<List<Pair<String, Double>>>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View = inflater.inflate(
      R.layout.input_money_container, container, false)

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)

    cashInputArray = arrayOf(
        cash_input1, cash_input2, cash_input3, cash_input4, cash_input5,
        cash_input6, cash_input7, cash_input8, cash_input9, cash_input10,
        cash_input11)

    for (i in cashInputArray.indices) {
      val button = cashInputArray[i].findViewById<Button>(R.id.input_btn)
      when (i) {
        0 -> button.text = "$100"
        1 -> button.text = "$50"
        2 -> button.text = "$20"
        3 -> button.text = "$10"
        4 -> button.text = "$5"
        5 -> button.text = "$2"
        6 -> button.text = "$1"
        7 -> button.text = "25 ¢"
        8 -> button.text = "10 ¢"
        9 -> button.text = "5 ¢"
        10 -> button.text = "1 ¢"
      }

      val editText = cashInputArray[i].findViewById<EditText>(R.id.input_text)
      editText.setOnFocusChangeListener { view, hasFocus ->
        if (view == editText) {
          updateEditTextWidth(editText)
        }
      }

      // return the text back if there was some from the saved instance state
      editText.setText(savedInstanceState?.getString(Integer.toString(i), ""))
      updateEditTextWidth(editText)

      // clear focus just in case so there is no button open
      editText.clearFocus()

      // if the button is clicked then the corresponding edittext must have focus
      button.setOnClickListener {
        editText.requestFocus()
        KeyboardUtil.showKeyboard(editText)
      }

      if (i == 0) {
        cashInputStream = makeCashObservable(editText)
      } else {
        cashInputStream = Observable.merge(cashInputStream, makeCashObservable(editText))
      }
    }

    // to make sure none of the cash input has input at this point
    hideKeyboardAndFocus()

    input_container.setOnTouchListener { view, motionEvent ->
      when (motionEvent.getAction()) {
        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
          hideKeyboardAndFocus()
        }
      }

      true
    }
  }

  override fun onResume() {
    super.onResume()

    cashInputEmitter.onNext(composeValueList())
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    for (i in cashInputArray.indices) {
      val editText = cashInputArray[i].findViewById<EditText>(R.id.input_text)

      if (editText.text.isNotEmpty()) {
        outState?.putString(Integer.toString(i), editText.text.toString())
      }
    }

    super.onSaveInstanceState(outState)
  }

  private fun updateEditTextWidth(editText: EditText) {
    val hasFocus = editText.hasFocus()
    val layoutParams = editText.layoutParams
    editText.isCursorVisible = hasFocus

    layoutParams.width = activity.resources.getDimensionPixelSize(
        if (hasFocus || editText.text.isNotEmpty())
          R.dimen.money_input_width_open else R.dimen.money_input_width_closed)
    editText.layoutParams = layoutParams
  }

  private fun hideKeyboardAndFocus() {
    KeyboardUtil.hideKeyboard(input_container)
    input_container.requestFocus()
  }

  private fun makeCashObservable(editText: EditText): Observable<List<Pair<String, Double>>> {
    return Observable.create<List<Pair<String, Double>>> { emitter ->

      editText.onTextChanged {
        val pairList = composeValueList()
        emitter.onNext(pairList)
      }

      emitter.setCancellable {
        editText.setOnClickListener(null)
      }

      cashInputEmitter = emitter
    }
  }

  private fun composeValueList(): List<Pair<String, Double>> {
    val pairList = mutableListOf<Pair<String, Double>>()
    for (i in cashInputArray.indices) {
      val cashNum = cashInputArray[i].findViewById<EditText>(R.id.input_text).text
      val cashInt = if (cashNum.isNotEmpty()) cashNum.toString().toInt() else 0
      if (cashInt > 0) {
        var pair: Pair<String, Double>
        when (i) {
          0 -> pair = Pair("$100 x " + cashNum, cashInt * 100.0)
          1 -> pair = Pair("$50 x " + cashNum, cashInt * 50.0)
          2 -> pair = Pair("$20 x " + cashNum, cashInt * 20.0)
          3 -> pair = Pair("$10 x " + cashNum, cashInt * 10.0)
          4 -> pair = Pair("$5 x " + cashNum, cashInt * 5.0)
          5 -> pair = Pair("$2 x " + cashNum, cashInt * 2.0)
          6 -> pair = Pair("$1 x " + cashNum, cashInt * 1.0)
          7 -> pair = Pair("25¢ x " + cashNum, cashInt * 0.25)
          8 -> pair = Pair("10¢ x " + cashNum, cashInt * 0.1)
          9 -> pair = Pair("5¢ x " + cashNum, cashInt * 0.05)
          10 -> pair = Pair("1¢ x " + cashNum, cashInt * 0.01)
          else -> pair = Pair("0 x 0", 0.0)
        }
        pairList.add(pair)
      }
    }

    return pairList
  }
}

fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
  this.addTextChangedListener(
      object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
          onTextChanged.invoke(s.toString())
        }
      })
}


