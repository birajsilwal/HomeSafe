import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class GUI {
    private double lcdScreenX;
    private double lcdScreenY;
    private double lcdScreenWidth;
    private double lcdScreenHeight;


    public Pane createSafeInterface(){
        double width  = 1000;
        double height = 680;

        //get button images
        int imgSize = 55;
        ArrayList<Button> imgArray = new ArrayList<>();
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
            imgArray.add(button);
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
        hbox1.getChildren().addAll(imgArray.get(1), imgArray.get(2), imgArray.get(3));
        hbox2.getChildren().addAll(imgArray.get(4), imgArray.get(5), imgArray.get(6));
        hbox3.getChildren().addAll(imgArray.get(7), imgArray.get(8), imgArray.get(9));
        hbox4.getChildren().addAll(imgArray.get(10), imgArray.get(0), imgArray.get(11));


        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4);
        vbox.setPrefSize(215, 290);

        int lcdDisplayHeight = 100;
        Rectangle background = new Rectangle(width, height + lcdDisplayHeight, Color.LIGHTGREY);
        Rectangle grayBackground = new Rectangle(235, 281 + lcdDisplayHeight, Color.GREY);
        Rectangle blackBackground = new Rectangle(215, 260 + lcdDisplayHeight, Color.BLACK);
        grayBackground.setX(grayBackground.getX()+360);
        grayBackground.setY(grayBackground.getY()+207);
        blackBackground.setX(grayBackground.getX()+10);
        blackBackground.setY(grayBackground.getY()+10);
        vbox.setTranslateX(blackBackground.getX());
        vbox.setTranslateY(blackBackground.getY() + lcdDisplayHeight);

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
        Pane pane = new Pane(text);
        pane.getChildren().addAll(background, safeCloseView, keypadPane, scannerButton);
        return pane;
    }

    public void updateLCDDisplay(String input, Pane pane){
        //Create Display Backlight
        Rectangle backlight = new Rectangle(lcdScreenWidth - 20, 0.2*lcdScreenHeight);
        backlight.setX(lcdScreenX + 10);
        backlight.setY(lcdScreenY + 10);

        //Create Text for Display
        Text display = new Text();
        Font font = Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 24);
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


}
