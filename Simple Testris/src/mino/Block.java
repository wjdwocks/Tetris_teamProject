package mino;

import java.awt.*;

public class Block extends Rectangle {
    public static final int SIZE = 30; //30X30 block
    public int x, y;
    public Color c;

    public Block(Color c) {
        this.c = c;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(c);
        g2.setFont(new Font("Courier", Font.BOLD, SIZE));
        g2.drawString("0", x, y + SIZE - 5); // Adjust the y position to align the text in the center of the block
    }
}
