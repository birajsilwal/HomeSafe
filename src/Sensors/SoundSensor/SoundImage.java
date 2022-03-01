package Sensors.SoundSensor;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SoundImage {
    public static Image getOffImage() {
        try {
            return new Image(new FileInputStream("assets/SoundIcons/off.png"));
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    public static Image getOnImage() {
        try {
            return new Image(new FileInputStream("assets/SoundIcons/on.png"));
        } catch (FileNotFoundException e) {

        }
        return null;
    }
}
