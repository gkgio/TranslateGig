package com.gkgio.translate.features.translate.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.gkgio.translate.R
import com.gkgio.translate.base.BaseActivity
import com.gkgio.translate.data.model.KeyValueItem
import com.gkgio.translate.features.translate.presenter.TranslatePresenter
import com.gkgio.translate.features.translate.presenter.TranslatePresenterImpl
import com.gkgio.translate.helpers.utils.*
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class TranslateActivity : BaseActivity(), TranslateView, FullBottomSheetDialogFragment.BottomSheetViewCallback {
  companion object {
    const val ARG_LANGUAGES_AVAILABLE = "LANGUAGES_AVAILABLE"
  }

  override val layoutRes: Int
    get() = R.layout.activity_translate

  private lateinit var tvLanguageFrom: TextView
  private lateinit var tvLanguageTo: TextView
  private lateinit var ivChangeLanguage: ImageView

  private lateinit var translateFrom: EditText
  private lateinit var translateTo: TextView

  private lateinit var translatePresenter: TranslatePresenter

  private lateinit var compositeDisposable: CompositeDisposable

  private var translateFromKeyValue: KeyValueItem = KeyValueItem("", "")
  private var translateToKeyValue: KeyValueItem = KeyValueItem("", "")

  private var languagesMap: HashMap<String, String>? = null

  private fun init() {
    compositeDisposable = CompositeDisposable()
    tvLanguageFrom = findViewById(R.id.tvLanguageFrom)
    tvLanguageTo = findViewById(R.id.tvLanguageTo)
    ivChangeLanguage = findViewById(R.id.ivChangeLanguage)

    translateFrom = findViewById(R.id.translateFrom)
    translateTo = findViewById(R.id.translateTo)

    translatePresenter = TranslatePresenterImpl(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    init()

    ivChangeLanguage.setOnClickListener {
      val tmpTranslateKeyValue = translateFromKeyValue
      translateFromKeyValue = translateToKeyValue
      translateToKeyValue = tmpTranslateKeyValue
      tvLanguageFrom.text = translateFromKeyValue.value
      tvLanguageTo.text = translateToKeyValue.value
      if (!translateFrom.text.isEmpty()) {
        getTranslateData(translateFrom.text.toString())
      }
    }

    val languagesJsonString = intent.getStringExtra(ARG_LANGUAGES_AVAILABLE)
    languagesMap = hashMapFromJson(languagesJsonString)

    val languagesMap = languagesMap
    if (languagesMap != null) {
      val localeLanguage = Locale.getDefault().displayLanguage.substring(0, 2).toLowerCase()
      val firstLanguageTranslate = languagesMap[localeLanguage]
      val iteratorMap = languagesMap.entries.iterator()
      if (firstLanguageTranslate != null) {
        tvLanguageFrom.text = firstLanguageTranslate
        translateFromKeyValue.key = localeLanguage
        translateFromKeyValue.value = firstLanguageTranslate
      } else {
        val entry = iteratorMap.next()
        tvLanguageFrom.text = entry.value
        translateFromKeyValue.key = entry.key
        translateFromKeyValue.value = entry.value
      }

      translateToKeyValue = giveHashMapRandomElement(languagesMap)
      tvLanguageTo.text = translateToKeyValue.value

      compositeDisposable.add(RxTextView.textChanges(translateFrom)
          .debounce(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
          .subscribe { s ->
            if (!s.isEmpty()) {
              getTranslateData(s.toString())
            } else {
              translateTo.text = ""
            }
          })
    }

    tvLanguageFrom.setOnClickListener {
      openLanguageSelectingDialog(true)
    }

    tvLanguageTo.setOnClickListener {
      openLanguageSelectingDialog(false)
    }
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    val view = currentFocus

    if (view is EditText) {
      val focus = currentFocus
      if (focus != null) {
        val scrcoords = IntArray(2)
        focus.getLocationOnScreen(scrcoords)
        val x = event.rawX + focus.left - scrcoords[0]
        val y = event.rawY + focus.top - scrcoords[1]

        if (event.action == MotionEvent.ACTION_UP && (x < focus.left || x >= focus.right
                || y < focus.top || y > focus.bottom)) {
          hideKeyboard()
        }
      }
    }
    return super.dispatchTouchEvent(event)
  }

  private fun openLanguageSelectingDialog(isFromLanguage: Boolean) {
    val languagesMap = languagesMap
    if (languagesMap != null) {
      val listKeyValue: ArrayList<KeyValueItem> = ArrayList()
      languagesMap.forEach { (key, value) ->
        listKeyValue.add(KeyValueItem(key, value))
      }
      val keyValueListString = convertToJson(listKeyValue)
      val fullBottomSheetDialogFragment = FullBottomSheetDialogFragment
          .getInstance(keyValueListString, isFromLanguage, this, supportFragmentManager)
      fullBottomSheetDialogFragment.show(supportFragmentManager, "dialogSheet")
    }
  }

  override fun clickItemSelectLanguage(keyValueItem: KeyValueItem, isFromLanguage: Boolean) {
    if (isFromLanguage) {
      tvLanguageFrom.text = keyValueItem.value
      translateFromKeyValue = keyValueItem
    } else {
      tvLanguageTo.text = keyValueItem.value
      translateToKeyValue = keyValueItem
    }
    if (!translateFrom.text.isEmpty()) {
      getTranslateData(translateFrom.text.toString())
    }
  }

  private fun getTranslateData(text: String) {
    translatePresenter.fetchData(text,
        String.format("%s-%s", translateFromKeyValue.key,
            translateToKeyValue.key))
  }

  override fun setTranslatedList(textList: List<String>) {
    translateTo.text = textList[0]
  }

  override fun showErrorDialog(errorMessage: String) {
    showErrorAlertDialog(
        this,
        errorMessage,
        DialogInterface.OnClickListener { dialogInterface, i ->
          //nothing do
        })
  }

  override fun onStop() {
    super.onStop()
    hideKeyboard()
  }

  override fun onDestroy() {
    super.onDestroy()
    translatePresenter.onDestroy()
    compositeDisposable.dispose()
  }

}
