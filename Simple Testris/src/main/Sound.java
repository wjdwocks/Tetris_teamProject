package main;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {
    Clip musicClip;
    URL[] url = new URL[10];

    public Sound() {
        url[0] = getClass().getResource("/resource/white-labyrinth-active.wav");
        url[1] = getClass().getResource("/resource/delete line.wav");
        url[2] = getClass().getResource("/resource/gameover.wav");
        url[3] = getClass().getResource("/resource/rotation.wav");
        url[4] = getClass().getResource("/resource/touch floor.wav");
    }

    public void play(int i, boolean music) {
        try {
            if (url[i] == null) {
                System.err.println("Sound file at index " + i + " not found.");
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();
            if (music) {
                musicClip = clip;
            }
            clip.open(ais);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
            ais.close();
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (musicClip == null) {
            System.err.println("Music clip not initialized.");
            return;
        }
        musicClip.stop();
        musicClip.close();
    }

    public void loop() {
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }


}
