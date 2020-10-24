package com.mai.nix.maiapp.navigation_fragments.campus

import androidx.lifecycle.ViewModelProviders
import com.mai.nix.maiapp.ListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class CampusAbstractFragment: ListFragment() {

    protected lateinit var campusViewModel: CampusViewModel

    override fun observeViewModel() {
        campusViewModel = ViewModelProviders.of(this, CampusViewModelFactory()).get(CampusViewModel::class.java)
    }

}