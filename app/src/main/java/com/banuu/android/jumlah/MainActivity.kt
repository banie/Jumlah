package com.banuu.android.jumlah

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.banuu.android.jumlah.extensions.shareFile
import com.banuu.android.jumlah.extensions.shareText
import com.banuu.android.jumlah.input.view.InputFragment
import com.banuu.android.jumlah.util.RecordUtil
import com.getbase.floatingactionbutton.FloatingActionsMenu
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_cordinator.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
  private val decimalFormatter = DecimalFormat("###,###.00")
  private var inputStreamDisposable: Disposable? = null
  private var totalSum = 0.0
  private var totalLabel = ""
  private var fabMenuIsShown = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_cordinator)
    setSupportActionBar(toolbar)
    setFabBehaviors()

    supportActionBar?.setDisplayShowTitleEnabled(false)
  }

  override fun onStart() {
    super.onStart()

    val inputFragment = getInputFragment()
    if (inputFragment is InputFragment) {
      inputStreamDisposable = inputFragment.cashInputStream
          .observeOn(Schedulers.computation())
          .map { computeSum(it) }
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe {
            sum_breakdown.text = totalLabel
            sum_label.text = "$ " + formatTotalSum()
          }
    }
  }

  override fun onStop() {
    super.onStop()
    inputStreamDisposable?.let { if (it.isDisposed) it.dispose() }
  }

  override fun onBackPressed() {
    if (fabMenuIsShown) {
      fab_menu.collapse()
    } else {
      super.onBackPressed()
    }
  }

  private fun setFabBehaviors() {
    fab_menu.setOnFloatingActionsMenuUpdateListener(
        object : FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
          override fun onMenuExpanded() {
            onFabMenuOpened(true)
          }

          override fun onMenuCollapsed() {
            onFabMenuOpened(false)
          }
        })

    //    fab_menu.expand()
    //    fab_menu.collapse()

    fab_total.setOnClickListener { _ ->
      sendShareTotalIntent()
    }

    fab_txt.setOnClickListener { _ ->
      sendShareTextIntent()
    }

    fab_csv.setOnClickListener { _ ->
      sendShareCsvFileIntent()
    }

    input_disabler.setOnClickListener { _ ->
      if (fabMenuIsShown) {
        fab_menu.collapse()
      }
    }
  }

  private fun onFabMenuOpened(opened: Boolean) {
    fabMenuIsShown = opened
    input_disabler.visibility = if (opened) View.VISIBLE else View.GONE
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

    val inputFragment = getInputFragment()
    if (inputFragment is InputFragment) {
      result = inputFragment.makeCsvData()
    }

    return result
  }

  private fun getInputFragment(): Fragment? {
    return fragmentManager.findFragmentById(R.id.input_fragment)
  }

  private fun sendShareTotalIntent() {
    val shareIntent = Intent(Intent.ACTION_SEND)

    shareIntent.shareText(this, "$ " + formatTotalSum())
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

  private fun formatTotalSum(): String {
    return decimalFormatter.format(totalSum)
  }
}
