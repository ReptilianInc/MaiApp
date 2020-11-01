package com.mai.nix.maiapp.navigation_fragments.subjects

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.Day
import com.mai.nix.maiapp.model.Schedule
import kotlinx.android.synthetic.main.view_subjects_list_item.view.*

class SubjectsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val subjects = mutableListOf<Schedule>()

    fun updateItems(items: List<Schedule>) {
        subjects.clear()
        subjects.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_subjects_list_item, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SubjectViewHolder).clearView()
        holder.bindItem(subjects[position])
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun clearView() {
            itemView.childItemsLayout.removeAllViews()
        }

        @SuppressLint("InflateParams")
        fun bindItem(schedule: Schedule) {
            itemView.subjectDate.text = schedule.day?.date
            itemView.subjectDay.text = schedule.day?.day
            schedule.subjects?.forEach {
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