package com.mai.nix.maiapp;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.mai.nix.maiapp.expandable_list_fragments.SimpleExpandableListFragment;
import com.mai.nix.maiapp.model.SportSectionsHeaders;
import java.util.List;

/**
 * Created by Nix on 11.08.2017.
 */

public class SportSectionsFragment extends SimpleExpandableListFragment {

    @Override
    protected void setObserve() {
        mSportSectionsHeadersLiveData = mApplicationViewModel.getSportSectionsLiveData();
        mSportSectionsHeadersLiveData.observe(SportSectionsFragment.this, new Observer<List<SportSectionsHeaders>>() {
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

    @Override
    protected void releaseThread() {
        mApplicationViewModel.loadSportSectionsData();
    }

}
