import Sensors.SoundSensor.Sound;
import Sensors.SoundSensor.SoundSensor;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

public class InputController{
    String entered_key = "";
    String finger_print = "";
    Boolean keyPressDisable = false;
    String displayText = "";
    String temp_setup_password = "";
    boolean enter_pressed = false;
    int count_login = 5;
    GUI gui;
    Pane pane;
    STATE state = STATE.SETUP;
    AuthenticationManager authenticationManager;
    SecurityManager  securityManager;
    private SoundSensor soundSensor;

    public InputController(GUI gui, Pane pane, SoundSensor soundSensor) {
        this.gui = gui;
        this.pane = pane;
        this.soundSensor = soundSensor;
    }

    /**
     * Read the number inputted by user
     * @param s number inputted
     */
    public void readKey(String s){
        this.entered_key += s;
    }
    public void finger_print(String s){
        this.finger_print = s;
    }

    /**
     * Check if entered password is correct
     * (NOT for INITIAL SET-UP)
     */
    public void checkPassword() {
//        boolean isCorrect = authenticationManager.verifyPassword(entered_key);
//        if (isCorrect){
        if (true){
            displayForTwoSeconds("Authorized",pane);
            // Let know Security Manager that the system is authorized
            keyPressDisable = true;
        }
        else{
            displayForTwoSeconds("Incorrect Password, Re-enter",pane);
            count_login--;
            AnimationTimer timer = new AnimationTimer() {
                private long start;
                @Override
                public void handle(long l) {
                    if(start==0L) start = l;
                    else{
                        if(l-start>2_000_000_000L){
                            startAuthorization();
                            this.stop();
                        }
                    }
                }
            };
            timer.start();
        }
    }


    public boolean checkResetPin(){
        return false;
    }

    /**
     * First-time set-up: Get input password from user and store it???
     * @param password set-up password
     */
    public void checkSetUpPassword(String password){
        if (temp_setup_password.equals("")) {
            if (password.length()==6) {
                temp_setup_password = password;
                displayForTwoSeconds("Confirm Password", pane);
                listenKeyPress();
            }else{
                displayForTwoSeconds("Enter 6-digit password",pane);
                startSetUp();
            }
        }
        else if(temp_setup_password.equals(password)){
//            authenticationManager.setSavedPassword(password);
            System.out.println("Saved password is: "+password);
            displayForTwoSeconds("Saved Password",pane);
            state = STATE.NORMAL;
            AnimationTimer timer = new AnimationTimer() {
                private long start;
                @Override
                public void handle(long l) {
                    if(start==0L) start = l;
                    else{
                        if(l-start>2_000_000_000L){
                            startAuthorization();
                            this.stop();
                        }
                    }
                }
            };
            timer.start();
        }
        else{
            displayForTwoSeconds("Password didn't Match",pane);
            temp_setup_password = "";
            AnimationTimer timer = new AnimationTimer() {
                private long start;
                @Override
                public void handle(long l) {
                    if(start==0L) start = l;
                    else{
                        if(l-start>2_000_000_000L){
                            startSetUp();
                            this.stop();
                        }
                    }
                }
            };
            timer.start();
        }
    }


    /**
     * Ask user for set-up password
     */
    public void startSetUp() {
        gui.updateLCDDisplay("Enter Set up Password", pane);
        listenKeyPress();
    }


    /**
     * Handle each key pressed
     * Check entered password when user presses 'Enter'
     * Handle both initial set-up and normal authorization
     */
    public void listenKeyPress() {
        enter_pressed = false;
        entered_key = "";
        AnimationTimer timer = new AnimationTimer() {
            private long start;
            @Override
            public void handle(long now) {
                if (start==0L) start = now;
                else{
                    if ((now-start>=10_000_000_000L) && !entered_key.equals("")){
                        System.out.println("Came here");
                        if (!enter_pressed) {
                            displayForTwoSeconds("TimeOut", pane);
                            AnimationTimer timer = new AnimationTimer() {
                                private long start;
                                @Override
                                public void handle(long l) {
                                    if(start==0L) start = l;
                                    else{
                                        if(l-start>2_000_000_000L){
                                            if (state.equals(STATE.SETUP)) startSetUp();
                                            else startAuthorization();
                                            this.stop();
                                        }
                                    }
                                }
                            };
                            timer.start();
                        }
                        this.stop();
                    }
                    else{
                        start = now;
                        for (int i = 0; i < 10; i++) {
                            var index = i;
                            gui.buttonArrayList.get(i).setOnAction(e -> {
                                if (!keyPressDisable) {
                                    try {
                                        soundSensor.playSound(Sound.Beep);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    displayText += ("" + index);
                                    readKey("" + index);
                                    gui.updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            });
                        }
                        gui.buttonArrayList.get(10).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if (!keyPressDisable) {
                                    try {
                                        soundSensor.playSound(Sound.Beep);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    displayText = displayText.substring(0, displayText.length() - 1);
                                    entered_key = entered_key.substring(0, entered_key.length() - 1);
                                    gui.updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.buttonArrayList.get(11).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    try {
                                        soundSensor.playSound(Sound.Beep);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    if (state.equals(STATE.SETUP)) checkSetUpPassword(entered_key);
                                    else if (state.equals(STATE.NORMAL)) checkPassword();
                                    enter_pressed = true;
                                }
                            }
                        });
                    }
                }
            }
        };
        timer.start();
    }


    /**
     * Display text on LCD screen for 2 seconds
     * @param message message to display
     * @param pane pane
     */
    private void displayForTwoSeconds(String message, Pane pane) {
        keyPressDisable = true;
        displayText = message;
        gui.updateLCDDisplay(displayText,pane);
        entered_key = "";
        AnimationTimer timer1 = new AnimationTimer() {
            private long start1;
            @Override
            public void handle(long l) {
                if (start1==0L) start1 = l;
                else{
                    if (l-start1>1_500_000_000){
                        displayText = "";
                        gui.updateLCDDisplay(displayText,pane);
                        keyPressDisable = false;
                        this.stop();
                    }
                }
            }
        };
        timer1.start();
    }


    /**
     * Get password from user
     */
    public void startAuthorization() {
        gui.updateLCDDisplay("Enter Password",pane);
        listenKeyPress();
    }

    public void setKeyPressDisable(boolean value) {
        this.keyPressDisable = value;
    }

    public STATE getState() {
        return state;
    }

    public void clearInput() {
        this.entered_key = "";
        this.displayText = "";
        if (this.state == STATE.SETUP) {
            this.temp_setup_password = "";
        }
    }
}
