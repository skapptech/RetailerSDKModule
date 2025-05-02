package app.retailer.krina.shop.com.mp_shopkrina_retailer.stepform;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;

class ConnectorLineDrawer {
    private final Paint paint = new Paint();
    private final Paint paint1 = new Paint();
    private final RectF line = new RectF();

    ConnectorLineDrawer(Context context) {
        paint.setColor(context.getResources().getColor(R.color.vertical_stepper_view_grey_100));
        paint1.setColor(context.getResources().getColor(R.color.colorAccent));
    }

    void adjust(Context context, int width, int height) {
        line.left = Util.dpToPx(context, 13.0f);
        line.right = Util.dpToPx(context, 14.6f);
        line.top = Util.dpToPx(context, 0);
        line.bottom = height;
    }

    void draw(Canvas canvas, int active) {
        if (active == 0)
            canvas.drawRect(line, paint);
        else
            canvas.drawRect(line, paint1);
    }
}