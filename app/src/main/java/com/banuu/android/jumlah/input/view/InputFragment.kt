package com.banuu.android.jumlah.input.view

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.banuu.android.jumlah.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.input_money_container.*

/**
 * Created by banie on 2017-12-09.
 */

class InputFragment : Fragment() {

  public lateinit var cashInputStream: Observable<List<Pair<String, Double>>>
  private lateinit var cashInputArray: Array<View>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View = inflater.inflate(
      R.layout.input_money_container, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    cashInputArray = arrayOf(
        cash_input1, cash_input2, cash_input3, cash_input4, cash_input5,
        cash_input6, cash_input7, cash_input8, cash_input9, cash_input10,
        cash_input11)

    var prevObservable: Observable<List<Pair<String, Double>>>? = null
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
      editText.setOnFocusChangeListener { view, b ->
        if (b) editText.hint = ""
        else editText.hint = "0"
      }
      editText.clearFocus()

      if (i == 0) {
        prevObservable = makeCashObservable(editText)
      } else {
        cashInputStream = Observable.merge(prevObservable, makeCashObservable(editText))
      }
    }
  }

  private fun makeCashObservable(editText: EditText): Observable<List<Pair<String, Double>>> {
    return Observable.create<List<Pair<String, Double>>> { emitter ->

      editText.onTextChanged {
        val pairList = mutableListOf<Pair<String, Double>>()
        for (i in cashInputArray.indices) {
          val cashNum = cashInputArray[i].findViewById<EditText>(R.id.input_text).text
          val cashInt = cashNum.toString().toInt()
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

        if (pairList.isNotEmpty()) {
          emitter.onNext(pairList)
        }
      }

      emitter.setCancellable {
        editText.setOnClickListener(null)
      }
    }
  }

  private fun getSum(): Double {
    val hundreds = cash_input1.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 100
    val fifties = cash_input2.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 50
    val twenties = cash_input3.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 20
    val tens = cash_input4.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 10
    val fives = cash_input5.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 5
    val toonies = cash_input6.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 2
    val loonies = cash_input7.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 1
    val quarters = cash_input8.findViewById<EditText>(
        R.id.input_text).text.toString().toInt() * 0.25
    val nickels = cash_input9.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 0.10
    val dimes = cash_input10.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 0.05
    val pennies = cash_input11.findViewById<EditText>(
        R.id.input_text).text.toString().toInt() * 0.01

    return hundreds + fifties + twenties + tens + fives + toonies + loonies + quarters + nickels + dimes + pennies
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


