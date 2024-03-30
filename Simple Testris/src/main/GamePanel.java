package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static Sound music = new Sound();
    public static Sound se = new Sound(); //sound effect
    final int FPS = 60;
    Thread gameThread; // to run the game loop
    PlayManager pm;


    public GamePanel() {
        //Panel Settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);

        //Implement KeyListener
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        pm = new PlayManager();

    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();

        music.play(0, true);
        music.loop();
    }

    @Override
    public void run() {
        // Game loop
        // You can study this part at https://www.youtube.com/watch?v=VpH33Uw-_0E

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    private void update() { // update the game
        if (!KeyHandler.pausePressed && !pm.gameover) {
            pm.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }
}
