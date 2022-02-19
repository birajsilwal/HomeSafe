import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController extends Application {
    public static void main(String[] args) {
        System.out.println("hello");
        System.out.println("ruby is here");
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

        /*
         * Set the scene
         */
        Scene scene = new Scene(pane, width, height);



        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
