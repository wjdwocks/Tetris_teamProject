package seoultech.se.tetris.component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import seoultech.se.tetris.blocks.Block;
import seoultech.se.tetris.blocks.IBlock;
import seoultech.se.tetris.blocks.JBlock;
import seoultech.se.tetris.blocks.LBlock;
import seoultech.se.tetris.blocks.OBlock;
import seoultech.se.tetris.blocks.SBlock;
import seoultech.se.tetris.blocks.TBlock;
import seoultech.se.tetris.blocks.ZBlock;



// JFrame 상속받은 클래스 Board
public class Board extends JFrame {

	private static final long serialVersionUID = 2434035659171694595L; // 이 클래스의 고유한 serialVersionUID
	//직렬화 역직렬화 과정에서 클래스 버전의 호환성 유지하기 위해 사용됨.
	
	public static final int HEIGHT = 20; // 높이
	public static final int WIDTH = 10; // 너비
	public static final char BORDER_CHAR = 'X'; //게임 테두리 문자
	
	private JTextPane pane; //게임 상태 표시하는 JTextPane 객체
	private int[][] board; // 게임 보드의 상태를 나타내는 2차원 배열
	private KeyListener playerKeyListener; // 사용자의 키 입력을 처리하는 KeyListener 객체
	private SimpleAttributeSet styleSet; // 텍스트 스타일 설정하는 SimpleAttributeSet
	private Timer timer; // 블록이 자동으로 아래로 떨어지게 하는 Timer
	private Block curr; // 현재 움직이고 있는 블록
	private Block nextcurr; // 다음 블럭
	int x = 3; //Default Position. 현재 블록 위치
	int y = 0; // 현재 블록 위치



	int scores = 0; // 현재 스코어
	int level = 0; // 현재 레벨
	int lines = 0; // 현재 지워진 라인 수
	int bricks = 0; // 생성된 벽돌의 개수
	
	private static final int initInterval = 1000; //블록이 자동으로 아래로 떨어지는 속도 제어 시간, 현재 1초
	
	// 생성자 Board, 게임 창 설정 및 초기게임 보드 준비, 첫 번째 블록 생성하고, 타이머 시작
	public Board() {
		super("3조 테트리스 게임"); //창의 제목을 "SeoulTech SE Tetris"로 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫을 때 프로그램이 종료되도록 설정
		
		//Board display setting.
		pane = new JTextPane(); // 텍스트 패널 생성
		pane.setEditable(false); // 텍스트 패널 편집 불가하도록 설정
		pane.setBackground(Color.BLACK); // 텍스트 패널의 배경색을 검은색으로 설정
		CompoundBorder border = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY, 10),
				BorderFactory.createLineBorder(Color.DARK_GRAY, 5)); // 복합 테두리 생성
		pane.setBorder(border); // 텍스트 패널에 테두리를 설정
		Border innerPadding = new EmptyBorder(5, 20, 5, 0); // 상단, 왼쪽, 하단, 오른쪽 여백 설정

// 기존 복합 테두리와 내부 여백을 결합한 새로운 복합 테두리 생성
		CompoundBorder newBorder = new CompoundBorder(border, innerPadding);

// 텍스트 패널에 새로운 테두리 설정
		pane.setBorder(newBorder);
		this.getContentPane().add(pane, BorderLayout.CENTER); // 텍스트 패널을 창의 중앙에 추가.this는 Board클래스의 인스턴스를 지칭
		
		//Document default style.
		styleSet = new SimpleAttributeSet(); // 스타일 설정을 위한 객체 생성
		StyleConstants.setFontSize(styleSet, 25); // 폰트 크기를 18로 설정
		StyleConstants.setFontFamily(styleSet, "Consolas");// 폰트 종류를 mac은 Courier로 설정, window는 consolas로 설정
		StyleConstants.setBold(styleSet, true); // 폰트를 굵게 설정
		StyleConstants.setForeground(styleSet, Color.WHITE); // 폰트 색상을 흰색으로 설정



		StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_LEFT); // 텍스트 정렬을 가운데로 설정
		
		//Set timer for block drops.
		timer = new Timer(initInterval, new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown(); // 블록 아래로 이동
				drawBoard(); // 보드 그리기
			}
		});
		
		//Initialize board for the game.
		board = new int[HEIGHT][WIDTH]; // 게임 보드 초기화
		playerKeyListener = new PlayerKeyListener(); // 플레이어 키 리스너를 생성
		addKeyListener(playerKeyListener); //키 리스너 추가
		setFocusable(true); // 키 입력을 받을 수 있도록 설정
		requestFocus(); //  입력 포커스 요청
		
		//Create the first block and draw.
		curr = getRandomBlock(); // 첫 번째 블록을 무작위로 선택
		nextcurr = getRandomBlock(); // 다음 블록을 무작위로 선택

		placeBlock(); //  선택된 블록을 배치합니다.
		drawBoard(); // 보드를 그린다.
		timer.start(); // 타이머 시작
	}

	private Block getRandomBlock() {
		Random rnd = new Random(System.currentTimeMillis()); // 현재 시간 기준으로 랜덤 객체 생성
		int block = rnd.nextInt(7); // 0부터 7사이의 난수를 생성
		switch(block) {
		case 0:
			return new IBlock(); // I 모양 블록 생성 반환
		case 1:
			return new JBlock(); // J 모양 블록 생성 반환
		case 2:
			return new LBlock(); // L 모양 블록 생성 반환
		case 3:
			return new ZBlock(); // Z 모양 블록 생성 반환
		case 4:
			return new SBlock(); // S 모양 블록 생성 반환
		case 5:
			return new TBlock(); // T 모양 블록 생성 반환
		case 6:
			return new OBlock(); // O 모양 블록 생성 반환
		}
		return new LBlock(); // switch 문에서 선택되지 않은 경우 기본적으로 L 블록을 반환
	}

	
	// 현재 떨어지고 있는 블록(curr)을 게임보드(board)에 배치하고, JTextPane(pane)에 해당블록의 시각적 표현을 업데이트 하는 역할
	private void placeBlock() {
		StyledDocument doc = pane.getStyledDocument(); // 현재 JTextPane의 스타일이 적용된 문서를 가져옵니다.
		SimpleAttributeSet styles = new SimpleAttributeSet(); // 스타일 속성을 설정하기 위한 객체를 생성합니다.
		StyleConstants.setForeground(styles, curr.getColor()); // 현재 블록의 색상을 스타일 속성에 설정합니다.
		StyleConstants.setForeground(styles, nextcurr.getColor()); // 현재 블록의 색상을 스타일 속성에 설정합니다.
		for(int j=0; j<curr.height(); j++) { // 현재 블록의 높이만큼 반복합니다.
			int rows = y+j == 0 ? 0 : y+j-1; // 블록이 배치될 행 위치를 계산합니다.
			int offset = rows * (WIDTH+3) + x + 1; // JTextPane 내에서 블록이 시작될 위치를 계산합니다.
			doc.setCharacterAttributes(offset, curr.width(), styles, true); // 계산된 위치부터 블록의 너비만큼 스타일을 적용합니다.
			for(int i=0; i<curr.width(); i++) { // 현재 블록의 너비만큼 반복합니다.
				board[y+j][x+i] = curr.getShape(i, j); // 게임 보드 배열에 블록의 모양을 저장합니다.
			}
		}

	}

	// 게임보드에서 현재 블록의 위치를 "지우는"기능을 수행. 블록이 이동하거나 회전할 때 이전위치의 블록을 지우는 데 사용.
	// 게임보드(board) 배열에서 해당 블록이 차지하고 있던 위치를 0으로 설정하여, 그 위치에 블록이 없음을 표시
	private void eraseCurr() {
		for(int i=x; i<x+curr.width(); i++) { // 현재 블록의 너비만큼 반복합니다.
			for(int j=y; j<y+curr.height(); j++) { // 현재 블록의 높이만큼 반복합니다.
				board[j][i] = 0; // 게임 보드에서 현재 블록의 위치를 0으로 설정하여 지웁니다.
			}
		}
	}

	// 현재 블록을 아래로 이동할 수 있는지 확인하는 메소드
	private boolean canMoveDown() {
		if (y + curr.height() == HEIGHT) return false; // 바닥에 닿은 경우

		for (int i = 0; i < curr.width(); i++) {
			for (int j = 0; j < curr.height(); j++) {
				if (curr.getShape(i, j) != 0) { // 블록의 일부인 경우
					if (board[y + j + 1][x + i] != 0) { // 아래 칸이 비어있지 않은 경우
						return false; // 이동할 수 없음
					}
				}
			}
		}
		return true; // 모든 검사를 통과하면 이동할 수 있음
	}

	// 현재 블록을 아래로 한 칸 이동시킨다. 만약 블록이 바닥이나 다른 블록에 닿았다면, 그 위치에 블록을 고정하고 새로운 블록 생성
	protected void moveDown() {
		eraseCurr(); // 현재 블록의 위치를 게임 보드에서 지웁니다.
		if (canMoveDown()) { // 아래로 이동할 수 있는 경우
			y++; // 블록을 아래로 이동
		} else { // 이동할 수 없는 경우 (다른 블록에 닿거나 바닥에 닿은 경우)
			placeBlock(); // 현재 위치에 블록을 고정시킵니다.
			curr = nextcurr; // 다음블록을 현재 블록에 넣음
			nextcurr = getRandomBlock(); // 새로운 블록을 무작위로 가져옵니다.
			x = 3; // 새 블록의 시작 x 좌표를 설정합니다.
			y = 0; // 새 블록의 시작 y 좌표를 설정합니다.
		}
		placeBlock(); // 게임 보드에 현재 블록의 새 위치를 표시합니다.
	}


	// moveRight 메서드는 현재 블록을 오른쪽으로 한 칸 이동시킵니다. 블록이 오른쪽 벽에 닿지 않은 경우에만 이동이 가능합니다.
	protected void moveRight() {
		eraseCurr(); // 현재 블록의 위치를 게임 보드에서 지웁니다.
		if(x < WIDTH - curr.width()) x++; // 블록이 오른쪽 벽에 닿지 않았다면, x
		placeBlock(); // 게임 보드에 현재 블록의 새 위치를 표시합니다.
	}

	// moveLeft 메서드는 현재 블록을 왼쪽으로 한 칸 이동시킵니다. 블록이 왼쪽 벽에 닿지 않은 경우에만 이동이 가능합니다.
	protected void moveLeft() {
		eraseCurr(); // 현재 블록의 위치를 게임 보드에서 지웁니다.
		if(x > 0) {
			x--; // 블록이 왼쪽 벽에 닿지 않았다면, x 좌표를 1 감소시켜 왼쪽으로 이동합니다.
		}
		placeBlock(); // 게임 보드에 현재 블록의 새 위치를 표시합니다.
	}

	public void drawBoard() {
		StringBuffer sb = new StringBuffer(); // 문자열을 효율적으로 더하기 위한 StringBuffer 인스턴스 생성

		// 상단 경계선을 그립니다.
		for (int t = 0; t < WIDTH + 2; t++) sb.append(BORDER_CHAR); // 보드의 너비만큼 상단에 경계 문자(BORDER_CHAR)를 추가합니다.
		for (int i = 0; i < WIDTH; i++) sb.append(" ");// 게임부분과 NEXT블럭 부분 사이의 공백
		sb.append("----NEXT----");// NEXT블럭의 상단경계선- 4개, NEXT, - 4개
		sb.append("\n"); // 줄 바꿈을 추가하여 경계선 다음에 내용이 오도록 합니다.

		// 게임 보드의 각 행을 순회합니다.
		for (int i = 0; i < board.length; i++) {

				sb.append(BORDER_CHAR); // 각 행의 시작에 경계 문자를 추가합니다.

			// 게임 보드의 각 열을 순회합니다.
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 1) {
					sb.append("O"); // 블록이 있는 위치는 "O" 문자로 표시합니다.
				} else {
					sb.append(" "); // 블록이 없는 위치는 공백으로 표시합니다.
				}
			}

			sb.append(BORDER_CHAR); // 각 행의 끝에 경계 문자를 추가합니다.

			
			if(i==1 || i==2) {
				for (int j = 0; j < WIDTH+4; j++) sb.append(" "); // 5번째줄까지 게임부분과 NEXT블럭 부분의 공백
				//NEXT 블럭 표시
				for (int k = 0; k < nextcurr.width(); k++) {
					if (nextcurr.width() == 4 && i ==2 ) // "OOOO"만 너비가 4이므로 따로 처리
						break;
					if (nextcurr.getShape(k, i-1) == 1) sb.append("O"); // 나머지 블럭들 표시
					else sb.append(" ");
				}


			}
			else if(i == 3){
				for (int j = 0; j < WIDTH; j++) sb.append(" "); // 6번째줄 게임부분과 NEXT블럭 부분의 공백
				sb.append("------------"); // 6번째줄에 NEXT의 하단 경계선 추가
			}



			sb.append("\n"); // 줄 바꿈을 추가하여 다음 행으로 넘어갑니다.
		}

		// 하단 경계선을 그립니다.
		for (int t = 0; t < WIDTH + 2; t++) sb.append(BORDER_CHAR); // 보드의 너비만큼 하단에 경계 문자를 추가합니다.

		pane.setText(sb.toString()); // StringBuffer에 저장된 문자열을 JTextPane에 설정합니다.

		StyledDocument doc = pane.getStyledDocument(); // JTextPane의 스타일이 적용된 문서를 가져옵니다.
		doc.setParagraphAttributes(0, doc.getLength(), styleSet, false); // 가져온 문서에 스타일 속성을 적용합니다.
		pane.setStyledDocument(doc); // 스타일이 적용된 문서를 다시 JTextPane에 설정
	}
	
	public void reset() {
		this.board = new int[20][10]; // 게임 보드를 초기화합니다. 20x10 크기의 2차원 배열을 새로 생성합니다.
	}

	public class PlayerKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			// 키가 타이핑됐을 때의 동작을 정의할 수 있으나, 여기서는 사용하지 않습니다.
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// 키가 눌렸을 때의 동작을 정의합니다.
			switch(e.getKeyCode()) { // 눌린 키에 따라 적절한 동작을 수행합니다.1
				case KeyEvent.VK_DOWN:
					moveDown(); // 아래 방향키가 눌렸을 때, 현재 블록을 아래로 이동시킵니다.
					drawBoard(); // 게임 보드를 다시 그립니다.
					break;
				case KeyEvent.VK_RIGHT:
					moveRight(); // 오른쪽 방향키가 눌렸을 때, 현재 블록을 오른쪽으로 이동시킵니다.
					drawBoard(); // 게임 보드를 다시 그립니다.
					break;
				case KeyEvent.VK_LEFT:
					moveLeft(); // 왼쪽 방향키가 눌렸을 때, 현재 블록을 왼쪽으로 이동시킵니다.
					drawBoard(); // 게임 보드를 다시 그립니다.
					break;
				case KeyEvent.VK_UP:
					eraseCurr(); // 현재 블록을 지웁니다.
					curr.rotate(); // 현재 블록을 회전시킵니다.
					drawBoard(); // 게임 보드를 다시 그립니다.
					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// 키가 떼어졌을 때의 동작을 정의할 수 있으나, 여기서는 사용하지 않습니다..
		}
	}

	
}
