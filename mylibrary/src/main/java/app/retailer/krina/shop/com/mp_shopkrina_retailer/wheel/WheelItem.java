package app.retailer.krina.shop.com.mp_shopkrina_retailer.wheel;

import android.graphics.Bitmap;

public class WheelItem {

    public int color;
    public Bitmap bitmap;
    public String text;

    public WheelItem(int color, Bitmap bitmap) {
        this.color = color;
        this.bitmap = bitmap;
    }

    public WheelItem(int color, Bitmap bitmap, String text) {
        this.color = color;
        this.bitmap = bitmap;
        this.text = text;
    }

}
