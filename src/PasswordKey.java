/**
 * PasswordKey class extends AuthenticationManager and manages a
 * key of a 6-digit PIN for accessing the safe
 * @author Nehemiah Cionelo
 */

import java.util.LinkedList;
import java.util.Random;

public class PasswordKey extends AuthenticationManager {
    private static final int NUM_RESET_PINS = 15;
    private static final int RESET_PIN_LENGTH = 20;
    private LinkedList<String> resetPINs;
    public PasswordKey () {
        resetPINs = new LinkedList<>();
        generateResetPINs();
        super.setResetPINs(resetPINs);
    }
    //Method to generate the unique 15 unique reset pins
    private void generateResetPINs() {
        for(int i = 0; i < NUM_RESET_PINS; i++) {
            resetPINs.add(getResetPIN());
        }
    }
    //generates individual reset PIN
    private String getResetPIN(){
        Random rand = new Random();
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < RESET_PIN_LENGTH; i++) {
            str.append(rand.nextInt(10));
        }
        return str.toString();
    }

}
