import Sensors.PowerSensor.PowerSensor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Sensors.SoundSensor.SoundSensor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainController extends Application {

    public ArrayList<Integer> password;

    public static void main(String[] args) {
        System.out.println("hello");
        System.out.println("Hey can you accept my change?");
        System.out.println("Check");
        System.out.println("ruby is here!!!!!!!!!!!!1");
        System.out.println("ruby is here");
        System.out.println("nemo was here123 d");
        System.out.println("no");
        System.out.println("hey what's up");
        System.out.println("Please make me a contributer!");
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
