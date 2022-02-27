import Sensors.PowerSensor.PowerSensor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Sensors.SoundSensor.Sound;
import Sensors.SoundSensor.SoundSensor;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class MainController extends Application {
    private SoundSensor soundSensor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        double width  = 1000;
        double height = 680;

        SoundSensor soundSensor = new SoundSensor();
        PowerSensor powerSensor = new PowerSensor();
        GUI gui = new GUI(soundSensor, powerSensor);

        Pane pane = gui.createSafeInterface();
        /*
         * Set the scene
         */
        Scene scene = new Scene(pane, width, height);



        primaryStage.setScene(scene);
        primaryStage.show();

        SecurityManager securityManager = new SecurityManager();
        InputController inputController = new InputController(gui,pane, soundSensor);
        powerSensor.setOnAction(value -> {
            inputController.setKeyPressDisable(!value);
            if (value) {
                if (inputController.getState() == STATE.SETUP) {
                    inputController.startSetUp();
                } else {
                    inputController.startAuthorization();
                }
            } else {
                gui.updateLCDDisplay("off", pane);
                inputController.clearInput();
            }
        });

        inputController.setKeyPressDisable(!powerSensor.hasPower());
    }


    /**
     * Get corresponding sound for buttons
     * @param s sound
     * @param text text corresponding with sounds
     * @return button
     */
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
