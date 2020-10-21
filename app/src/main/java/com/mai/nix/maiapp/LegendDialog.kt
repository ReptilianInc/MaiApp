package com.mai.nix.maiapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment

/**
 * Created by Nix on 14.05.2017.
 */
class LegendDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = LayoutInflater.from(activity).inflate(R.layout.fragment_legend_layout, null)
        return AlertDialog.Builder(activity)
                .setView(v)
                .setTitle("Обозначения")
                .setPositiveButton(android.R.string.ok, null)
                .create()
    }
}