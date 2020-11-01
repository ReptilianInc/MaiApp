package com.mai.nix.maiapp.navigation_fragments.campus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mai.nix.maiapp.LegendDialog
import com.mai.nix.maiapp.R
import kotlinx.android.synthetic.main.fragment_map_layout.*

/**
 * Created by Nix on 13.05.2017.
 */

class MapFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        campusView.settings.setSupportZoom(true)
        campusView.settings.builtInZoomControls = true
        campusView.setPadding(0, 0, 0, 0)
        campusView.isScrollbarFadingEnabled = true
        campusView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        campusView.loadUrl("file:///android_asset/scheme_for_app.png")
        legendButton.setOnClickListener {
            val dialog = LegendDialog()
            dialog.show(childFragmentManager, "LegendDialog")
        }
    }
}