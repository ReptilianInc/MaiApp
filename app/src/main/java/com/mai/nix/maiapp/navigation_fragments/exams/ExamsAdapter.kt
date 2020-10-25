package com.mai.nix.maiapp.navigation_fragments.exams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.ExamModel
import kotlinx.android.synthetic.main.view_exam_list_item.view.*

class ExamsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val exams = mutableListOf<ExamModel>()

    fun updateItems(items: List<ExamModel>) {
        exams.clear()
        exams.addAll(exams)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_group_choose_preference_item, parent, false)
        return ExamsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExamsViewHolder).bindItem(exams[position])
    }

    override fun getItemCount(): Int {
        return exams.size
    }

    inner class ExamsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(examModel: ExamModel) {
            itemView.examDate.text = examModel.date
            itemView.examDay.text = examModel.day
            itemView.examTime.text = examModel.time
            itemView.examTitle.text = examModel.title
            itemView.examTeacher.text = examModel.teacher
            itemView.examRoom.text = examModel.room
        }
    }
}