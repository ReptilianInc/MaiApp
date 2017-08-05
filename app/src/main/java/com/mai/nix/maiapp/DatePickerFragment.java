package com.mai.nix.maiapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Nix on 03.08.2017.
 */

public class DatePickerFragment extends DialogFragment {
    private ListView mListView;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.choose_week_layout, null);
        mListView = (ListView)v.findViewById(R.id.list_weeks);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.choose_date)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
