/**
 * Security Manager class handles status of lock state and
 * sensor state.
 * When the lock system is locked, the sensor is presses
 * but when the lock system is unlocked, the sensor is unpressed state.
 * @author Biraj Silwal
 */

public class SecurityManager {

    private Boolean lockedState = true;
    private Boolean sensorIsPressed = true;

    /**
     * set the state of the lock system to locked state.
     */
    public void setLockedState(Boolean lockedState) {
        this.lockedState = lockedState;
    }

    /**
     * @return lockedState - lock state of the lock system
     */
    public Boolean getLockedState() {
        return lockedState;
    }

    /**
     * if the lock system is locked, the sensor is pressed
     */
    public void setSensorPressedState(Boolean lockedState) {
        sensorIsPressed = lockedState;
    }

    /**
     * @return sensorIsPressed, if the sensor is pressed, return true else false
     */
    public Boolean getSensorPressedState() {
        return sensorIsPressed;
    }
}
