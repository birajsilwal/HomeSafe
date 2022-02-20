import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController extends Application {
    public static void main(String[] args) {
        System.out.println("hello");
        System.out.println("Hey can you accept my change?");
        System.out.println("ruby is here");
        System.out.println("nemo was here123 d");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        double width  = 1000;
        double height = 680;
        GUI gui = new GUI();
        Pane pane = new Pane();
        pane = gui.createSafeInterface();
        /*
         * Set the scene
         */
        gui.updateLCDDisplay("test", pane );
        Scene scene = new Scene(pane, width, height);



        primaryStage.setScene(scene);
        primaryStage.show();
    }
}