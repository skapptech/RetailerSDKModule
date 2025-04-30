package app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.IndicatorView;

import androidx.annotation.Nullable;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.IndicatorView.animation.AnimationManager;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.IndicatorView.animation.controller.ValueController;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.IndicatorView.animation.data.Value;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.IndicatorView.draw.DrawManager;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.slider.IndicatorView.draw.data.Indicator;

public class IndicatorManager implements ValueController.UpdateListener {

    private final DrawManager drawManager;
    private final AnimationManager animationManager;
    private final Listener listener;

    interface Listener {
        void onIndicatorUpdated();
    }

    IndicatorManager(@Nullable Listener listener) {
        this.listener = listener;
        this.drawManager = new DrawManager();
        this.animationManager = new AnimationManager(drawManager.indicator(), this);
    }

    public AnimationManager animate() {
        return animationManager;
    }

    public Indicator indicator() {
        return drawManager.indicator();
    }

    public DrawManager drawer() {
        return drawManager;
    }

    @Override
    public void onValueUpdated(@Nullable Value value) {
        drawManager.updateValue(value);
        if (listener != null) {
            listener.onIndicatorUpdated();
        }
    }
}
