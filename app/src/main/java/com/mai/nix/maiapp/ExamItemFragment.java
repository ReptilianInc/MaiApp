package com.mai.nix.maiapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nix on 01.08.2017.
 */

public class ExamItemFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<ExamModel> mExamModels;
    private MyAdapter mMyAdapter;
    private ProgressBar mProgressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_layout, container, false);
        mExamModels = new ArrayList<>();
        mExamModels.add(new ExamModel("01.06", "ЧТ", "09:00 – 10:30", "Стандартизация и сертификация программных продуктов",
                "Молчанова Светлана Ивановна", "609 (Орш. А)"));
        mExamModels.add(new ExamModel("05.06", "ПН", "09:00 – 10:30", "Архитектура ЭВМ и систем",
                "Жарков Сергей Викторович", "624 (Орш. А) ИСТ"));
        mExamModels.add(new ExamModel("09.06", "ПТ", "10:45 – 12:15", "Военная подготовка",
                "Пидор И.И.", "Военная кафедра (Полбина,45)"));
        mExamModels.add(new ExamModel("13.06", "ВТ", "13:00 – 14:30", "Философия",
                "Сухно Алексей Андреевич", "414 (Орш. В)"));
        mExamModels.add(new ExamModel("17.06", "СБ", "13:00 – 14:30", "Web-программирование",
                "Коновалов Кирилл Андреевич", "624 (Орш. А) ИСТ"));
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mMyAdapter = new MyAdapter(mExamModels);
        mRecyclerView.setAdapter(mMyAdapter);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        return v;
    }
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle, mTeacher, mTime, mDate, mDay, mRoom;
        private ExamModel mExamModel;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.exam_title);
            mDate = (TextView)itemView.findViewById(R.id.exam_date);
            mDay = (TextView)itemView.findViewById(R.id.exam_day);
            mTime = (TextView)itemView.findViewById(R.id.exam_time);
            mTeacher = (TextView)itemView.findViewById(R.id.exam_teacher);
            mRoom = (TextView)itemView.findViewById(R.id.exam_room);
        }
        public void bindModel(ExamModel model){
            mExamModel = model;
            mTitle.setText(mExamModel.getTitle());
            mTime.setText(mExamModel.getTime());
            mTeacher.setText(mExamModel.getTeacher());
            mDay.setText(mExamModel.getDay());
            mDate.setText(mExamModel.getDate());
            mRoom.setText(mExamModel.getRoom());
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<ExamModel> mExamsModelList;

        public MyAdapter(List<ExamModel> examsModelList) {
            mExamsModelList = examsModelList;
        }
        public void setExamsModelList(List<ExamModel> models){
            mExamsModelList = models;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_card, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ExamModel model = mExamsModelList.get(position);
            holder.bindModel(model);
        }

        @Override
        public int getItemCount() {
            return mExamsModelList.size();
        }
    }
}
