import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.PasswordAuthentication;
import java.util.Scanner;

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

    public InputController(GUI gui, Pane pane) {
        this.gui = gui;
        this.pane = pane;
        this.authenticationManager = authenticationManager;
        this.securityManager = securityManager;
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
        boolean isCorrect = authenticationManager.verifyPassword(entered_key);
        if (isCorrect){
            displayForTwoSeconds("Authorized",pane);
            // Let know Security Manager that the system is authorized
            keyPressDisable = true;
        }
        else{
            entered_key = "";
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
    public void checkResetPin(){
        boolean isPWCorrect = authenticationManager.verifyPassword(entered_key);
        boolean isResetCorrect = authenticationManager.verifyResetPin(entered_key);
        entered_key = "";
        gui.updateLCDDisplay("",pane);
        if (isPWCorrect || isResetCorrect){
            if (isResetCorrect){
                System.out.println("1 reset is lost");
            }
            state = STATE.SETUP_IN_RESET;
            setUpPassword();
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
//            authenticationManager.setSavedPassword(password);
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
    public void setUpPassword() {
        gui.updateLCDDisplay("Enter New Password",pane);
        listenKeyPress();
    }


    /**
     * Ask user for set-up password
     */
    public void startSetUp() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Setup fingerprint: Any number for simulation");
        authenticationManager.setFingerPrint(scanner.next());
        setUpPassword();
    }


    public void startAuthorization() {
        temp_setup_password = "";
        System.out.println("Verify Finger Print");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String fingerPrint = scanner.next();
            if (authenticationManager.verifyFingerPrint(fingerPrint)) {
                gui.updateLCDDisplay("Enter Password", pane);
                listenKeyPress();
                return;
            } else {
                System.out.println("Please re-enter finger print.");
            }
        }
    }

    private void startResetPassword() {
        temp_setup_password = "";
        gui.updateLCDDisplay("Enter password or reset pin",pane);
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
                        gui.imgArray.get(0).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("0");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
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
                        gui.imgArray.get(1).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("1");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(2).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("2");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(3).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("3");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(4).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if (!keyPressDisable) {
                                    readKey("4");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(5).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("5");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(6).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("6");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(7).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("7");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(8).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("8");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(9).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    readKey("9");
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(10).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if (!keyPressDisable) {
                                    entered_key = entered_key.substring(0, entered_key.length() - 1);
                                    gui.updateLCDDisplay(entered_key, pane);
                                    start = now;
                                }
                            }
                        });
                        gui.imgArray.get(11).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    if (entered_key.equals("000")) {
                                        temp_setup_password = "";
                                        startResetPassword();
                                        state = STATE.RESET;
                                    }
                                    else if (state.equals(STATE.SETUP) || state.equals(STATE.SETUP_IN_RESET)) checkSetUpPassword(entered_key);
                                    else if (state.equals(STATE.NORMAL)) checkPassword();
                                    else if (state.equals(STATE.RESET)) checkResetPin();
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
     * Get password from user
     */
    public void startAuthorization() {
        gui.updateLCDDisplay("Enter Password",pane);
        listenKeyPress();
    }
}
