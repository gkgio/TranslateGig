package com.gkgio.translate.features.translate.view

import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gkgio.translate.R
import com.gkgio.translate.base.BaseActivity
import com.gkgio.translate.data.model.KeyValueItem
import com.gkgio.translate.features.translate.presenter.TranslatePresenter
import com.gkgio.translate.features.translate.presenter.TranslatePresenterImpl
import com.gkgio.translate.helpers.utils.closeKeyboard
import com.gkgio.translate.helpers.utils.giveHashMapRandomElement
import com.gkgio.translate.helpers.utils.showErrorAlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class TranslateActivity : BaseActivity(), TranslateView {

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

    val translateContainer: LinearLayout = findViewById(R.id.translateContainer)
    translateContainer.setOnClickListener {
      closeKeyboard(this)
    }

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
    val gson = Gson()
    val languagesMapType = object : TypeToken<HashMap<String, String>>() {}.type
    val languagesMap: HashMap<String, String> = gson.fromJson(languagesJsonString, languagesMapType)
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

  private fun getTranslateData(text: String) {
    translatePresenter.fetchData(text,
        String.format("%s-%s", translateFromKeyValue.key,
            translateToKeyValue.key))
  }

  override fun setTranslatedList(textList: List<String>) {
    translateTo.text = textList[0]
  }

  override fun showErrorDialog(erorrMessage: String) {
    showErrorAlertDialog(
        this,
        erorrMessage,
        DialogInterface.OnClickListener { dialogInterface, i ->
          //nothing do
        })
  }

  override fun onStop() {
    super.onStop()
    closeKeyboard(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    translatePresenter.onDestroy()
    compositeDisposable.dispose()
  }

}
