package com.mai.nix.maiapp.simple_list_fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mai.nix.maiapp.R;
import com.mai.nix.maiapp.model.StudentOrgModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StudentOrgModel> simpleListItems = new ArrayList<>();

    public void setData(List<StudentOrgModel> data) {
        simpleListItems.clear();
        simpleListItems.addAll(data);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stud_org_card, parent, false);
        return new SimpleListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SimpleListItemViewHolder) holder).bindItem(simpleListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return simpleListItems.size();
    }

    private class SimpleListItemViewHolder extends RecyclerView.ViewHolder {

        public SimpleListItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindItem(StudentOrgModel model) {
            ((TextView) itemView.findViewById(R.id.title)).setText(model.getTitle());
            if (model.getLeader() == null || model.getLeader().isEmpty()) {
                itemView.findViewById(R.id.leader).setVisibility(View.GONE);
            } else {
                ((TextView) itemView.findViewById(R.id.leader)).setText(model.getLeader());
            }
            ((TextView) itemView.findViewById(R.id.address)).setText(model.getAddress());
            if (model.getPhone() == null || model.getPhone().isEmpty()) {
                itemView.findViewById(R.id.phone).setVisibility(View.GONE);
            } else {
                ((TextView) itemView.findViewById(R.id.phone)).setText(model.getPhone());
            }
        }
    }
}
