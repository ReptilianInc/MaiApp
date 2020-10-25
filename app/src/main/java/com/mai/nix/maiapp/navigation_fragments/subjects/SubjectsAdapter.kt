package com.mai.nix.maiapp.navigation_fragments.subjects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.SubjectHeader
import kotlinx.android.synthetic.main.view_subjects_list_item.view.*

class SubjectsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val subjects = mutableListOf<SubjectHeader>()

    fun updateItems(items: List<SubjectHeader>) {
        subjects.clear()
        subjects.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_subjects_list_item, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SubjectViewHolder).bindItem(subjects[position])
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(subjectHeader: SubjectHeader) {
            itemView.subjectDate.text = subjectHeader.date
            itemView.subjectDay.text = subjectHeader.day
            subjectHeader.children.forEach {
                val childView = LayoutInflater.from(itemView.context).inflate(R.layout.view_subjects_list_child_item, null)
                childView.findViewById<TextView>(R.id.subjectTime).text = it.time
                childView.findViewById<TextView>(R.id.subjectRoom).text = it.room
                childView.findViewById<TextView>(R.id.subjectTeacher).text = it.teacher
                childView.findViewById<TextView>(R.id.subjectType).text = it.type
                childView.findViewById<TextView>(R.id.subjectTitle).text = it.title
                itemView.childItemsLayout.addView(childView)
            }
        }
    }
}