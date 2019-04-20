package com.mai.nix.maiapp.expandable_list_fragments;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import com.mai.nix.maiapp.SimpleExpandableListFragment;
import com.mai.nix.maiapp.model.SportSectionsHeaders;
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
        mSportSectionsHeadersLiveData.observe(BarracksFragment.this, new Observer<List<SportSectionsHeaders>>() {
            @Override
            public void onChanged(@Nullable List<SportSectionsHeaders> sportSectionsHeaders) {
                mSwipeRefreshLayout.setRefreshing(false);
                mHeaders.clear();
                mHeaders.addAll(sportSectionsHeaders);
                mExpandableListView.setAdapter(mAdapter);
                for(int i = 0; i < mHeaders.size(); i++){
                    mExpandableListView.expandGroup(i);
                }
            }
        });
    }
}
