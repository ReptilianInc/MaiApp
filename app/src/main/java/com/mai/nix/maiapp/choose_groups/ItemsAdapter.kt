package com.mai.nix.maiapp.choose_groups

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mai.nix.maiapp.R

class ItemsAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    val items = mutableListOf<String>()
    var callback: OnItemClickListener? = null

    interface OnItemClickListener {
        fun itemClicked(position: Int)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.view_group_choose_preference_item, p0, false)
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        (p0 as ItemsViewHolder).bindItem(items[p1])
    }

    override fun getItemCount() = items.size

    inner class ItemsViewHolder(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bindItem(item: String) {
            (itemView as TextView).text = item
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View) {
            if (adapterPosition >= 0) callback?.itemClicked(adapterPosition)
        }
    }
}