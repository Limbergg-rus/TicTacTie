package ru.vsu.cs.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class AndroidUtils {
    private Context context;

    public AndroidUtils(Context context) {
        this.context = context;
    }

    public float dpToPx(float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public int dpToPxInt(float dp) {
        return Math.round(dpToPx(dp));
    }
}
