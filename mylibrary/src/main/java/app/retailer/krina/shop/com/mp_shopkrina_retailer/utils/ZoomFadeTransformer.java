package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ZoomFadeTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage( View page, float pos ) {
        final float scale = pos < 0 ? pos + 1f : Math.abs( 1f - pos );
        page.setScaleX( scale );
        page.setScaleY( scale );
        page.setPivotX( page.getWidth() * 0.5f );
        page.setPivotY( page.getHeight() * 0.5f );
        page.setAlpha( pos < -1f || pos > 1f ? 0f : 1f - (scale - 1f) );
    }
}
/*implements ViewPager.PageTransformer {

    private float offset = -1;
    private float paddingLeft = 0;
    private float minScale = 1.2f;
    private float minAlpha = 1.2f;

    @Override
    public void transformPage(View page, float position) {
        if (offset == -1) {
            offset = paddingLeft / page.getMeasuredWidth();
        }
        if (position < -1) {
            page.setAlpha(0);
        } else if (position <= 1) {
            float scaleFactor = Math.max(minScale, 1 - Math.abs(position - offset));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            float alphaFactor = Math.max(minAlpha, 1 - Math.abs(position - offset));
            page.setAlpha(alphaFactor);
        } else {
            page.setAlpha(0);
        }
    }
}*/
