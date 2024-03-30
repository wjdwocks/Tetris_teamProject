package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {
    //PlayManager handles basic game elements such as minos, blocks, and the play area

    //Main Game Area
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;
    //Others
    public static int drop_Interval = 60; // mino drops in every 60frames
    public static ArrayList<Block> staticBlocks = new ArrayList<>();
    final int WIDTH = 300;
    final int HEIGHT = 600;

    //Mino
    final int MINO_START_X;
    final int MINO_START_Y;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;

    //Effect
    public boolean effectCounteron;
    Mino currentMino;
    Mino nextMino;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();
    boolean gameover;

    // Score
    int level = 1;
    int lines;
    int score;

    public PlayManager() {
        //Main Play Area Frame

        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); // Adjusted for new width
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;
        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE; // Adjusted for new width
        MINO_START_Y = top_y + Block.SIZE;
        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;


        //Set the Starting Mino
        currentMino = pickMino();
        //currentMino = new Mino_L2();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
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
        // Check if he currentMino is active
        if (!currentMino.active) {
            // if the mino is not active, put it into the staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            //check if the game is over
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                //this means the currentMino immediately collided a block and couldn't move at all
                // so it's xy are the same with the nextMino's
                gameover = true;
                GamePanel.music.stop();
                GamePanel.se.play(2, false);
            }

            currentMino.deactivating = false;

            // replace the currentMino with the nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            //when a mino becomes inactive, check if line(s) can be deleted
            checkDelete();
        } else {
            currentMino.update();
        }
    }


    private void checkDelete() {
        int blocksPerLine = WIDTH / Block.SIZE; // Blocks per line에 맞게 수정될수있도록 코드 추가
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;


        while (x < right_x && y < bottom_y) {
            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    //increase the count if there is a static block
                    blockCount++;
                }
            }
            x += Block.SIZE;
            if (x == right_x) {
                if (blockCount == blocksPerLine) {
                    effectCounteron = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        // remove all the block in the current y line
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;

                    //Drop Speed
                    // if the line score hits a certain number, increase the drop speed
                    // 1 is the fastest
                    if (lines % 10 == 0 && drop_Interval > 1)
                        level++;
                    if (drop_Interval > 10) {
                        drop_Interval -= 10;
                    } else {
                        drop_Interval -= 1;
                    }


                    // a line has been deleted so need to slide down blocks that are above it
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        // if a block is above the current y, move it down by the block size
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
        // Add Score
        if (lineCount > 0) {
            GamePanel.se.play(1, false);
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
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

        // Draw Score Frame
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y);
        y += 70;
        g2.drawString("LINES: " + lines, x, y);
        y += 70;
        g2.drawString("SCORE: " + score, x, y);

        // Draw the currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        //Draw the nextMino
        nextMino.draw(g2);

        //Draw Static Blocks
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        // Draw Effect
        if (effectCounteron) {
            effectCounter++;

            g2.setColor(Color.red);
            for (int i = 0; i < effectY.size(); i++) {
                g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
            }

            if (effectCounter == 10) {
                effectCounteron = false;
                effectCounter = 0;
                effectY.clear();
            }

        }

        // Draw Pause, gameover
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (gameover) {
            x = left_x + 25;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        } else if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        // Draw the GAME TITLE
        x = 35;
        y = top_y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
        g2.drawString("Simple Tetris", x + 20, y);


    }
}
