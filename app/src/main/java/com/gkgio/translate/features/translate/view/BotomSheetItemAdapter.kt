package com.gkgio.translate.features.translate.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.gkgio.translate.R
import com.gkgio.translate.data.model.KeyValueItem

class BotomSheetItemAdapter(var keyValueItems: List<KeyValueItem>, private val itemClick: (KeyValueItem) -> Unit)
  : RecyclerView.Adapter<BotomSheetItemAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.botom_sheet_item, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = keyValueItems[position]
    holder.language.text = item.value

    holder.itemView.setOnClickListener {
      itemClick(item)
    }
  }

  override fun getItemCount(): Int {
    return keyValueItems.size
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var language: TextView = itemView.findViewById(R.id.tvLanguage)
  }
}
