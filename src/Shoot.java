import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Shoot implements Runnable {
        public void run() {
            String bip = "src/sounds/shoot.mp3";
            Media hit = new Media(new File(bip).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        }
}
