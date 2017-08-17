package com.mai.nix.maiapp.navigation_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.mai.nix.maiapp.R;

/**
 * Created by Nix on 14.08.2017.
 */

public class AboutFragment extends Fragment implements View.OnClickListener{
    private Button mMai, mDev;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_fragment_layout, container, false);
        mMai = (Button)v.findViewById(R.id.go_to_site_mai);
        mDev = (Button)v.findViewById(R.id.go_to_site_dev);
        mMai.setOnClickListener(this);
        mDev.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.go_to_site_dev:
                Toast.makeText(getContext(), "Coming soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.go_to_site_mai:
                Toast.makeText(getContext(), "Coming soon!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
