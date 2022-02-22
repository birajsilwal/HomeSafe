import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

public class GUI extends InputController{
    private double lcdScreenX;
    private double lcdScreenY;
    private double lcdScreenWidth;
    private double lcdScreenHeight;
    ArrayList<Button> imgArray = new ArrayList<>();
    private String displayText = "";

    public Pane createSafeInterface(){
        double width  = 1000;
        double height = 680;

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
            imgArray.add(button);
        }

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
        pane.getChildren().addAll(background, safeCloseView, keypadPane);
        return pane;
    }

    public void updateLCDDisplay(String input, Pane pane){
        //Create Display Backlight
        Rectangle backlight = new Rectangle(lcdScreenWidth - 20, 0.2*lcdScreenHeight);
        backlight.setX(lcdScreenX + 10);
        backlight.setY(lcdScreenY + 10);

        //Create Text for Display
        Text display = new Text();
        Font font;
        if (displayText.length()<7) {
             font = Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 24);
        }
        else font = Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 12);
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

    boolean keyPressDisable = true;

    public void listenKeyPress(Pane pane) {
        keyPressDisable = false;
        AnimationTimer timer = new AnimationTimer() {
            private long start;
            @Override
            public void handle(long now) {
                if (start==0L) start = now;
                else{
                    if ((now-start>=10_000_000_000L)){
                        System.out.println("Came here");
                        displayForThreeSeconds("TimeOut",pane);
                        keyPressDisable = true;
                        this.stop();
                    }
                    else{
                        imgArray.get(0).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "0";
                                    readKey("0");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(1).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "1";
                                    readKey("1");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(2).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "2";
                                    readKey("2");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(3).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "3";
                                    readKey("3");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(4).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if (!keyPressDisable) {
                                    displayText += "4";
                                    readKey("4");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(5).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "5";
                                    readKey("5");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(6).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "6";
                                    readKey("6");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(7).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "7";
                                    readKey("7");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(8).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "8";
                                    readKey("8");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(9).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!keyPressDisable) {
                                    displayText += "9";
                                    readKey("9");
                                    updateLCDDisplay(displayText, pane);
                                    start = now;
                                }
                            }
                        });
                        imgArray.get(10).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                displayText = displayText.substring(0,displayText.length()-1);
                                entered_password = entered_password.substring(0,entered_password.length()-1);
                                updateLCDDisplay(displayText,pane);
                                start = now;
                            }
                        });
                        imgArray.get(11).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if (displayText.length()==6){
                                    System.out.println(displayText);
                                    System.out.println("Length 4 condition passed");
                                    if (checkPassword()){
                                        displayForThreeSeconds("Authorized", pane);
                                        keyPressDisable = true;
                                    }
                                    else {
                                        displayForThreeSeconds("Re-enter Password",pane);
                                        start = now;
                                    }
                                }
                                else if (displayText.length()==20){
                                    System.out.println("Length 4 condition failed");
                                    displayText = "";
                                    checkResetPin();
                                }
                            }
                        });
                    }
                }
            }

            private void displayForThreeSeconds(String message, Pane pane) {
                displayText = message;
                updateLCDDisplay(displayText,pane);
                entered_password = "";
                AnimationTimer timer1 = new AnimationTimer() {
                    private long start1;
                    @Override
                    public void handle(long l) {
                        if (start1==0L) start1 = l;
                        else{
                            if (l-start1>2_000_000_000){
                                displayText = "";
                                updateLCDDisplay(displayText,pane);
                                this.stop();
                            }
                        }
                    }
                };
                timer1.start();
            }
        };
        timer.start();
    }
}
