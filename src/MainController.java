import Sensors.SoundSensor.Sound;
import Sensors.SoundSensor.SoundSensor;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainController extends Application {
    private SoundSensor soundSensor;
    public static void main(String[] args) {
        System.out.println("hello");
        System.out.println("Hey can you accept my change?");
        System.out.println("ruby is here");
        System.out.println("hey what's up");
        System.out.println("Please make me a contributer!");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*
         * Setup
         */
        double width  = 500;
        double height = 500;
        primaryStage.setTitle("HomeSafe");

        /*
         * Setup the root of the scene graph
         */
        Label text = new Label ("hello");
        Pane pane = new Pane(text);
        System.out.println("Joe");

        /*
         * Example of SoundSensor for testing. Uncomment to use.
         */
//        this.soundSensor = new SoundSensor();
//        VBox box = new VBox();
//        // Create buttons to play sounds
//        Button on = this.SoundButtonExample(Sound.On, "On");
//        Button off = this.SoundButtonExample(Sound.Off, "Off");
//        Button beep = this.SoundButtonExample(Sound.Beep, "Beep");
//        // To show icon all you have to do is get the view from the soundSensor object.
//        // The icon will change automatically
//        box.getChildren().addAll(on, off, beep, this.soundSensor.getView());
//        pane.getChildren().add(box);

        /*
         * Set the scene
         */
        gui.updateLCDDisplay("test", pane );
        gui.animate
        Scene scene = new Scene(pane, width, height);



        primaryStage.setScene(scene);
        primaryStage.show();
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
