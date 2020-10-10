package com.mai.nix.maiapp.choose_groups

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mai.nix.maiapp.R

class GroupsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val groups = mutableListOf<String>()
    var callback: GroupChosenListener? = null
    private var lastChosenPosition = -1

    interface GroupChosenListener {
        fun onGroupChosen(group: String)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.view_group_list_item, p0, false)
        return GroupItemViewHolder(view)
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        (p0 as GroupItemViewHolder).bindItem(groups[p1])
    }

    override fun getItemCount() = groups.size

    inner class GroupItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bindItem(group: String) {
            (itemView as TextView).text = group
            itemView.setCompoundDrawablesWithIntrinsicBounds(0, 0, if (adapterPosition == lastChosenPosition) R.drawable.ic_baseline_done_24 else 0, 0)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (adapterPosition != -1) lastChosenPosition = adapterPosition
            callback?.onGroupChosen(groups[lastChosenPosition])
            notifyDataSetChanged()
        }
    }
}