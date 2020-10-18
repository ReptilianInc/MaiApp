package com.mai.nix.maiapp.expandable_list_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.SportSectionsExpListAdapter
import com.mai.nix.maiapp.model.ExpandableItemHeader
import java.util.*

/**
 * Created by Nix on 13.09.2017.
 */
abstract class ExpandableListFragment : Fragment() {
    protected abstract fun releaseThread()
    @JvmField
    protected var mExpandableListView: ExpandableListView? = null
    @JvmField
    protected var mHeaders: ArrayList<ExpandableItemHeader>? = null
    @JvmField
    protected var mAdapter: SportSectionsExpListAdapter? = null
    @JvmField
    protected var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_expandable_list, container, false)
        mExpandableListView = v.findViewById<View>(R.id.expandableRecyclerView) as ExpandableListView
        mSwipeRefreshLayout = v.findViewById<View>(R.id.expandableListSwipeRefreshLayout) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.isRefreshing = true
        mHeaders = ArrayList()
        mAdapter = SportSectionsExpListAdapter(context, mHeaders)
        releaseThread()
        mExpandableListView!!.setAdapter(mAdapter)
        for (i in mHeaders!!.indices) {
            mExpandableListView!!.expandGroup(i)
        }
        mSwipeRefreshLayout!!.setOnRefreshListener {
            mSwipeRefreshLayout!!.isRefreshing = true
            releaseThread()
        }
        return v
    }
}