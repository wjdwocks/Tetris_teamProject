package main;

import mino.*;

import java.awt.*;
import java.util.Random;

public class PlayManager {

    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;
    //Others
    public static int drop_Interval = 60; // mino drops in every 60frames
    //Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    final int MINO_START_X;
    final int MINO_START_Y;
    //Mino
    Mino currentMino;

    public PlayManager() {
        //Main Play Area Frame

        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); //1280/2-360/2=460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;
        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        //Set the Starting Mino
        currentMino = pickMino();
        //currentMino = new Mino_L2();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
    }

    private Mino pickMino() {
        // Pick a random mino
        Mino mino = null;
        int i = new Random().nextInt(7);
        mino = switch (i) {
            case 0 -> new Mino_L1();
            case 1 -> new Mino_L2();
            case 2 -> new Mino_Square();
            case 3 -> new Mino_Bar();
            case 4 -> new Mino_T();
            case 5 -> new Mino_Z1();
            case 6 -> new Mino_Z2();
            default -> mino;
        };
        return mino;
    }

    public void update() {
        currentMino.update();

    }

    public void draw(Graphics2D g2) {
        // Draw Play Area Frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Draw Next Mino Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        // Draw the currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }
    }
}
