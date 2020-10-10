package com.mai.nix.maiapp.choose_groups

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mai.nix.maiapp.R

class GroupsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val groups = mutableListOf<String>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.view_group_list_item, p0, false)
        return GroupItemViewHolder(view)
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        (p0 as GroupItemViewHolder).bindItem(groups[p1])
    }

    override fun getItemCount() = groups.size

    inner class GroupItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(group: String) {
            (itemView as TextView).text = group
        }
    }
}