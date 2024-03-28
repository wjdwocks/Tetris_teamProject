package Menu;

import component.Board;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static JFrame frame;
    public static JPanel mainPanel;
    public static CardLayout cardLayout;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static MainMenuLabel mainMenu;
    public static OptionsLabel optionMenu;

    public static void main(String[] args) {
        frame = new JFrame("Main Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setLocationRelativeTo(null);

        // 메인 패널 초기화 및 레이아웃 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel();
        //
        mainPanel.setLayout(cardLayout);

        // 메뉴와 옵션 패널 추가
        mainMenu = new MainMenuLabel();
        mainMenu.setName("MainMenu");
        optionMenu = new OptionsLabel();
        optionMenu.setName("Options");

        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(optionMenu, "Options");

        cardLayout.show(mainPanel, "MainMenu");

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
