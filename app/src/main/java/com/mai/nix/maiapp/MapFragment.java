package com.mai.nix.maiapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Nix on 13.05.2017.
 */

public class MapFragment extends Fragment {
    private WebView mWebView;
    private FloatingActionButton mActionButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment_layout, container, false);
        mWebView = (WebView) v.findViewById(R.id.campus_view);
        mActionButton = (FloatingActionButton)v.findViewById(R.id.legend_button);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setPadding(0, 0, 0, 0);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.loadUrl("file:///android_res/drawable/scheme_for_app.png");
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LegendDialog dialog = new LegendDialog();
                dialog.show(getChildFragmentManager(), "kek");
            }
        });
        return v;
    }
}
