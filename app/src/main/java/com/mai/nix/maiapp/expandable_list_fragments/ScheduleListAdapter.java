package com.mai.nix.maiapp.expandable_list_fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mai.nix.maiapp.R;
import com.mai.nix.maiapp.model.SubjectBody;
import com.mai.nix.maiapp.model.SubjectHeader;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            LinearLayout container = itemView.findViewById(R.id.childItemsLayout);
            container.removeAllViews();
            List<SubjectBody> children = subjectHeader.getChildren();
            for (int i = 0; i < children.size(); i++) {
                View childView = inflater.inflate(R.layout.view_schedule_child_item, null);
                ((TextView) childView.findViewById(R.id.timeTextView)).setText(children.get(i).getTime());
                ((TextView) childView.findViewById(R.id.typeTextView)).setText(children.get(i).getType());
                ((TextView) childView.findViewById(R.id.titleTextView)).setText(children.get(i).getTitle());
                ((TextView) childView.findViewById(R.id.teacherTextView)).setText(children.get(i).getTeacher());
                ((TextView) childView.findViewById(R.id.roomTextView)).setText(children.get(i).getRoom());
                container.addView(childView);
                if (i != children.size() - 1) {
                    View divider = new View(itemView.getContext());
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    divider.setLayoutParams(layoutParams);
                    divider.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorBlack));
                    container.addView(divider);
                }
            }
        }
    }
}
