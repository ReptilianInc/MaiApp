package com.mai.nix.maiapp.navigation_fragments.life

import androidx.lifecycle.ViewModelProviders
import com.mai.nix.maiapp.ListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class LifeAbstractFragment: ListFragment() {

    protected lateinit var lifeViewModel: LifeViewModel

    override fun observeViewModel() {
        lifeViewModel = ViewModelProviders.of(this, LifeViewModelFactory()).get(LifeViewModel::class.java)
    }
}