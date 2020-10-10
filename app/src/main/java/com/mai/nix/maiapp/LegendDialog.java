package com.mai.nix.maiapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Nix on 14.05.2017.
 */

public class LegendDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.legend_layout, null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Обозначения")
                .setPositiveButton(android.R.string.ok, null)
                .create();

    }
}
