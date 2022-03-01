import Sensors.SoundSensor.Sound;
import Sensors.SoundSensor.SoundSensor;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import java.util.Scanner;

/**
 * Controls the input from the user and perform required functionality
 */
public class InputController{
    private String entered_key = "";
    private String temp_fingerPrint =  "";
    private boolean isTimeOutActive = false;
    private String temp_setup_password = "";
    private int count_login = 5;
    private boolean shouldStop = false;
    private final GUI gui;
    private final Pane pane;
    private STATE state = STATE.FIRST_ACCESS;
    private final AuthenticationManager fingerPrintManager = new FingerprintKey();
    private final AuthenticationManager passwordManager = new PasswordKey();
    private boolean keyPressDisable = true;
    SoundSensor soundSensor;


    public InputController(GUI gui, Pane pane, SecurityManager securityManager, SoundSensor soundSensor) {
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

    /**
     * Check if entered password is correct
     * (NOT for INITIAL SET-UP)
     */
    public void checkPassword(String entered_key) {
        boolean isCorrect = passwordManager.isValidKey(entered_key);
        if (isCorrect){
//            displayForTwoSeconds("Authorized",pane);
            displayThenOpen("Authorized",pane);
//            gui.openSafe();
//            gui.close.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//                    gui.pane.getChildren().clear();
//                    gui.createSafeInterface();
//                    startAuthorization();
//                }
//            });
        }
        else if (count_login==0){
            displayForTwoSeconds("Max attempt reached",pane);
            redirectToAuthorization();
        }
        else{
            displayForTwoSeconds("Incorrect Password, Re-enter",pane);
            count_login--;
            redirectToAuthorization();
        }
    }
    public void redirectToAuthorization(){
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
    public void checkResetPin(String entered_key){
        boolean isPWCorrect = passwordManager.isValidKey(entered_key);
        boolean isResetCorrect = passwordManager.containsResetPIN(entered_key);
        if (isResetCorrect) passwordManager.removeResetPIN(entered_key);
        gui.updateLCDDisplay("",pane);

        if (isPWCorrect || isResetCorrect){
            if (isResetCorrect){
                System.out.println("1 reset is lost");
            }
            state = STATE.SETUP_IN_RESET;
            setUpNewPassword();
        }else{
            displayForTwoSeconds("Incorrect pw or reset pin",pane);
            AnimationTimer timer = new AnimationTimer() {
                private long start;
                @Override
                public void handle(long l) {
                    if(start==0L) start = l;
                    else{
                        if(l-start>1_500_000_000L){
                            startResetPassword();
                            this.stop();
                        }
                    }
                }
            };
            timer.start();
        }
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
                if (state.equals(STATE.SETUP))
                    startSetUp();
                else {
                    state = STATE.RESET;
                    startResetPassword();
                }
            }
        }
        else if(temp_setup_password.equals(password)){
            //authenticationManager.setSavedPassword(password);
            passwordManager.setKey(password);
            fingerPrintManager.setKey(temp_fingerPrint);
            System.out.println("Saved password is: "+password);
            displayForTwoSeconds("Saved Password",pane);
            state = STATE.NORMAL;
            temp_setup_password = "";
            AnimationTimer timer = new AnimationTimer() {
                private long start;
                @Override
                public void handle(long l) {
                    if(start==0L) start = l;
                    else{
                        if(l-start>1_500_000_000L){
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
                        if(l-start>1_500_000_000L){
                            if (state.equals(STATE.SETUP)) startSetUp();
                            else {
                                state = STATE.RESET;
                                startResetPassword();
                            }
                            this.stop();
                        }
                    }
                }
            };
            timer.start();
        }
    }
    /**
     * Runs setting-up password functionality
     */
    public void setUpNewPassword() {
        gui.updateLCDDisplay("Enter New Password",pane);
        listenKeyPress();
    }

    /**
     * Starts setting-up password
     */
    public void startSetUp() {
        gui.updateLCDDisplay("Enter Set-up Password",pane);
        listenKeyPress();
    }

    public void startAuthorization() {
        temp_setup_password = "";
        listenFingerPress();
    }

    public void startResetPassword() {
        temp_setup_password = "";
        gui.updateLCDDisplay("Enter password or reset pin",pane);
        listenKeyPress();
    }

    public void listenFingerPress(){
        gui.updateLCDDisplay("Enter Fingerprint",pane);
        for (int i = 0; i < gui.fingerPrintButton.length; i++) {
            int finalI = i;
            gui.fingerPrintButton[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    keyPressDisable =  false;
                    gui.animateFingerPrint(pane);
                    if (state.equals(STATE.SETUP)) {
                        temp_fingerPrint = gui.fingerPrintButton[finalI].getText();
                        System.out.println(temp_fingerPrint);
                        setUpNewPassword();
                    }
                    else{
                        if (fingerPrintManager.isValidKey(gui.fingerPrintButton[finalI].getText())){
                            gui.updateLCDDisplay("Enter Password", pane);
                            listenKeyPress();
                        }
                        else{
                            listenFingerPress();
                        }
                    }
                }
            });
        }
    }

    /**
     * Handle each key pressed
     * Check entered password when user presses 'Enter'
     * Handle both initial set-up and normal authorization
     */
    public void listenKeyPress() {
        entered_key = "";
        isTimeOutActive = false;
        shouldStop = false;
        AnimationTimer timer = new AnimationTimer() {
            private long start;
            @Override
            public void handle(long now) {
                if (start==0L) start = now;
                else if(shouldStop) this.stop();
                else{
                    if (now-start>=10_000_000_000L){
                        if (isTimeOutActive) {
                            displayForTwoSeconds("TimeOut", pane);
                            AnimationTimer timer1 = new AnimationTimer() {
                                private long startT;

                                @Override
                                public void handle(long l) {
                                    if (startT == 0L) startT = l;
                                    else {
                                        if (l - startT > 2_000_000_000L) {
                                            shouldStop = true;
                                            if (state.equals(STATE.SETUP)) startSetUp();
                                            else if (state.equals(STATE.NORMAL)) startAuthorization();
                                            else startResetPassword();
                                            this.stop();
                                        }
                                    }
                                }
                            };
                            timer1.start();
                            isTimeOutActive = false;
                        }
                    }
                    else{
                        for (int i=0; i<=9; i++){
                            String finalI = String.valueOf(i);
                            gui.buttonArrayList.get(i).setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    if(!keyPressDisable) {
                                        System.out.println("Clicked");
                                        readKey(finalI);
                                        gui.updateLCDDisplay(entered_key, pane);
                                        try {
                                            soundSensor.playSound(Sound.Beep);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        start = now;
                                        isTimeOutActive = true;
                                    }
                                }
                            });
                        }
//                        gui.imgArray.get(0).addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
//                            @Override
//                            public void handle(MouseEvent event) {
//                                AnimationTimer timer1 = new AnimationTimer() {
//
//                                    long startTime;
//                                    boolean isFirst = true;
//
//                                    @Override
//                                    public void handle(long l) {
//                                        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
//                                            if(isFirst) {
//                                                startTime=l;
//                                                isFirst = false;
//                                            }
//                                        } else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
//                                            if (l-startTime > 3_000_000_000L){
//                                                System.out.println("Here checks");
//                                            }
//                                            this.stop();
//                                        }
//                                    }
//                                };
//                                timer1.start();
//                            }
//                        });

                        gui.buttonArrayList.get(10).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if (!keyPressDisable) {
                                    if (entered_key.length()>0)
                                        entered_key = entered_key.substring(0, entered_key.length() - 1);
                                    try {
                                        soundSensor.playSound(Sound.Beep);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.buttonArrayList.get(11).setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    if (state.equals(STATE.FIRST_ACCESS)){
                                        if (passwordManager.isValidSetUpPin(entered_key)){
                                            state = STATE.SETUP;
                                            listenFingerPress();
                                        }else startSetUp();
                                    }
                                    else if (entered_key.equals("000") && state.equals(STATE.NORMAL)) {
                                        temp_setup_password = "";
                                        state = STATE.RESET;
                                        startResetPassword();
                                    }
                                    else if (state.equals(STATE.SETUP) || state.equals(STATE.SETUP_IN_RESET)) checkSetUpPassword(entered_key);
                                    else if (state.equals(STATE.NORMAL)) checkPassword(entered_key);
                                    else if (state.equals(STATE.RESET)) checkResetPin(entered_key);
                                    entered_key = "";
                                    try {
                                        soundSensor.playSound(Sound.Beep);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    isTimeOutActive = false;
                                    shouldStop = true;
                                    start = now;
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
        gui.updateLCDDisplay(message,pane);
        entered_key = "";
        AnimationTimer timer1 = new AnimationTimer() {
            private long start1;
            @Override
            public void handle(long l) {
                if (start1==0L) start1 = l;
                else{
                    if (l-start1>1_500_000_000){
                        gui.updateLCDDisplay("",pane);
                        keyPressDisable = false;
                        this.stop();
                    }
                }
            }
        };
        timer1.start();
    }


    /**
     * Display text on LCD screen for 2 seconds then open safe
     * @param message message to display
     * @param pane pane
     */
    private void displayThenOpen(String message, Pane pane) {
        keyPressDisable = true;
        gui.updateLCDDisplay(message,pane);
        entered_key = "";
        AnimationTimer timer1 = new AnimationTimer() {
            private long start1;
            @Override
            public void handle(long l) {
                if (start1==0L) start1 = l;
                else{
                    if (l-start1>1_500_000_000){
                        gui.updateLCDDisplay("",pane);
                        keyPressDisable = false;
                        this.stop();
                        gui.openSafe();
                        gui.close.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                gui.pane.getChildren().clear();
                                gui.createSafeInterface();
                                startAuthorization();
                            }
                        });
                    }
                }
            }
        };
        timer1.start();

    }


    public void setKeyPressDisable(boolean value) {
        this.keyPressDisable = value;
    }

    public STATE getState() {
        return state;
    }

    public void clearInput() {
        this.entered_key = "";
        this.temp_setup_password = "";
        this.temp_fingerPrint = "";
        this.isTimeOutActive = false;
    }
}
