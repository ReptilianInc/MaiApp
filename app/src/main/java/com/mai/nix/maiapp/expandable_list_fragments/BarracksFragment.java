package com.mai.nix.maiapp.expandable_list_fragments;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.mai.nix.maiapp.model.SportSectionsHeader;
import java.util.List;

/**
 * Created by Nix on 13.08.2017.
 */

public class BarracksFragment extends SimpleExpandableListFragment {

    @Override
    protected void releaseThread() {
        mApplicationViewModel.loadBarracksData();
    }

    @Override
    protected void setObserve() {
        mSportSectionsHeadersLiveData = mApplicationViewModel.getBarracksLiveData();
        mSportSectionsHeadersLiveData.observe(BarracksFragment.this, new Observer<List<SportSectionsHeader>>() {
            @Override
            public void onChanged(@Nullable List<SportSectionsHeader> sportSectionsHeaders) {
                mSwipeRefreshLayout.setRefreshing(false);
                mHeaders.clear();
                mHeaders.addAll(sportSectionsHeaders);
                mAdapter.setData(mHeaders);
                mExpandableRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
