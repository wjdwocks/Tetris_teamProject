package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Simple Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Add GamePanel to the window
        GamePanel gp = new GamePanel(); // create a GamePanel(=JPanel)
        window.add(gp); // add the GamePanel to the window(=JFrame)
        window.pack(); // resize the window to fit the GamePanel

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gp.launchGame();


    }
}