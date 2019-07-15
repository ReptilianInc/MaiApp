package com.mai.nix.maiapp;

import android.view.View;
import android.view.ViewGroup;

public class PeekWeekController implements View.OnClickListener {

    private int lastChosenWeek = 0;
    private ViewGroup weekControllerView;
    private WeekButtonClickListener mCallback;

    public interface WeekButtonClickListener {
        void weekButtonClicked(int chosenWeek);
    }

    public int getLastChosenWeek() {
        return lastChosenWeek;
    }

    public PeekWeekController(View rootView, int controllerViewId, WeekButtonClickListener listener) {
        weekControllerView = rootView.findViewById(controllerViewId);
        mCallback = listener;
        for (int i = 0; i < weekControllerView.getChildCount(); i++) {
            weekControllerView.getChildAt(i).setOnClickListener(this);
        }
    }

    public void initFirstClickedItem() {
        for (int i = 0; i < weekControllerView.getChildCount(); i++) {
            View button = weekControllerView.getChildAt(i);
            if (button instanceof WeekButton && ((WeekButton) button).getWeekNumber() == lastChosenWeek) {
                ((WeekButton) button).setState(true);
            }
        }
        mCallback.weekButtonClicked(lastChosenWeek);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof WeekButton) {
            ((WeekButton) v).setState(true);
            for (int i = 0; i < weekControllerView.getChildCount(); i++) {
                View button = weekControllerView.getChildAt(i);
                if (button instanceof WeekButton && ((WeekButton) button).getWeekNumber() == lastChosenWeek) {
                    ((WeekButton) button).setState(false);
                }
            }
            lastChosenWeek = ((WeekButton) v).getWeekNumber();
            mCallback.weekButtonClicked(lastChosenWeek);
        }
    }
}
