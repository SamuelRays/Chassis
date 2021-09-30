package sample.util;

import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;

public enum Color {
    OK(Paint.valueOf("#00ff00")),
    ERROR(Paint.valueOf("#ff0000")),
    PS_ERROR(Paint.valueOf("#ff5000")),
    UNREACHED(Paint.valueOf("#a0a0a0")),
    SLOT_WAITING(RadialGradient.valueOf("radial-gradient(center 50% 50%, radius 50%, #ffffff  0%, #a0a0a0 100%)")),
    SLOT_PS_ERROR(RadialGradient.valueOf("radial-gradient(center 50% 50%, radius 50%, #ffffff  0%, #ff5000 100%)")),
    SLOT_OK(RadialGradient.valueOf("radial-gradient(center 50% 50%, radius 50%, #ffffff  0%, #00ff00 100%)")),
    SLOT_ERROR(RadialGradient.valueOf("radial-gradient(center 50% 50%, radius 50%, #ffffff  0%, #ff0000 100%)"));

    private Paint paint;

    Color(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }
}