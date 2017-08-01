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
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nix on 01.08.2017.
 */

public class SubjectsOfThisWeekFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<SubjectModel> mSubjectModels;
    private SubjectsOfThisWeekFragment.MyAdapter mMyAdapter;
    private ProgressBar mProgressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_layout, container, false);
        mSubjectModels = new ArrayList<>();
        mSubjectModels.add(new SubjectModel("01.06", "ЧТ", "09:00 – 10:30", "Стандартизация и сертификация программных продуктов",
                "Молчанова Светлана Ивановна", "609 (Орш. А)", "КСР"));
        mSubjectModels.add(new SubjectModel("05.06", "ПН", "09:00 – 10:30", "Архитектура ЭВМ и систем",
                "Жарков Сергей Викторович", "624 (Орш. А) ИСТ", "ПЗ"));
        mSubjectModels.add(new SubjectModel("10:45 – 12:15", "Военная подготовка",
                "Пидор И.И.", "Военная кафедра (Полбина,45)", "ЛК"));
        mSubjectModels.add(new SubjectModel("13:00 – 14:30", "Философия",
                "Сухно Алексей Андреевич", "414 (Орш. В)", "ЛК"));
        mSubjectModels.add(new SubjectModel("17.06", "СБ", "13:00 – 14:30", "Web-программирование",
                "Коновалов Кирилл Андреевич", "624 (Орш. А) ИСТ", "ПЗ"));
        mProgressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mMyAdapter = new SubjectsOfThisWeekFragment.MyAdapter(mSubjectModels);
        mRecyclerView.setAdapter(mMyAdapter);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        return v;
    }
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle, mTeacher, mTime, mDate, mDay, mRoom, mType;
        private RelativeLayout mHolder;
        private SubjectModel mSubjectModel;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.subject_title);
            mDate = (TextView)itemView.findViewById(R.id.subject_date);
            mDay = (TextView)itemView.findViewById(R.id.subject_day);
            mTime = (TextView)itemView.findViewById(R.id.subject_time);
            mTeacher = (TextView)itemView.findViewById(R.id.subject_teacher);
            mRoom = (TextView)itemView.findViewById(R.id.subject_room);
            mType = (TextView)itemView.findViewById(R.id.subject_type);
            mHolder = (RelativeLayout) itemView.findViewById(R.id.date_holder);
        }
        public void bindModel(SubjectModel model){
            mSubjectModel = model;
            if(mSubjectModel.getDate() != null && mSubjectModel.getDay()!= null ){
                mDay.setText(mSubjectModel.getDay());
                mDate.setText(mSubjectModel.getDate());
            }else{
                mHolder.getLayoutParams().height = 0;
                mHolder.invalidate();
            }
            mTitle.setText(mSubjectModel.getTitle());
            mTime.setText(mSubjectModel.getTime());
            mTeacher.setText(mSubjectModel.getTeacher());
            mRoom.setText(mSubjectModel.getRoom());
            mType.setText(mSubjectModel.getType());
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<SubjectsOfThisWeekFragment.MyViewHolder>{
        private List<SubjectModel> mSubjectsModelList;

        public MyAdapter(List<SubjectModel> subjectModels) {
            mSubjectsModelList = subjectModels;
        }
        public void setSubjectsModelList(List<SubjectModel> models){
            mSubjectsModelList = models;
        }
        @Override
        public SubjectsOfThisWeekFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_card, parent, false);
            SubjectsOfThisWeekFragment.MyViewHolder vh = new SubjectsOfThisWeekFragment.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(SubjectsOfThisWeekFragment.MyViewHolder holder, int position) {
            SubjectModel model =mSubjectsModelList.get(position);
            holder.bindModel(model);
        }

        @Override
        public int getItemCount() {
            return mSubjectsModelList.size();
        }
    }
}
