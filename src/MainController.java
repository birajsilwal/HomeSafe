import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainController extends Application {
    public static void main(String[] args) {
        System.out.println("hello");
        System.out.println("Hey can you accept my change?");
        System.out.println("ruby is here");
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
