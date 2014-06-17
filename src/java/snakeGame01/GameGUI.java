package snakeGame01;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

class GameGUI extends JFrame implements KeyListener, Runnable {

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
    private boolean randomSnakeSelect;
    private boolean gameOver;
    private boolean gameBefore;
    private String imageString;
    private final String images = "C:/home/malyshkina/SnakeGame/background/";
    private Color colorBackground;

    private void initializeValues() {
        this.score = 0;
        if (getSpeed() == 0) {
            setSpeed(100);
        }
//        if (getRandomSnakeSelect()) {
//            this.randomSnakeSelect = false;
//        }
//        this.randomSnakeSelect = getRandomSnakeSelect();
        this.gameOver = false;
        this.gameBefore = false;
        this.fruit = new Fruit();
        this.snake = new Snake();
    }

    GameGUI() {
        super("Snake Game");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setBounds(200, 200, 606, 480);
        createBar();
        this.panelGame = new JPanel();
        panelGame.setLayout(null);
        panelGame.setBounds(0, 0, WIDTH, HEIGHT);
        this.backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.scoreLabel = new JLabel(scoreString + score);
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
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (getGameBefore() && !gameOver) {
                    thread.stop();
                }
                Object[] options = {"Да", "Нет"};
                int result = JOptionPane
                        .showOptionDialog(e.getWindow(), "Вы уверены, что хотите выйти из игры?", "Вы уверены?",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (result == JOptionPane.YES_OPTION) {
                    e.getWindow().setVisible(false);
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    if (getGameBefore() && !gameOver) {
                        startThread();
                    }
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {
                if (getGameBefore() && !gameOver) {
                    thread.stop();
                }
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                if (getGameBefore() && !gameOver) {
                    startThread();
                }
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    void newGame() {
        if (getGameBefore()) {
            thread.stop();
        }
        new GameGUI();
        panelGame.removeAll();
        initializeValues();
        createFirstSnake();
        changeBackground();
        scoreLabel.setText(scoreString + score);
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

    void growUp() {
        buttonSnake[snake.getLength()] = new JButton();
        buttonSnake[snake.getLength()].setBackground(Color.white);
        buttonSnake[snake.getLength()].setBorder(BorderFactory.createLineBorder(Color.black));
        buttonSnake[snake.getLength()].setEnabled(false);
        panelGame.add(buttonSnake[snake.getLength()]);
        buttonSnake[snake.getLength()].setBounds(snake.getPointSnake()[snake.getLength() - 1].x, snake.getPointSnake()[snake.getLength() - 1].y, 10, 10);
        snake.setLength(snake.getLength() + 1);
    }

    void move() {
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
                score += 1;
                growUp();
                scoreLabel.setText(scoreString + score);
                scoreLabel.setForeground(Color.black);
                fruit.setFruit(false);
            }
        }
    }

    private void runWall() {
        if (snake.x[0] == WIDTH || snake.x[0] == 0
                || snake.y[0] == HEIGHT || snake.y[0] == 0) {
            messageGameOver();
            thread.stop();
        }
    }

    private void runSnake() {
        for (int i = 1; i < snake.getLength(); i++) {
            if (buttonSnake[i].getX() == snake.x[0] && buttonSnake[i].getY() == snake.y[0]) {
                messageGameOver();
                thread.stop();
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
            }
        }
    }

    private void startThread() {
        if (!getGameOver()) {
            thread = new Thread(this);
            thread.start();
        }
    }

    private void createBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu game = new JMenu("Игра");
        JMenuItem newGame = new JMenuItem("Новая игра");
        JMenuItem parameters = new JMenuItem("Параметры");
        JMenuItem appearance = new JMenuItem("Оформление");
        JMenuItem exit = new JMenuItem("Выход");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getGameBefore() && !gameOver) {
                    thread.stop();
                    messageNewGame();
                } else {
                    newGame();
                }
            }
        });
        parameters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getGameBefore() && !gameOver) {
                    thread.stop();
                }
                parametersChange(e);
            }
        });
        appearance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getGameBefore() && !gameOver) {
                    thread.stop();
                }
                appearanceChange(e);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getGameBefore() && !gameOver) {
                    thread.stop();
                }
                messageExit();
            }
        });
        game.add(newGame);
        game.addSeparator();
        game.add(parameters);
        game.add(appearance);
        game.addSeparator();
        game.add(exit);
        menuBar.add(game);
        setJMenuBar(menuBar);
    }

    private void messageNewGame() {
        final JDialog dialog = new JDialog(this, "Вы уверены?", true);
        dialog.setSize(400, 120);
        JLabel labelExit = new JLabel("Вы уверены, что хотите начать новую игру?");
        labelExit.setBorder(new EmptyBorder(10, 50, 0, 0));
        JButton yes = new JButton("Да");
        JButton no = new JButton("Нет");
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                newGame();
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                if (getGameBefore() && !gameOver) {
                    startThread();
                }
            }
        });
        JPanel buttons = new JPanel();
        buttons.add(yes);
        buttons.add(no);
        dialog.add(labelExit, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
    }

    private void messageGameOver() {
        setGameOver(true);
        final JDialog dialog = new JDialog(this, "Новая игра?", true);
        dialog.setSize(300, 130);
        JLabel labelGameOver = new JLabel("Вы проиграли. Ваш результат " + score);
        JLabel labelNewGame = new JLabel("Хотите начать новую игру?");
        JButton keep = new JButton("Да");
        JButton cancel = new JButton("Нет");
        keep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
                dialog.setVisible(false);
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getGameBefore()) {
                    thread.stop();
                }
                dialog.setVisible(false);
            }
        });
        JPanel buttons = new JPanel();
        buttons.add(keep);
        buttons.add(cancel);
        Box box = Box.createVerticalBox();
        box.setBorder(new EmptyBorder(10, 50, 0, 0));
        box.add(labelGameOver, BorderLayout.CENTER);
        box.add(labelNewGame, BorderLayout.CENTER);
        dialog.add(box, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
    }

    private void messageExit() {
        final JDialog dialog = new JDialog(this, "Вы уверены?", true);
        dialog.setSize(400, 120);
        JLabel labelExit = new JLabel("Вы уверены, что хотите выйти из игры?");
        labelExit.setBorder(new EmptyBorder(10, 50, 0, 0));
        JButton yes = new JButton("Да");
        JButton no = new JButton("Нет");
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                if (getGameBefore() && !gameOver) {
                    startThread();
                }
            }
        });
        JPanel buttons = new JPanel();
        buttons.add(yes);
        buttons.add(no);
        dialog.add(labelExit, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
    }

    private void parametersChange(ActionEvent e) {
        final JDialog dialog = new JDialog(this, "Изменение параметров", true);
        dialog.setSize(350, 200);
        final JPanel levelPanel = new JPanel();
        levelPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        Border border = BorderFactory.createTitledBorder("Уровень");
        levelPanel.setBorder(border);
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));
        JButton keep = new JButton("Сохранить");
        JButton cancel = new JButton("Отмена");
        final JRadioButton simple = new JRadioButton("Новичок");
        final JRadioButton normal = new JRadioButton("Любитель");
        final JRadioButton hard = new JRadioButton("Профессионал");
        ButtonGroup group = new ButtonGroup();
        group.add(simple);
        group.add(normal);
        group.add(hard);
        keep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (simple.isSelected()) {
                    setSpeed(150);
                } else {
                    if (normal.isSelected()) {
                        setSpeed(100);
                    } else {
                        if (hard.isSelected()) {
                            setSpeed(50);
                        }
                    }
                }
                dialog.setVisible(false);
                if (getGameBefore() && !gameOver) {
                    startThread();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                if (getGameBefore() && !gameOver) {
                    startThread();
                }
            }
        });
        levelPanel.add(simple);
        levelPanel.add(normal);
        levelPanel.add(hard);
        final JCheckBox randomSnake = new JCheckBox("Играть с самостоятельно движущейся змейкой");
        randomSnake.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (randomSnake.isSelected()) {
                    setRandomSnakeSelect(true);
                }
            }
        });
        JPanel buttons = new JPanel();
        buttons.add(keep);
        buttons.add(cancel);
        dialog.add(levelPanel, BorderLayout.NORTH);
        dialog.add(randomSnake);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
    }

    private void appearanceChange(ActionEvent e) {
        final JDialog dialog = new JDialog(this, "Изменение оформления", true);
        dialog.setSize(300, 250);
        final JPanel backgroundPanel = new JPanel();
        Border border = BorderFactory.createTitledBorder("Выбор фона");
        backgroundPanel.setBorder(border);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        JButton keep = new JButton("Сохранить");
        JButton cancel = new JButton("Отмена");
        final JRadioButton waterfall = new JRadioButton("Водопад");
        final JRadioButton river = new JRadioButton("Река");
        final JRadioButton rain = new JRadioButton("Дождь");
        final JRadioButton snake = new JRadioButton("Змея");
        final JRadioButton mowgli = new JRadioButton("Маугли");
        final JRadioButton green = new JRadioButton("Зеленый фон");
        ButtonGroup group = new ButtonGroup();
        group.add(waterfall);
        group.add(river);
        group.add(rain);
        group.add(snake);
        group.add(mowgli);
        group.add(green);
        keep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (waterfall.isSelected()) {
                    setImageString(images + "waterfall.jpg");
                } else {
                    if (river.isSelected()) {
                        setImageString(images + "river.jpg");
                    } else {
                        if (rain.isSelected()) {
                            setImageString(images + "rain.jpg");
                        } else {
                            if (snake.isSelected()) {
                                setImageString(images + "snake.jpg");
                            } else {
                                if (mowgli.isSelected()) {
                                    setImageString(images + "mowgli.jpg");
                                } else {
                                    if (green.isSelected()) {
                                        setImageString(null);
                                        setColorBackground(new Color(0, 128, 0));
                                    }
                                }
                            }
                        }
                    }
                }
                dialog.setVisible(false);
//                changeBackground();
                if (getGameBefore() && !gameOver) {
                    startThread();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                if (getGameBefore() && !gameOver) {
                    startThread();
                }
            }
        });
        backgroundPanel.add(waterfall);
        backgroundPanel.add(river);
        backgroundPanel.add(rain);
        backgroundPanel.add(snake);
        backgroundPanel.add(mowgli);
        backgroundPanel.add(green);
        JPanel buttons = new JPanel();
        buttons.add(keep);
        buttons.add(cancel);
        dialog.add(backgroundPanel, BorderLayout.NORTH);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
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

    private void setSpeed(int speed) {
        this.speed = speed;
    }

    private boolean getRandomSnakeSelect() {
        return randomSnakeSelect;
    }

    private void setRandomSnakeSelect(boolean select) {
        this.randomSnakeSelect = select;
    }

    private boolean getGameOver() {
        return gameOver;
    }

    private void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    private boolean getGameBefore() {
        return gameBefore;
    }

    private void setGameBefore(boolean gameBefore) {
        this.gameBefore = gameBefore;
    }

    private String getImageString() {
        return imageString;
    }

    private void setImageString(String string) {
        this.imageString = string;
    }

    private Color getColorBackground() {
        return colorBackground;
    }

    private void setColorBackground(Color color) {
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
