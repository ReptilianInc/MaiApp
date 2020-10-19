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

class ExpandableListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            item.bodies.forEachIndexed { index, expandableItemBody ->
                val childView = LayoutInflater.from(itemView.context).inflate(R.layout.view_expandable_list_item_child, null)
                childView.findViewById<TextView>(R.id.title).text = expandableItemBody.title
                childView.findViewById<TextView>(R.id.owner).text = expandableItemBody.owner
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
    }


}