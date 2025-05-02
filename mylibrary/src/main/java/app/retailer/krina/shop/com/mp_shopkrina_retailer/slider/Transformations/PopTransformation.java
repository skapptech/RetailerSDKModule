package app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.Transformations;

import android.view.View;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.SliderPager;

public class PopTransformation implements SliderPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {

        page.setTranslationX(-position * page.getWidth());

        if (Math.abs(position) < 0.5) {
            page.setVisibility(View.VISIBLE);
            page.setScaleX(1 - Math.abs(position));
            page.setScaleY(1 - Math.abs(position));
        } else if (Math.abs(position) > 0.5) {
            page.setVisibility(View.GONE);
        }


    }
}