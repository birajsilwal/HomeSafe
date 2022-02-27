import Sensors.PowerSensor.PowerSensor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Sensors.SoundSensor.Sound;
import Sensors.SoundSensor.SoundSensor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Scanner;


public class MainController extends Application {
    private SoundSensor soundSensor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        double width  = 1000;
        double height = 680;

        SoundSensor soundSensor = new SoundSensor();
        PowerSensor powerSensor = new PowerSensor();
        GUI gui = new GUI(soundSensor, powerSensor);
        Pane pane = new Pane();


        pane = gui.createSafeInterface();
        /*
         * Set the scene
         */
        gui.updateLCDDisplay("test", pane );


        /*
         * Example of SoundSensor for testing. Uncomment to use.
         */
        this.soundSensor = new SoundSensor();
        VBox box = new VBox();
        // Create buttons to play sounds
        Button on = this.SoundButtonExample(Sound.On, "On");
        Button off = this.SoundButtonExample(Sound.Off, "Off");
        Button beep = this.SoundButtonExample(Sound.Beep, "Beep");
        // To show icon all you have to do is get the view from the soundSensor object.
        // The icon will change automatically
        box.getChildren().addAll(on, off, beep, this.soundSensor.getView());
        pane.getChildren().add(box);


        Scene scene = new Scene(pane, width, height);



        primaryStage.setScene(scene);
        primaryStage.show();

        InputController inputController = new InputController(gui,pane);
        inputController.startSetUp();
    }


    private Button SoundButtonExample(Sound s, String text){
        Button b = new Button(text);
        b.setOnAction(e -> {
            try {
                this.soundSensor.playSound(s);
                /*
                You can put other stuff here for the button to do. This is just an example.
                 */
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        b.setFocusTraversable(false);
        return b;
    }
}
