package snakeGame01;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class MenuBar extends JFrame {
    private GameGUI gameGUI;

    public MenuBar(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
    }

    JMenuBar createBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu game = new JMenu("Игра");
        JMenuItem newGame = new JMenuItem("Новая игра");
        JMenuItem parameters = new JMenuItem("Параметры");
        JMenuItem appearance = new JMenuItem("Оформление");
        JMenuItem exit = new JMenuItem("Выход");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.stopThread();
                    messageNewGame();
                } else {
                    gameGUI.newGame();
                }
            }
        });
        parameters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.stopThread();
                }
                parametersChange();
            }
        });
        appearance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.stopThread();
                }
                appearanceChange();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.stopThread();
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
        return menuBar;
    }

    private void parametersChange() {
        final JDialog dialog = new JDialog(gameGUI, "Изменение параметров", true);
        dialog.setSize(250, 180);
        final JPanel levelPanel = new JPanel();
        levelPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        Border border = BorderFactory.createTitledBorder("Уровень");
        levelPanel.setBorder(border);
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));
        JButton keep = new JButton("Сохранить");
        JButton cancel = new JButton("Отмена");
        final JRadioButton simple = new JRadioButton("Новичок");
        final JRadioButton normal = new JRadioButton("Любитель");
        normal.setSelected(true);
        final JRadioButton hard = new JRadioButton("Профессионал");
        ButtonGroup group = new ButtonGroup();
        group.add(simple);
        group.add(normal);
        group.add(hard);
        keep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (simple.isSelected()) {
                    gameGUI.setSpeed(150);
                } else {
                    if (normal.isSelected()) {
                        gameGUI.setSpeed(100);
                    } else {
                        if (hard.isSelected()) {
                            gameGUI.setSpeed(50);
                        }
                    }
                }
                dialog.setVisible(false);
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.startThread();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.startThread();
                }
            }
        });
        levelPanel.add(simple);
        levelPanel.add(normal);
        levelPanel.add(hard);
        JPanel buttons = new JPanel();
        buttons.add(keep);
        buttons.add(cancel);
        dialog.add(levelPanel, BorderLayout.NORTH);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
    }

    private void messageNewGame() {
        final JDialog dialog = new JDialog(gameGUI, "Вы уверены?", true);
        dialog.setSize(400, 120);
        JLabel labelExit = new JLabel("Вы уверены, что хотите начать новую игру?");
        labelExit.setBorder(new EmptyBorder(10, 50, 0, 0));
        JButton yes = new JButton("Да");
        JButton no = new JButton("Нет");
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                gameGUI.newGame();
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.startThread();
                }
            }
        });
        JPanel buttons = new JPanel();
        buttons.add(yes);
        buttons.add(no);
        dialog.add(labelExit, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(gameGUI);
        dialog.setVisible(true);
        dialog.pack();
    }

    private void appearanceChange() {
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
                    gameGUI.setImageString(gameGUI.getImages() + "waterfall.jpg");
                } else {
                    if (river.isSelected()) {
                        gameGUI.setImageString(gameGUI.getImages() + "river.jpg");
                    } else {
                        if (rain.isSelected()) {
                            gameGUI.setImageString(gameGUI.getImages() + "rain.jpg");
                        } else {
                            if (snake.isSelected()) {
                                gameGUI.setImageString(gameGUI.getImages() + "snake.jpg");
                            } else {
                                if (mowgli.isSelected()) {
                                    gameGUI.setImageString(gameGUI.getImages() + "mowgli.jpg");
                                } else {
                                    if (green.isSelected()) {
                                        gameGUI.setImageString(null);
                                        gameGUI.setColorBackground(new Color(0, 128, 0));
                                    }
                                }
                            }
                        }
                    }
                }
                dialog.setVisible(false);
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.startThread();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.startThread();
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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
                if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
                    gameGUI.startThread();
                }
            }
        });
        JPanel buttons = new JPanel();
        buttons.add(yes);
        buttons.add(no);
        dialog.add(labelExit, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
    }
}
