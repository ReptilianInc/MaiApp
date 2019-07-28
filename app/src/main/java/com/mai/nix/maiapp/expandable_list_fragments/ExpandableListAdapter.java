package com.mai.nix.maiapp.expandable_list_fragments;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mai.nix.maiapp.R;
import com.mai.nix.maiapp.model.SportSectionsBodies;
import com.mai.nix.maiapp.model.SportSectionsHeader;
import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SportSectionsHeader> expandableListItems = new ArrayList<>();

    public void setData(List<SportSectionsHeader> expandableItems) {
        expandableListItems.clear();
        expandableListItems.addAll(expandableItems);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_expandable_list_item, parent, false);
        return new ExpandableListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ExpandableListViewHolder) holder).bindItem(expandableListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return expandableListItems.size();
    }

    private class ExpandableListViewHolder extends RecyclerView.ViewHolder {
        public ExpandableListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindItem(SportSectionsHeader sportSectionsHeader) {
            ((TextView) itemView.findViewById(R.id.sectionTitle)).setText(sportSectionsHeader.getTitle());
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            LinearLayout container = itemView.findViewById(R.id.childItemsLayout);
            container.removeAllViews();
            List<SportSectionsBodies> children = sportSectionsHeader.getBodies();
            for (int i = 0; i < children.size(); i++) {
                View childView = inflater.inflate(R.layout.view_expandable_list_child_item, null);
                ((TextView) childView.findViewById(R.id.titleTextView)).setText(children.get(i).getTitle());
                ((TextView) childView.findViewById(R.id.ownerTextView)).setText(Html.fromHtml(children.get(i).getOwner()));
                if (children.get(i).getPhoneEtc() != null) {
                    ((TextView) childView.findViewById(R.id.phoneTextView)).setText(children.get(i).getPhoneEtc());
                } else {
                    childView.findViewById(R.id.phoneTextView).setVisibility(View.GONE);
                }
                container.addView(childView);
            }
        }
    }
}
