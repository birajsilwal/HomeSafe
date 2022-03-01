package Sensors.SoundSensor;

import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.FileNotFoundException;

public class SoundSensor {
    private final ImageView view;
    private Sound recentSound;

    public SoundSensor() {
        this.view = new ImageView();
        this.view.setImage(SoundImage.getOffImage());
    }

    /**
     * Method used to play sound clip.
     *
     * @param s is a sound enum for the clip to be played
     * @throws Exception
     */
    public void playSound(Sound s) throws Exception {
        File f = null;
        switch (s) {
            case Beep -> f = SoundFiles.BEEP;
            case On -> f = SoundFiles.ON;
            case Off -> f = SoundFiles.OFF;
        }
        if (f == null) {
            throw new FileNotFoundException("No media file found for " + s);
        }
        this.recentSound = s;
        AudioClip audio = new AudioClip(f.toURI().toURL().toString());
        audio.play();
        Thread t = new Thread(() -> {
            while (audio.isPlaying()) {
                if (s != recentSound) {
                    audio.stop();
                    return;
                }
                this.view.setImage(SoundImage.getOnImage());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.view.setImage(SoundImage.getOffImage());
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Call this method to get a image view to display in the ui.
     * This view will automatically show a on/off icon for sound playing.
     *
     * @return ImageView
     */
    public ImageView getView() {
        return this.view;
    }
}
