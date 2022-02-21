package Sensors.PowerSensor;


import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class PowerSensor {
    private boolean _hasPower;
    private Ellipse display;
    public PowerSensor(){
        this._hasPower = false;
    }

    public void setPower(boolean toggle){
        this._hasPower = toggle;
        this.display.setVisible(this._hasPower);
    }

    public boolean hasPower(){
        return _hasPower;
    }

    public Ellipse getView(){
        this.display = new Ellipse(15, 20);
        this.display.setVisible(this._hasPower);
        return this.display;
    }
}
