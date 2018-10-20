package com.gkgio.translate.features.translate.view

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.gkgio.translate.R
import com.gkgio.translate.data.model.KeyValueItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FullBottomSheetDialogFragment : BottomSheetDialogFragment() {

  companion object {
    @JvmField
    val LOG_TAG: String = FullBottomSheetDialogFragment::class.java.simpleName
    const val ARG_KEY_VALUE_ITEMS_LIST = "KEY_VALUE_ITEMS_LIST"
    const val ARG_IS_FROM_LANGUAGE = "IS_FROM_LANGUAGE"

    @JvmStatic
    fun getInstance(keyValueListString: String, isFromLanguage: Boolean,
                    bottomSheetViewCallback: BottomSheetViewCallback,
                    supportFragmentManager: FragmentManager): FullBottomSheetDialogFragment {
      var fullBottomSheetDialogFragment = supportFragmentManager.findFragmentByTag(LOG_TAG)
          as FullBottomSheetDialogFragment?
      if (fullBottomSheetDialogFragment == null) {
        fullBottomSheetDialogFragment = FullBottomSheetDialogFragment()
      }
      fullBottomSheetDialogFragment.bottomSheetViewCallback = bottomSheetViewCallback
      val arguments = Bundle()
      arguments.putString(ARG_KEY_VALUE_ITEMS_LIST, keyValueListString)
      arguments.putBoolean(ARG_IS_FROM_LANGUAGE, isFromLanguage)
      fullBottomSheetDialogFragment.arguments = arguments
      return fullBottomSheetDialogFragment
    }
  }

  private lateinit var behavior: BottomSheetBehavior<*>
  private var isFromLanguage: Boolean? = null

  lateinit var bottomSheetViewCallback: BottomSheetViewCallback

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)

    val view = View.inflate(context, R.layout.sheet_layout, null)

    val arguments = arguments
    if (arguments != null) {
      isFromLanguage = arguments.getBoolean(ARG_IS_FROM_LANGUAGE)
      val keyValueListString = arguments.getString(ARG_KEY_VALUE_ITEMS_LIST)
      if (keyValueListString != null) {
        val gson = Gson()
        val keyValueListType = object : TypeToken<List<KeyValueItem>>() {}.type
        val keyValueList = gson.fromJson<List<KeyValueItem>>(keyValueListString, keyValueListType)
        val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val itemAdapter = BotomSheetItemAdapter(keyValueList, ::itemClick)
        recyclerView.adapter = itemAdapter
      }
    }
    dialog.setContentView(view)
    behavior = BottomSheetBehavior.from(view.parent as View)
    return dialog
  }

  override fun onStart() {
    super.onStart()
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
  }

  private fun itemClick(item: KeyValueItem) {
    val isFromLanguage = isFromLanguage
    if (isFromLanguage != null) {
      bottomSheetViewCallback.clickItemSelectLanguage(item, isFromLanguage)
    }
    behavior.state = BottomSheetBehavior.STATE_HIDDEN
  }

  interface BottomSheetViewCallback {
    fun clickItemSelectLanguage(keyValueItem: KeyValueItem, isFromLanguage: Boolean)
  }
}
