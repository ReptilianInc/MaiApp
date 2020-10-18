package com.mai.nix.maiapp.simple_list_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.StudOrgAdapter
import com.mai.nix.maiapp.model.SimpleListModel
import kotlinx.android.synthetic.main.student_orgs_layout.*
import java.util.*

/**
 * Created by Nix on 13.09.2017.
 */
abstract class SimpleListFragment : Fragment() {
    abstract fun releaseThread()

    @JvmField
    protected var simpleListCollection: ArrayList<SimpleListModel>? = null

    @JvmField
    protected var mAdapter: StudOrgAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.student_orgs_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        simpleListSwipeRefreshLayout.isRefreshing = true
        simpleListCollection = ArrayList()
        mAdapter = StudOrgAdapter(context, simpleListCollection)
        releaseThread()
        simpleListView.adapter = mAdapter
        simpleListSwipeRefreshLayout.setOnRefreshListener {
            simpleListSwipeRefreshLayout.isRefreshing = true
            releaseThread()
        }
    }
}