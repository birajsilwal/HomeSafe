package Sensors.PowerSensor;

import javafx.scene.shape.Ellipse;

import java.util.function.Consumer;

public class PowerSensor {
    private boolean _hasPower;
    private Ellipse display;
    private Consumer<Boolean> onAction;

    public PowerSensor() {
        this._hasPower = false;
    }

    public void setPower(boolean toggle) {
        this._hasPower = toggle;
        this.display.setVisible(this._hasPower);
        if (this.onAction != null) {
            this.onAction.accept(this._hasPower);
        }
    }

    public boolean hasPower() {
        return _hasPower;
    }

    public void setOnAction(Consumer<Boolean> r) {
        this.onAction = r;
    }

    public Ellipse getView() {
        this.display = new Ellipse(8, 10);
        this.display.setVisible(this._hasPower);
        return this.display;
    }
}
