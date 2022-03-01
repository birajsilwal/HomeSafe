import java.util.HashMap;
import java.util.Stack;

public class WebsiteSimulator {
    private final HashMap<String, String> serialToPhoneNumber = new HashMap<>();
    private final HashMap<String, String> serialToSetUp = new HashMap<>();
    private final HashMap<String, Stack<String>> serialToResetPins = new HashMap<>();

    public WebsiteSimulator() {
    }

    public void registerPhoneNumber(String phone, String serial) {
        serialToPhoneNumber.put(phone, serial);
    }

    public void setSerialToResetPins(String serial, Stack<String> reset_pins) {
        serialToResetPins.put(serial, reset_pins);
    }

    public void setSerialToSetUp(String serial, String setup) {
        serialToSetUp.put(serial, setup);
    }

    public String getSetUpPin(String phone) {
        String serial = serialToPhoneNumber.get(phone);
        return serialToSetUp.get(serial);
    }

    public String getResetPin(String phone) {
        String serial = serialToPhoneNumber.get(phone);
        return serialToResetPins.get(serial).pop();
    }

}
