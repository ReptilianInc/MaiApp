package com.mai.nix.maiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_tabs_abstract.*

/**
 * Created by Nix on 01.08.2017.
 */
abstract class TabsFragment : Fragment() {

    protected abstract fun setupViewPager(viewPager: ViewPager?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tabs_abstract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.offscreenPageLimit = 3
        setupViewPager(viewPager)
        tabs.post { tabs.setupWithViewPager(viewPager) }
    }
}