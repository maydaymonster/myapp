package com.ttit.myapp.schedule.custom.course;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class BackgroundView extends FrameLayout {
    public BackgroundView(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}
