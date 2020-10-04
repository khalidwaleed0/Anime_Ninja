package AnimeNinja;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundPlayer implements Runnable{

    @Override
    public void run() {
        try {
            InputStream fis = SoundPlayer.class.getResourceAsStream("/resources/sharingan.mp3");
            BufferedInputStream bis = new BufferedInputStream(fis);
            Player player = new Player(bis);
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
}
