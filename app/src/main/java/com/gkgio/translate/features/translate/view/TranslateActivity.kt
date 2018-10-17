package com.gkgio.translate.features.translate.view

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.gkgio.translate.R
import com.gkgio.translate.base.BaseActivity
import com.gkgio.translate.features.translate.presenter.TranslatePresenter
import com.gkgio.translate.features.translate.presenter.TranslatePresenterImpl
import com.gkgio.translate.helpers.SimpleTextWatcher

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

  private fun init() {
    tvLanguageFrom = findViewById(R.id.tvLanguageFrom)
    tvLanguageTo = findViewById(R.id.tvLanguageTo)
    ivChangeLanguage = findViewById(R.id.ivChangeLanguage)

    translateFrom = findViewById(R.id.translateFrom)
    translateTo = findViewById(R.id.translateTo)

    translatePresenter = TranslatePresenterImpl(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val languagesJsonString = intent.getStringExtra(ARG_LANGUAGES_AVAILABLE)

    init()

    translateFrom.addTextChangedListener(object : SimpleTextWatcher() {
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString().isNotEmpty()) {
          translatePresenter.fetchData(s.toString())
        }
      }
    })
  }

  override fun onDestroy() {
    super.onDestroy()
    translatePresenter.onDestroy()
  }


}
