package com.banuu.android.jumlah

import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.banuu.android.jumlah.input.view.InputFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_cordinator.*

class MainActivity : AppCompatActivity() {
  private var inputStreamDisposable: Disposable? = null
  private var totalSum = 0.0
  private var totalLabel = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_cordinator)
    setSupportActionBar(toolbar)

    supportActionBar?.setDisplayShowTitleEnabled(false)

    //    fab.setOnClickListener { view ->
    //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction(
    //          "Action", null).show()
    //    }
  }

  override fun onStart() {
    super.onStart()
    val manager: FragmentManager = fragmentManager
    val inputFragment: Fragment? = manager.findFragmentById(R.id.input_fragment)
    if (inputFragment is InputFragment) {
      inputStreamDisposable = inputFragment.cashInputStream
          .observeOn(Schedulers.computation())
          .map { computeSum(it) }
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe {
            sum_breakdown.text = totalLabel
            sum_label.text = "$ " + totalSum.toString()
          }
    }
  }

  override fun onStop() {
    super.onStop()
    inputStreamDisposable?.let { if (it.isDisposed) it.dispose() }
  }

  private fun computeSum(cashList: List<Pair<String, Double>>) {
    totalSum = 0.0
    for (cash in cashList) {
      totalLabel += "(" + cash.first + ")" + if (cash != cashList.last()) " + " else ""
      totalSum += cash.second
    }
  }
}
