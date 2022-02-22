import Sensors.PowerSensor.PowerSensor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Sensors.SoundSensor.SoundSensor;

import java.util.ArrayList;


public class MainController extends Application {

    public ArrayList<Integer> password;

    public static void main(String[] args) {
        System.out.println("hello");
        System.out.println("Hey can you accept my change?");
        System.out.println("Check");
        System.out.println("ruby is here!!!!!!!!!!!!1");
        System.out.println("ruby is here");
        System.out.println("hey what's up");
        System.out.println("Please make me a contributer!");
        System.out.println("nemo was here123 d");
        System.out.println("no");
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

        pane = gui.createSafeInterface();

        /*
         * Set the scene
         */

        gui.updateLCDDisplay("test", pane);

        Scene scene = new Scene(pane, width, height);



        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Get all the keys user presses before pressing 'Enter'
    public void setPassword(ArrayList<Integer> pw){
        password = pw;
        for (int i = 0; i < password.size(); i++) {
            System.out.print(password.get(i));
        }
        System.out.println();
    }
}
