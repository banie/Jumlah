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
import kotlinx.android.synthetic.main.input_money_container.*

/**
 * Created by banie on 2017-12-09.
 */

class InputFragment : Fragment() {

  var total = 0.0

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View = inflater.inflate(
      R.layout.input_money_container, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val cashList = arrayOf<View>(cash_input1, cash_input2, cash_input3, cash_input4, cash_input5,
                                 cash_input6, cash_input7, cash_input8, cash_input9, cash_input10,
                                 cash_input11)

    for (i in cashList.indices) {
      val button = cashList[i].findViewById<Button>(R.id.input_btn)
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
      val editText = cashList[i].findViewById<EditText>(R.id.input_text)
      editText.onTextChanged {
        total = getSum()
      }
      editText.setOnFocusChangeListener { view, b ->
        if (b) editText.hint = ""
        else editText.hint = "0"
      }
      editText.clearFocus()
    }
  }

  private fun getSum(): Double {
    val hundreds = cash_input1.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 100
    val fifties = cash_input2.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 50
    val twenties = cash_input3.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 20
    val tens = cash_input4.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 10
    val fives = cash_input2.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 5
    val toonies = cash_input2.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 2
    val loonies = cash_input2.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 1
    val quarters = cash_input2.findViewById<EditText>(
        R.id.input_text).text.toString().toInt() * 0.25
    val nickels = cash_input2.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 0.10
    val dimes = cash_input2.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 0.05
    val pennies = cash_input2.findViewById<EditText>(R.id.input_text).text.toString().toInt() * 0.01

    return hundreds + fifties + twenties + tens + fives + toonies + loonies + quarters + nickels + dimes + pennies
  }
}

fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
  this.addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(s: Editable?) = Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
      onTextChanged.invoke(s.toString())
    }
  })
}


