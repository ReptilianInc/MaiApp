package com.mai.nix.maiapp.expandable_list_fragments

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.ExpandableItemHeader
import kotlinx.android.synthetic.main.view_expandable_list_item.view.*

class ExpandableListAdapter(private val iconsPack: Array<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val models = mutableListOf<ExpandableItemHeader>()

    fun updateItems(items: List<ExpandableItemHeader>) {
        models.clear()
        models.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_expandable_list_item, parent, false)
        return ExpandableListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExpandableListViewHolder).clearView()
        holder.bindItem(models[position])
    }

    override fun getItemCount() = models.size

    inner class ExpandableListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun clearView() {
            itemView.childItemsLayout.removeAllViews()
        }

        fun bindItem(item: ExpandableItemHeader) {
            itemView.title.text = item.title
            item.bodies.forEachIndexed { index, expandableItemBody ->
                val childView = LayoutInflater.from(itemView.context).inflate(R.layout.view_expandable_list_item_child, null)
                setIcons(childView)
                childView.findViewById<TextView>(R.id.title).text = HtmlCompat.fromHtml(expandableItemBody.title, HtmlCompat.FROM_HTML_MODE_LEGACY)
                val owner = HtmlCompat.fromHtml(expandableItemBody.owner, HtmlCompat.FROM_HTML_MODE_LEGACY)
                if (owner.isNotEmpty()) {
                    childView.findViewById<TextView>(R.id.owner).text = owner
                } else {
                    childView.findViewById<TextView>(R.id.owner).visibility = View.GONE
                }
                val phoneView = childView.findViewById<TextView>(R.id.phone)
                if (!expandableItemBody.phoneEtc.isNullOrEmpty()) {
                    phoneView.text = HtmlCompat.fromHtml(expandableItemBody.phoneEtc, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    Linkify.addLinks(phoneView, Linkify.ALL)
                } else {
                    phoneView.visibility = View.GONE
                }
                if (index != item.bodies.lastIndex) {
                    childView.findViewById<View>(R.id.bottomDivider).visibility = View.VISIBLE
                }
                itemView.childItemsLayout.addView(childView)
            }
        }

        private fun setIcons(view: View) {
            if (iconsPack.size < 2) throw Exception("Icons Pack length must = 2")
            view.findViewById<TextView>(R.id.owner).setCompoundDrawablesRelativeWithIntrinsicBounds(iconsPack[0],0,0,0)
            view.findViewById<TextView>(R.id.phone).setCompoundDrawablesRelativeWithIntrinsicBounds(iconsPack[1],0,0,0)
        }
    }


}