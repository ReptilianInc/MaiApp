package com.mai.nix.maiapp.expandable_list_fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mai.nix.maiapp.R;
import com.mai.nix.maiapp.model.SubjectHeader;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SubjectHeader> scheduleItems = new ArrayList<>();

    public void setData(List<SubjectHeader> data) {
        scheduleItems.clear();
        scheduleItems.addAll(data);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_schedule_item, parent, false);
        return new ScheduleItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ScheduleItemViewHolder) holder).bindItem(scheduleItems.get(position));
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    private class ScheduleItemViewHolder extends RecyclerView.ViewHolder {
        public ScheduleItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindItem(SubjectHeader subjectHeader) {
            ((TextView) itemView.findViewById(R.id.dateTextView)).setText(subjectHeader.getDate());
        }
    }
}
