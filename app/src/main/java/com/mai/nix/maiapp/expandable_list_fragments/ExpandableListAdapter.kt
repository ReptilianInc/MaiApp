package com.mai.nix.maiapp.expandable_list_fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.ExpandableItemHeader
import kotlinx.android.synthetic.main.view_expandable_list_item.view.*

class ExpandableListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val models = mutableListOf<ExpandableItemHeader>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_expandable_list_item, parent, false)
        return ExpandableListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExpandableListViewHolder).bindItem(models[position])
    }

    override fun getItemCount() = models.size

    inner class ExpandableListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: ExpandableItemHeader) {
            itemView.title.text = item.title
        }
    }
}