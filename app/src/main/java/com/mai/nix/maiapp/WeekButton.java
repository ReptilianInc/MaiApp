package com.mai.nix.maiapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class WeekButton extends AppCompatTextView {

    private int mWeekNumber;

    public WeekButton(Context context) {
        super(context);
    }

    public WeekButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    private void initAttributes(Context context, @Nullable AttributeSet set) {
        if (set == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.WeekButton);
        mWeekNumber = typedArray.getInt(R.styleable.WeekButton_week_number, 0);
        typedArray.recycle();
    }

    public int getWeekNumber() {
        return mWeekNumber;
    }

    public void setState(boolean enabled) {
        if (enabled) {
            if (mWeekNumber != 0)
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.current_week_button_background));
            else
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.week_button_background));
        } else {
            setBackgroundDrawable(null);
        }
    }

}
