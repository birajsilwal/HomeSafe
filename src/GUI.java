import Sensors.PowerSensor.PowerSensor;
import Sensors.SoundSensor.Sound;
import Sensors.SoundSensor.SoundSensor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class GUI{
    private final double width  = 1000;
    private final double height = 680;
    private double lcdScreenX;
    private double lcdScreenY;
    private double lcdScreenWidth;
    private double lcdScreenHeight;
    public Button close;

    private final SoundSensor soundSensor;
    private final PowerSensor powerSensor;

    int lcdDisplayHeight = 100;
    Rectangle background = new Rectangle(width, height + lcdDisplayHeight, Color.LIGHTGREY);
    Rectangle grayBackground = new Rectangle(235, 281 + lcdDisplayHeight, Color.GREY);
    Rectangle blackBackground = new Rectangle(215, 260 + lcdDisplayHeight, Color.BLACK);
    Rectangle onOffBtn = new Rectangle(50,50);

    Pane pane = new Pane();

    public GUI(SoundSensor soundSensor, PowerSensor powerSensor){
        this.soundSensor = soundSensor;
        this.powerSensor = powerSensor;
    }

    ArrayList<Button> buttonArrayList = new ArrayList<>();
    Button[] fingerPrintButton = new Button[3];


    /**
     * Create user's interface with safe's image,
     * keypad, and fingerprint scanner
     * @return pane
     */
    public Pane createSafeInterface() {
        //get button images
        int imgSize = 55;
        InputStream stream = null;
        for (int count = 0; count < 12; count++){
            String path = "Resources/Images/bt" + count + ".PNG";
            try {
                stream = new FileInputStream(
                        path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Image current_image = new Image(stream, imgSize, imgSize, false, false);
            ImageView view = new ImageView(current_image);
            Button button = new Button();
            button.setGraphic(view);
            button.setPrefSize(imgSize,imgSize);
            button.setStyle("-fx-background-color: #000000");
            button.setOnAction(e -> {
                if(powerSensor.hasPower()) {
                    try {
                        this.soundSensor.playSound(Sound.Beep);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            buttonArrayList.add(button);
        }

        //Add fingerprint scanner button
        String path = "Resources/Images/ScannerButton.png";
        try {
            stream = new FileInputStream(
                    path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image scannerImage = new Image(stream);
        ImageView scannerImageView = new ImageView(scannerImage);
        scannerImageView.setFitWidth(57);
        scannerImageView.setFitHeight(100);
        Button scannerButton = new Button();
        scannerButton.setPrefSize(50,100);
        scannerButton.setLayoutX(605);
        scannerButton.setLayoutY(340);
        scannerButton.setGraphic(scannerImageView);
        scannerButton.setStyle("-fx-background-color: #555659");



        //Get safe image
        try {
            stream = new FileInputStream(
                    "Resources/Images/safeClose.PNG");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image safeCloseImg = new Image(stream, width, height, false, false);
        ImageView safeCloseView = new ImageView(safeCloseImg);

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        hbox1.getChildren().addAll(buttonArrayList.get(1), buttonArrayList.get(2), buttonArrayList.get(3));
        hbox2.getChildren().addAll(buttonArrayList.get(4), buttonArrayList.get(5), buttonArrayList.get(6));
        hbox3.getChildren().addAll(buttonArrayList.get(7), buttonArrayList.get(8), buttonArrayList.get(9));
        hbox4.getChildren().addAll(buttonArrayList.get(10), buttonArrayList.get(0), buttonArrayList.get(11));


        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4);
        vbox.setPrefSize(215, 290);

        grayBackground.setX(360);
        grayBackground.setY(207);
        blackBackground.setX(grayBackground.getX()+10);
        blackBackground.setY(grayBackground.getY()+10);

        vbox.setTranslateX(blackBackground.getX());
        vbox.setTranslateY(blackBackground.getY() + lcdDisplayHeight);
        System.out.println(grayBackground.getX());

        lcdScreenX = blackBackground.getX();
        lcdScreenY = blackBackground.getY();
        lcdScreenWidth = blackBackground.getWidth();
        lcdScreenHeight = blackBackground.getHeight();

        /*
         * Setup the root of the scene graph
         */
        Label text = new Label ("hello");

        BorderPane keypadPane = new BorderPane(text);
        keypadPane.getChildren().addAll(grayBackground, blackBackground,vbox);


        /*
           On/Off button
         */
//        Rectangle onOffBtn = new Rectangle(50,50);
        onOffBtn.setTranslateX(620);
        onOffBtn.setTranslateY(510);
        onOffBtn.setFill(Color.TRANSPARENT);
        onOffBtn.setOnMouseClicked(e -> {
            this.powerSensor.setPower(!this.powerSensor.hasPower());
            try {
                if (this.powerSensor.hasPower()) {
                    this.soundSensor.playSound(Sound.On);
                } else {
                    this.soundSensor.playSound(Sound.Off);
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });


        Ellipse powerDisplay = this.powerSensor.getView();
        powerDisplay.setFill(Color.RED);
        powerDisplay.setTranslateX(645);
        powerDisplay.setTranslateY(534);

        ImageView soundDisplay = soundSensor.getView();
        soundDisplay.setTranslateX(0);
        soundDisplay.setTranslateY(50);

        pane.getChildren().addAll(background, safeCloseView, keypadPane, powerDisplay, onOffBtn, soundDisplay);
        updateLCDDisplay("", pane );
        displaySelectFingerPrintButtons(pane);
        return pane;
    }


    /**
     * Display the unlocked/opened safe
     * Close-safe button
     */
    public void openSafe(){
        close = new Button("Wanna close your safe?");
        pane.getChildren().clear();
        InputStream stream = null;
        //Get safe image
        try {
            stream = new FileInputStream(
                    "Resources/Images/safeOpen.PNG");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image safeOpenImg = new Image(stream, width, height, false, false);
        ImageView safeOpenView = new ImageView(safeOpenImg);
        Ellipse powerDisplay = this.powerSensor.getView();
        powerDisplay.setFill(Color.RED);
        powerDisplay.setTranslateX(720);
        powerDisplay.setTranslateY(477);
        pane.getChildren().addAll(background, safeOpenView, close, powerDisplay);
    }


    /**
     * Display input on LCD display
     * @param input input to display
     * @param pane user's interface pane
     */
    public void updateLCDDisplay(String input, Pane pane){
        //Create Display Backlight
        Rectangle backlight = new Rectangle(lcdScreenWidth - 20, 0.2*lcdScreenHeight);
        backlight.setX(lcdScreenX + 10);
        backlight.setY(lcdScreenY + 10);

        //Create Text for Display
        Text display = new Text();
        Font font;
        if (input.length()<7) {
             font = Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 24);
        }else if(input.length() > 20){
            font = Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 12);
            input = input.substring(0,20);
        }else{
            font = Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 12);
        }
        display.setFont(font);
        display.setX(lcdScreenX + 30);
        display.setY(lcdScreenY + 0.15 * lcdScreenHeight);
        display.setFill(Color.GHOSTWHITE);


        if(input.equals("off")){ // when display is off
            backlight.setFill(Color.BLACK);
            display.setText("");
        }else if (input.equals("nothing")){ // when safe is on but display shows nothing
            backlight.setFill(Color.BLUE);
            display.setText("");
        }else{
            backlight.setFill(Color.BLUE);
            display.setText(input);
        }

        pane.getChildren().addAll(backlight, display);
    }


    /**
     * Display finger print animation.
     * @param pane user's interface pane.
     */
    public void animateFingerPrint(Pane pane){
        //Get gif of fingerprint scanner
        InputStream stream = null;
        try {
            stream = new FileInputStream(
                    "Resources/Images/Finger-Print.gif");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Create ImageView and set location
        Image scannerAnimation = new Image(stream, 500, 500, true, false);
        ImageView scannerAnimationView = new ImageView(scannerAnimation);
        scannerAnimationView.setX(250);
        scannerAnimationView.setY(100);
        scannerAnimationView.setViewOrder(-1);

        //Add to Panel
        pane.getChildren().add(scannerAnimationView);
    }

    /**
     * Displays fingerprint buttons to select fingerprint to use in authorization.
     * @param pane
     */
    public void displaySelectFingerPrintButtons(Pane pane){
        InputStream stream = null;
        ImageView fingerPrintButtonIV[] = new ImageView[3];
        String names[] = {"Biraj", "Benathy", "Ruby"};
        String imageName;
        HBox box = new HBox();
        int w = 30;
        int h = 35;
        for(int i = 0; i < 3; i++){
            //Get safe image
            imageName = "Fingerprint" + (i+1) + ".png";
            try {
                stream = new FileInputStream(
                        "Resources/Images/" + imageName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Image image = new Image(stream, 75, 100, false, false);
            fingerPrintButtonIV[i] = new ImageView(image);
            fingerPrintButtonIV[i].setFitHeight(h);
            fingerPrintButtonIV[i].setFitWidth(w);

            //Create button
            fingerPrintButton[i] = new Button(names[i]);
//            fingerPrintButton[i].setPrefSize(w,h);
            fingerPrintButton[i].setGraphic(fingerPrintButtonIV[i]);
            fingerPrintButton[i].setViewOrder(-1);

            box.getChildren().add(fingerPrintButton[i]);
        }

        //Add to Panel
        pane.getChildren().add(box);
    }

    /**
     * @return all the buttons
     */
    public ArrayList<Button> getButtons(){
        return buttonArrayList;
    }

}
