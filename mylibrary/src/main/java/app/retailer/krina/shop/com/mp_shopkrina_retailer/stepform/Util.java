package app.retailer.krina.shop.com.mp_shopkrina_retailer.stepform;

import android.content.Context;
import android.util.TypedValue;

class Util {
    static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}