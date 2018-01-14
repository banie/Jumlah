package com.banuu.android.jumlah

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import com.banuu.android.jumlah.extensions.shareFile
import com.banuu.android.jumlah.extensions.shareText
import com.banuu.android.jumlah.input.view.InputFragment
import com.banuu.android.jumlah.util.RecordUtil
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

    fab.setOnClickListener { view ->
      //      Snackbar.make(view, getRecordText(), Snackbar.LENGTH_LONG).setAction(
      //          "Action", null).show()

      //      sendShareTextIntent()

      sendShareCsvFileIntent()
    }

  }

  override fun onStart() {
    super.onStart()

    val inputFragment: Fragment? = fragmentManager.findFragmentById(R.id.input_fragment)
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
    totalLabel = ""
    totalSum = 0.0
    for (cash in cashList) {
      totalLabel += "(" + cash.first + ")" + if (cash != cashList.last()) " + " else ""
      totalSum += cash.second
    }
  }

  private fun getRecordText(): String {
    var result = ""

    val inputFragment: Fragment? = fragmentManager.findFragmentById(R.id.input_fragment)
    if (inputFragment is InputFragment) {
      result = inputFragment.makeCsvData()
    }

    return result
  }

  private fun sendShareTextIntent() {
    val csvText = getRecordText()
    val shareIntent = Intent(Intent.ACTION_SEND)

    shareIntent.shareText(this, csvText)
  }

  private fun sendShareCsvFileIntent() {
    val csvText = getRecordText()
    val csvFileName = RecordUtil.makeCsvFilename()

    // save the file 1st
    val file = RecordUtil.saveRecordToFile(this, csvText, csvFileName)
    val fileUri = FileProvider.getUriForFile(this, "com.banuu.android.jumlah.fileprovider", file)
    val shareIntent = Intent(Intent.ACTION_SEND)

    shareIntent.shareFile(this, fileUri, file.name)
  }
}
