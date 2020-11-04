package com.mai.nix.maiapp.simple_list_fragments

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.SimpleListModel
import kotlinx.android.synthetic.main.view_simple_list_item.view.*

class SimpleListAdapter(private val iconsPack: Array<Int>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val simpleListModels = mutableListOf<SimpleListModel>()

    fun updateItems(items: List<SimpleListModel>) {
        simpleListModels.clear()
        simpleListModels.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_simple_list_item, parent, false)
        return SimpleListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SimpleListViewHolder).bindItem(simpleListModels[position])
    }

    override fun getItemCount() = simpleListModels.size

    inner class SimpleListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(simpleListModel: SimpleListModel) {
            setIcons()
            itemView.title.text = simpleListModel.title
            itemView.leader.text = simpleListModel.leader
            if (simpleListModel.address.isNullOrEmpty()) {
                itemView.address.visibility = View.GONE
            } else {
                val html = HtmlCompat.fromHtml(simpleListModel.address, HtmlCompat.FROM_HTML_MODE_LEGACY).trim()
                if (html.length > 1) itemView.address.text = html else itemView.address.visibility = View.GONE
                if (html.contains("https")) Linkify.addLinks(itemView.address, Linkify.ALL)
            }
            if (simpleListModel.phone.isNullOrEmpty()) {
                itemView.phone.visibility = View.GONE
            } else {
                itemView.phone.text = simpleListModel.phone
            }
        }

        private fun setIcons() {
            if (iconsPack.size < 3) throw Exception("Icons Pack length must = 3")
            itemView.leader.setCompoundDrawablesRelativeWithIntrinsicBounds(iconsPack[0],0,0,0)
            itemView.address.setCompoundDrawablesRelativeWithIntrinsicBounds(iconsPack[1],0,0,0)
            itemView.phone.setCompoundDrawablesRelativeWithIntrinsicBounds(iconsPack[2],0,0,0)
        }
    }
}