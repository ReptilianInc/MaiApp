package com.mai.nix.maiapp.simple_list_fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.SimpleListModel
import kotlinx.android.synthetic.main.view_simple_list_item.view.*

class SimpleListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            itemView.title.text = simpleListModel.title
            itemView.leader.text = simpleListModel.leader
            itemView.address.text = simpleListModel.address
            if (simpleListModel.phone.isNullOrEmpty()) {
                itemView.phone.visibility = View.GONE
            } else {
                itemView.phone.text = simpleListModel.phone
            }
        }
    }
}