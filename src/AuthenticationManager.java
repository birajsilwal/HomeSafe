/**
 * AuthenicationManager is the abstract class for the Fingerprint
 * and Password classes. It manages data structures and variables
 * holding the different keys for HomeSafe's two factor authentication.
 * @author Nehemiah Cionelo
 */

import java.util.LinkedList;
import java.util.Random;

abstract class AuthenticationManager {
    private final LinkedList<Object> resetPINs;
    private Object currentKey;
    public AuthenticationManager(){
        resetPINs = new LinkedList<>();
    }
    /**
     * Method for when user wants to set a new key.
     * @param input reset or 6-digit PIN or fingerprint from user
     * @param newKey new key user wants to set as current key
     * @return true if input is valid passcode and
     * current key is changed to new key, else false
     */
    public boolean setNewKey(Object input, Object newKey) {
        //CHK NEW KEY CORRECT TYPE
        if(newKey.getClass().equals(currentKey)) {
            System.out.println("New key ["+ newKey + "] invalid type." +
                    "\nNew key not set.");
            return false;
        }
        //RESET PIN CASE
        else if (!resetPINs.isEmpty() &&
                resetPINs.getFirst().getClass().equals(input.getClass())
                && containsResetPIN(input)) {
            //destroy that reset PIN
            resetPINs.remove(input);
            this.currentKey = newKey;
            System.out.println("New key [" + newKey + "] set." +
                    "\nThere are now ["+ resetPINs.size() +"] reset PINs left.");
            return true;
        }
        //6-DIGIT PIN OR KNOWN FINGERPRINT CASE
        else if (currentKey.getClass().equals(input.getClass())
                && isValidKey(input)) {
            this.currentKey = newKey;
            System.out.println("New key [" + newKey + "] set.");
            return true;
        }
        System.out.println("Input [" + input + "] invalid." +
                "\nNew key not set.");
        return false;
    }

    /**
     * @param PINs
     * @return true if reset PINs sucessfully set to given list PINs
     */
    public boolean setResetPINs(LinkedList<String> PINs){
        resetPINs.addAll(PINs);
        return !resetPINs.isEmpty();
    }
    /**
     * @return current key
     */
    public Object getCurrentKey() {
        if(currentKey == null) {
            System.out.println("Current key NULL.");
            return null;
        }
        return currentKey;
    }
    /**
     * @return reset pins
     */
    public LinkedList<Object> getResetPINs() {
        if (resetPINs.isEmpty()) System.out.println("Reset PINs empty.");
        return resetPINs;
    }
    /**
     * @param PIN inputted reset pin from user
     * @return true if given pin is one of the safe's unique reset PINs
     */
    public boolean containsResetPIN(Object PIN) { //compareResetPin()
        return resetPINs.contains(PIN);
    }
    /**
     * @param inputKey
     * @return true if inputKey is the current key, else false
     */
    public boolean isValidKey(Object inputKey) {
    return currentKey.equals(inputKey); //may need to override .equals() especially for Fingerprint?
    }
}
/**
 * FingerprintKey class extends AuthenticationManager and manages a
 * key of a fingerprint for accessing the safe
 * @author Nehemiah Cionelo
 */

class FingerprintKey extends AuthenticationManager {
    public FingerprintKey () {}
}

/**
 * PasswordKey class extends AuthenticationManager and manages a
 * key of a 6-digit PIN for accessing the safe
 * @author Nehemiah Cionelo
 */

class PasswordKey extends AuthenticationManager {
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

