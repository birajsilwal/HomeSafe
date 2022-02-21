package Sensors.PowerSensor;

public class PowerSensor {
    private boolean _hasPower;
    public PowerSensor(){
        this._hasPower = false;
    }

    public void setPower(boolean toggle){
        this._hasPower = toggle;
    }

    public boolean hasPower(){
        return _hasPower;
    }
}
