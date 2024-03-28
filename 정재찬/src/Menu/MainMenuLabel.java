package Menu;

import component.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class MainMenuLabel extends JPanel implements KeyListener {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    private int currentIndex = 0; // 현재 선택된 메뉴 인덱스
    private final String cursorSymbol = "> "; // 현재 선택된 메뉴룰 따라갈 커서
    private final String nonSelected = "  "; // 커서가 있을 위치
    private final String[] labels = {"Start", "Settings", "Exit"}; // 메인 메뉴에 있을 서브 메뉴들.
    java.util.List<JLabel> menuItems;
    public final JLabel mainLabel;
    private JLabel keyMessage;
    private javax.swing.Timer messageTimer;
    public MainMenuLabel() {
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLayout(null);

        menuItems = new ArrayList<>();

        ImageIcon backgroundIcon = new ImageIcon(Main.class.getResource("../images/introBackground.jpg"));
        mainLabel = new JLabel(new ImageIcon(backgroundIcon.getImage().getScaledInstance(SCREEN_WIDTH, SCREEN_HEIGHT, Image.SCALE_SMOOTH)));
        mainLabel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        JLabel title = new JLabel("Tetris Game");
        title.setFont(new Font("Arial", Font.BOLD, 40)); // 폰트 설정
        title.setForeground(Color.BLACK); // 텍스트 색상 설정
        title.setBounds(50, SCREEN_HEIGHT / 8, 400, 50); // 위치와 크기 설정
        mainLabel.add(title);

        int Start_y = SCREEN_HEIGHT * 5 / 9;
        for (String i : labels) {
            addMenuItem(i, Start_y);
            Start_y += 50;
        }

        keyMessage = new JLabel(" ");
        keyMessage.setFont(new Font("Arial", Font.BOLD, 40)); // 폰트 설정
        keyMessage.setForeground(Color.BLACK); // 텍스트 색상 설정
        keyMessage.setBounds(SCREEN_WIDTH/2 - 300, SCREEN_HEIGHT / 2 - 100, 600, 100); // 위치와 크기 설정
        add(keyMessage);

        messageTimer = new javax.swing.Timer(3000, e -> keyMessage.setVisible(false));
        messageTimer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정

        updateMenuDisplay(); // 메뉴 디스플레이 업데이트

        add(mainLabel);

        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
    }

    private void updateMenuDisplay() {
        for (int i = 0; i < menuItems.size(); i++) {
            if (i == currentIndex) {
                menuItems.get(i).setText(cursorSymbol + labels[i]);
            } else {
                menuItems.get(i).setText(nonSelected + labels[i]);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                currentIndex = (currentIndex - 1 + menuItems.size()) % menuItems.size();
                break;
            case KeyEvent.VK_S:
                currentIndex = (currentIndex + 1) % menuItems.size();
                break;
            case KeyEvent.VK_ENTER:
                activateMenuItem(currentIndex);
                break;
            default:
                showTemporaryMessage("<html>Invalid Key Input. <br>Please press W, S, Space, ESC</html>");
                break;
        }
        updateMenuDisplay();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void activateMenuItem(int index) {
        switch (index) {
            case 0: // Start
                System.out.println("Start Selected");
                Board game = new Board();
                game.timer.start(); // 타이머 시작
                game.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                game.setVisible(true);
                Main.mainPanel.setVisible(false);
                // 여기서 다음 화면으로 넘어가는 로직을 구현
                break;
            case 1: // Settings 버튼을 누르면 Setting을 하는 화면으로 넘어감.
                System.out.println("Settings Selected");
                switchToScreen(Main.optionMenu);
                // 설정 화면 로직 구현
                break;
            case 2: // Exits
                System.out.println("Exit Selected");
                System.exit(0);
                // 컨트롤 설명 화면 로직 구현
                break;
        }
    }

    private void addMenuItem(String text, int y) {
        JLabel menuItem = new JLabel(text);
        menuItem.setFont(new Font("Arial", Font.BOLD, 30)); // 폰트 설정
        menuItem.setForeground(Color.BLACK); // 텍스트 색상 설정
        menuItem.setBounds(50, y, 200, 30); // 위치와 크기 설정
        menuItems.add(menuItem);
        mainLabel.add(menuItem);
    }


    private void showTemporaryMessage(String message)
    { // 화면에 키입력 메시지를 띄움
        keyMessage.setText(message); // 메시지 표시
        keyMessage.setVisible(true); // 라벨을 보이게 설정
        messageTimer.restart(); // 타이머 시작 (이전 타이머가 실행 중이었다면 재시작)
    }

    public void switchToScreen(JPanel newScreen) {
        Main.cardLayout.show(Main.mainPanel, newScreen.getName()); // 화면 전환
        System.out.println(newScreen.getName());
        newScreen.setFocusable(true); // 새 화면이 포커스를 받을 수 있도록 설정
        newScreen.requestFocusInWindow(); // 새 화면에게 포커스 요청
    }
}