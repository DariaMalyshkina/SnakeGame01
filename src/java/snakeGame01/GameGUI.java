package snakeGame01;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GameGUI extends JFrame implements KeyListener, Runnable {

    public static void main(String[] args) {
        JFrame game = new GameGUI();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

    private Fruit fruit;
    private Snake snake;

    private JPanel panelGame;
    private JPanel backgroundPanel;
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private JButton buttonFruit;
    private JButton[] buttonSnake = new JButton[1000];
    private int score;
    private final String scoreString = "Количество собранных фруктов: ";
    private JLabel scoreLabel;
    private int speed;
    private Thread thread;
    private Random random = new Random();
    private boolean gameOver;
    private boolean gameBefore;
    private String imageString;
    private final String images = "C:/home/malyshkina/term4/lang/background/";
    private Color colorBackground;

    private void initializeValues() {
        setScore(0);
        if (getSpeed() == 0) {
            setSpeed(100);
        }
        this.gameOver = false;
        this.gameBefore = false;
        this.fruit = new Fruit();
        this.snake = new Snake();
    }

    public GameGUI() {
        super("Snake Game");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowReaction(this));
        setResizable(false);
        setBounds(200, 200, 606, 480);
        MenuBar menuBar = new MenuBar(this);
        setJMenuBar(menuBar.createBar());
        this.panelGame = new JPanel();
        panelGame.setLayout(null);
        panelGame.setBounds(0, 0, WIDTH, HEIGHT);
        this.backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.scoreLabel = new JLabel(scoreString + getScore());
        scoreLabel.setForeground(Color.black);
        scoreLabel.setEnabled(false);
        scoreLabel.setVisible(false);
        this.buttonFruit = new JButton();
        buttonFruit.setEnabled(false);
        JPanel panelScore = new JPanel();
        panelScore.setLayout(new GridLayout(0, 1));
        panelScore.setBounds(0, HEIGHT, WIDTH, 30);
        panelScore.setBackground(Color.white);
        panelScore.add(scoreLabel);
        Background background = new Background(images + "background.jpg");
        background.setSize(WIDTH, HEIGHT);
        panelGame.add(background);
        getContentPane().setLayout(null);
        getContentPane().add(panelGame);
        getContentPane().add(panelScore);
        addKeyListener(this);
    }

    void newGame() {
        if (getGameBefore()) {
            stopThread();
        }
        new GameGUI();
        panelGame.removeAll();
        initializeValues();
        createFirstSnake();
        changeBackground();
        scoreLabel.setText(scoreString + getScore());
        scoreLabel.setForeground(Color.black);
        scoreLabel.setVisible(true);
        startThread();
        setGameBefore(true);
    }

    private void createFirstSnake() {
        for (int i = 0; i < 3; i++) {
            buttonSnake[i] = new JButton();
            buttonSnake[i].setBackground(Color.white);
            buttonSnake[i].setBorder(BorderFactory.createLineBorder(Color.black));
            buttonSnake[i].setEnabled(false);
            panelGame.add(buttonSnake[i]);
            buttonSnake[i].setBounds(snake.x[i], snake.y[i], 10, 10);
            snake.x[i + 1] = snake.x[i] - 10;
            snake.y[i + 1] = snake.y[i];
        }
    }

    private void growUp() {
        buttonSnake[snake.getLength()] = new JButton();
        buttonSnake[snake.getLength()].setBackground(Color.white);
        buttonSnake[snake.getLength()].setBorder(BorderFactory.createLineBorder(Color.black));
        buttonSnake[snake.getLength()].setEnabled(false);
        panelGame.add(buttonSnake[snake.getLength()]);
        buttonSnake[snake.getLength()].setBounds(snake.getPointSnake()[snake.getLength() - 1].x, snake.getPointSnake()[snake.getLength() - 1].y, 10, 10);
        snake.setLength(snake.getLength() + 1);
    }

    private void move() {
        for (int i = 0; i < snake.getLength(); i++) {
            snake.getPointSnake()[i] = buttonSnake[i].getLocation();
        }
        snake.x[0] += snake.getDirectionX();
        snake.y[0] += snake.getDirectionY();
        buttonSnake[0].setBounds(snake.x[0], snake.y[0], 10, 10);

        for (int i = 1; i < snake.getLength(); i++) {
            buttonSnake[i].setLocation(snake.getPointSnake()[i - 1]);
        }
        createFruit();
        runFruit();
        runWall();
        runSnake();
        panelGame.repaint();
    }

    private void createFruit() {
        if (!fruit.isFruit()) {
            panelGame.add(buttonFruit);
            buttonFruit.setBounds((10 * random.nextInt(60)), (10 * random.nextInt(40)), 10, 10);
            buttonFruit.setBackground(Color.white);
            buttonFruit.setBorder(BorderFactory.createLineBorder(Color.black));
            for (int i = 0; i < snake.getLength(); i++) {
                if (buttonSnake[i].getX() == buttonFruit.getX() && buttonSnake[i].getY() == buttonFruit.getY()
                        || buttonFruit.getX() == WIDTH || buttonFruit.getX() == 0 || buttonFruit.getY() == HEIGHT || buttonFruit.getY() == 0) {
                    panelGame.remove(buttonFruit);
                    createFruit();
                    break;
                }
            }
            fruit.setPoint(buttonFruit.getLocation());
            fruit.setFruit(true);
        }
    }

    private void runFruit() {
        if (fruit.isFruit()) {
            if (fruit.getPoint().x == snake.x[0] && fruit.getPoint().y == snake.y[0]) {
                panelGame.remove(buttonFruit);
                setScore(getScore() + 1);
                growUp();
                scoreLabel.setText(scoreString + getScore());
                scoreLabel.setForeground(Color.black);
                fruit.setFruit(false);
            }
        }
    }

    private void runWall() {
        if (snake.x[0] == WIDTH || snake.x[0] == 0
                || snake.y[0] == HEIGHT || snake.y[0] == 0) {
            messageGameOver();
            stopThread();
        }
    }

    private void runSnake() {
        for (int i = 1; i < snake.getLength(); i++) {
            if (buttonSnake[i].getX() == snake.x[0] && buttonSnake[i].getY() == snake.y[0]) {
                messageGameOver();
                stopThread();
                break;
            }
        }
    }

    public void run() {
        while (true) {
            move();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void startThread() {
        if (!getGameOver()) {
            thread = new Thread(this);
            thread.start();
        }
    }

    void stopThread() {
        thread.stop();
    }

    private void messageGameOver() {
        setGameOver(true);
        new GameOverDialog(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void changeBackground() {
        if (getImageString() == null && getColorBackground() == null) {
            panelGame.setBackground(new Color(0, 0, 0));
            panelGame.setOpaque(true);
        } else {
            if (getImageString() == null) {
                panelGame.setBackground(getColorBackground());
                panelGame.setOpaque(true);
            } else {
                Background background = new Background(getImageString());
                background.setSize(WIDTH, HEIGHT);
                backgroundPanel.add(background);
                backgroundPanel.removeAll();
                backgroundPanel.add(background);
                getContentPane().add(backgroundPanel);
                panelGame.setOpaque(false);
            }
        }
    }

    private int getSpeed() {
        return speed;
    }

    void setSpeed(int speed) {
        this.speed = speed;
    }

    int getScore() {
        return score;
    }

    private void setScore(int score) {
        this.score = score;
    }

    boolean getGameOver() {
        return gameOver;
    }

    void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    boolean getGameBefore() {
        return gameBefore;
    }

    private void setGameBefore(boolean gameBefore) {
        this.gameBefore = gameBefore;
    }

    private String getImageString() {
        return imageString;
    }

    void setImageString(String string) {
        this.imageString = string;
    }

    String getImages() {
        return images;
    }

    private Color getColorBackground() {
        return colorBackground;
    }

    void setColorBackground(Color color) {
        this.colorBackground = color;
    }


    public void keyPressed(KeyEvent e) {
        if (buttonSnake[0] != null) {
            snake.pressing(e);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}



