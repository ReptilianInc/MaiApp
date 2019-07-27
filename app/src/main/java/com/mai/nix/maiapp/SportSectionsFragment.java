package com.mai.nix.maiapp;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.mai.nix.maiapp.expandable_list_fragments.SimpleExpandableListFragment;
import com.mai.nix.maiapp.model.SportSectionsHeader;
import java.util.List;

/**
 * Created by Nix on 11.08.2017.
 */

public class SportSectionsFragment extends SimpleExpandableListFragment {

    @Override
    protected void setObserve() {
        mSportSectionsHeadersLiveData = mApplicationViewModel.getSportSectionsLiveData();
        mSportSectionsHeadersLiveData.observe(SportSectionsFragment.this, new Observer<List<SportSectionsHeader>>() {
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

    @Override
    protected void releaseThread() {
        mApplicationViewModel.loadSportSectionsData();
    }

}
