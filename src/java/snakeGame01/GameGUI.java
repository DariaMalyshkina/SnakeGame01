package snakeGame01;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;

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
    private MenuBar menuBar;
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private JButton buttonFruit;
    private List<JButton> buttonsSnake;
    private List<JButton> buttonsObstacle;
    private JLabel scoreLabel;
    private Thread thread;
    private Random random = new Random();
    private boolean gameOver;
    private boolean gameBefore;
    private final String scoreString = "Количество собранных фруктов: ";
    private final String images = "C:/home/malyshkina/SnakeGame/background/";
    private final String fileName = "C:/home/malyshkina/SnakeGame/results.txt";
    private List<String> listResults = new ArrayList<String>();
    private int speed;
    private String imageString;
    private int score;
    private String level;
    private String variant;
    private String[] variantAsArray;
    private Color colorBackground;

    private void initializeValues() {
        setScore(0);
        if (getSpeed() == 0) {
            setSpeed(100);
        }
        if (getLevel() == null) {
            setLevel("Любитель");
        }
        if (getVariant() == null) {
            setVariant("Без препятствий");
        }
        this.gameOver = false;
        this.gameBefore = false;
        this.buttonsSnake = new ArrayList<JButton>();
        this.buttonsObstacle = new ArrayList<JButton>();
        this.fruit = new Fruit();
        this.snake = new Snake();
    }

    public GameGUI() {
        super("Snake Game");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowReaction(this));
        setResizable(false);
        setBounds(200, 200, 606, 480);
        this.menuBar = new MenuBar(this, listResults);
        setJMenuBar(menuBar.createBar());
        readFile();
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
        createObstacle();
        changeBackground();
        scoreLabel.setText(scoreString + getScore());
        scoreLabel.setForeground(Color.black);
        scoreLabel.setVisible(true);
        startThread();
        setGameBefore(true);
    }

    private void createFirstSnake() {
        for (int i = 0; i < 3; i++) {
            buttonsSnake.add(i, new JButton());
            if (i == 0) {
                buttonsSnake.get(i).setBackground(new Color(255, 110, 40));
                buttonsSnake.get(i).setBorder(BorderFactory.createLineBorder(new Color(255, 110, 40)));
            } else {
                if (i == 1) {
                    buttonsSnake.get(i).setBackground(new Color(255, 150, 40));
                    buttonsSnake.get(i).setBorder(BorderFactory.createLineBorder(new Color(255, 150, 40)));
                } else {
                    buttonsSnake.get(i).setBackground(new Color(255, 200, 40));
                    buttonsSnake.get(i).setBorder(BorderFactory.createLineBorder(new Color(255, 200, 40)));
                }
            }
            buttonsSnake.get(i).setEnabled(false);
            panelGame.add(buttonsSnake.get(i));
            buttonsSnake.get(i).setBounds(snake.x[i], snake.y[i], 10, 10);
            snake.x[i + 1] = snake.x[i] - 10;
            snake.y[i + 1] = snake.y[i];
        }
    }

    private void createObstacle() {
        if (getVariantAsArray() != null) {
            String[] variant1 = getVariantAsArray();
            String line;
            for (int y = 0; y < variant1.length; y++) {
                line = variant1[y];
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '1') {
                        JButton button = new JButton();
                        button.setBackground(getColorObstacle());
                        button.setBorder(BorderFactory.createLineBorder(getColorObstacle()));
                        button.setEnabled(false);
                        buttonsObstacle.add(button);
                        panelGame.add(button);
                        button.setBounds(x * 10, y * 10, 10, 10);
                    }
                }
            }
        }
    }

    private void createFruit() {
        if (!fruit.isFruit()) {
            panelGame.add(buttonFruit);
            buttonFruit.setBounds((10 * random.nextInt(60)), (10 * random.nextInt(40)), 10, 10);
            buttonFruit.setBackground(getColorFruit());
            buttonFruit.setBorder(BorderFactory.createLineBorder(getColorFruit()));
            for (int i = 0; i < snake.getLength(); i++) {
                if (buttonsSnake.get(i).getX() == buttonFruit.getX() && buttonsSnake.get(i).getY() == buttonFruit.getY()) {
                    panelGame.remove(buttonFruit);
                    createFruit();
                    break;
                }
            }
            for (JButton buttonObstacle : buttonsObstacle) {
                if (buttonObstacle.getX() == buttonFruit.getX() && buttonObstacle.getY() == buttonFruit.getY()) {
                    panelGame.remove(buttonFruit);
                    createFruit();
                    break;
                }
            }
            fruit.setPoint(buttonFruit.getLocation());
            fruit.setFruit(true);
        }
    }

    private void growUp() {
        buttonsSnake.add(snake.getLength(), new JButton());
        buttonsSnake.get(snake.getLength()).setBackground(getColorSnake());
        buttonsSnake.get(snake.getLength()).setBorder(BorderFactory.createLineBorder(getColorSnake()));
        buttonsSnake.get(snake.getLength()).setEnabled(false);
        panelGame.add(buttonsSnake.get(snake.getLength()));
        buttonsSnake.get(snake.getLength()).setBounds(snake.getPointSnake().get(snake.getLength() - 1).x, snake.getPointSnake().get(snake.getLength() - 1).y, 10, 10);
        snake.setLength(snake.getLength() + 1);
    }

    private void move() {
        for (int i = 0; i < snake.getLength(); i++) {
            snake.getPointSnake().add(i, buttonsSnake.get(i).getLocation());
        }
        snake.x[0] += snake.getDirectionX();
        snake.y[0] += snake.getDirectionY();
        buttonsSnake.get(0).setBounds(snake.x[0], snake.y[0], 10, 10);

        for (int i = 1; i < snake.getLength(); i++) {
            buttonsSnake.get(i).setLocation(snake.getPointSnake().get(i - 1));
        }
        createFruit();
        runFruit();
        runWall();
        runSnake();
        runObstacle();
        panelGame.repaint();
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
            if (buttonsSnake.get(i).getX() == snake.x[0] && buttonsSnake.get(i).getY() == snake.y[0]) {
                messageGameOver();
                stopThread();
                break;
            }
        }
    }

    private void runObstacle() {
        for (JButton buttonObstacle : buttonsObstacle) {
            if (buttonObstacle.getX() == snake.x[0] && buttonObstacle.getY() == snake.y[0]) {
                panelGame.remove(buttonsSnake.get(0));
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
        new GameOverDialog(this, fileName, menuBar.getListResults());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void changeBackground() {
        if (getImageString() == null && getColorBackground() == null) {
            panelGame.setBackground(Color.black);
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

    private Color getColorFruit() {
        return new Color(193, 0, 0);
    }

    private Color getColorObstacle() {
        return new Color(255, 90, 0);
    }

    private Color getColorSnake() {
        return new Color(255, 255, 40);
    }

    int getSpeed() {
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

    private String[] getVariantAsArray() {
        return variantAsArray;
    }

    void setVariantAsArray(String[] variantAsArray) {
        this.variantAsArray = variantAsArray;
    }

    String getVariant() {
        return variant;
    }

    void setVariant(String variant) {
        this.variant = variant;
    }

    String getLevel() {
        return level;
    }

    void setLevel(String level) {
        this.level = level;
    }

    public void keyPressed(KeyEvent e) {
        if (buttonsSnake.get(0) != null) {
            snake.pressing(e);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    private void readFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    listResults.add(line);
                }
            } finally {
                bufferedReader.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    void rewriteFile() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            try {
                for (String s : menuBar.getListResults()) {
                    bufferedWriter.write(s);
                    bufferedWriter.write("\n");
                    System.out.println(s);
                }
            } finally {
                bufferedWriter.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}