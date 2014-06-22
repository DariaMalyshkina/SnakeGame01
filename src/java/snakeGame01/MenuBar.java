package snakeGame01;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MenuBar extends JFrame {
    private GameGUI gameGUI;
    private Obstacle obstacle;
    private List<String> listResults;

    public MenuBar(GameGUI gameGUI, List<String> listResults) {
        this.gameGUI = gameGUI;
        this.obstacle = new Obstacle();
        this.listResults = listResults;
    }

    JMenuBar createBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu game = new JMenu("Игра");
        JMenuItem newGame = new JMenuItem("Новая игра");
        JMenuItem parameters = new JMenuItem("Параметры");
        JMenuItem appearance = new JMenuItem("Оформление");
        JMenuItem results = new JMenuItem("Результаты");
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
                inhibitGame();
                parametersChange();
            }
        });
        appearance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inhibitGame();
                appearanceChange();
            }
        });
        results.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inhibitGame();
                JDialog dialog = resultsVisible();
                dialog.setVisible(true);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inhibitGame();
                messageExit();
            }
        });
        game.add(newGame);
        game.addSeparator();
        game.add(parameters);
        game.add(appearance);
        game.add(results);
        game.addSeparator();
        game.add(exit);
        menuBar.add(game);
        return menuBar;
    }

    private void messageNewGame() {
        final JDialog dialog = new JDialog(gameGUI, "Вы уверены?", true);
        dialog.setSize(320, 120);
        JLabel labelExit = new JLabel("Вы уверены, что хотите начать новую игру?");
        labelExit.setBorder(new EmptyBorder(10, 20, 0, 0));
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
                renewGame(dialog);
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

    private void parametersChange() {
        final JDialog dialog = new JDialog(gameGUI, "Изменение параметров", true);
        dialog.setSize(250, 180);
        final JPanel levelPanel = new JPanel();
        levelPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        Border border = BorderFactory.createTitledBorder("Уровень");
        levelPanel.setBorder(border);
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));
        JButton save = new JButton("Сохранить");
        JButton cancel = new JButton("Отмена");
        final JRadioButton simple = new JRadioButton("Новичок");
        final JRadioButton normal = new JRadioButton("Любитель");
        normal.setSelected(true);
        final JRadioButton hard = new JRadioButton("Профессионал");
        ButtonGroup group = new ButtonGroup();
        group.add(simple);
        group.add(normal);
        group.add(hard);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (simple.isSelected()) {
                    gameGUI.setSpeed(150);
                    gameGUI.setLevel("Новичок");
                } else {
                    if (normal.isSelected()) {
                        gameGUI.setSpeed(100);
                        gameGUI.setLevel("Любитель");
                    } else {
                        if (hard.isSelected()) {
                            gameGUI.setSpeed(50);
                            gameGUI.setLevel("Профессионал");
                        }
                    }
                }
                renewGame(dialog);
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renewGame(dialog);
            }
        });
        levelPanel.add(simple);
        levelPanel.add(normal);
        levelPanel.add(hard);
        JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(cancel);
        dialog.add(levelPanel, BorderLayout.NORTH);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
    }

    private void appearanceChange() {
        final JDialog dialog = new JDialog(this, "Изменение оформления", true);
        dialog.setSize(300, 260);
        final JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Выбор фона"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        final JRadioButton waterfall = new JRadioButton("Водопад");
        final JRadioButton river = new JRadioButton("Река");
        final JRadioButton rain = new JRadioButton("Дождь");
        final JRadioButton snake = new JRadioButton("Змея");
        final JRadioButton mowgli = new JRadioButton("Маугли");
        final JRadioButton green = new JRadioButton("Зеленый фон");
        green.setSelected(true);
        ButtonGroup groupBackground = new ButtonGroup();
        groupBackground.add(waterfall);
        groupBackground.add(river);
        groupBackground.add(rain);
        groupBackground.add(snake);
        groupBackground.add(mowgli);
        groupBackground.add(green);
        final JPanel obstaclePanel = new JPanel();
        obstaclePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Вариант препятствий"),
                BorderFactory.createEmptyBorder(5, 5, 5, 50)));
        obstaclePanel.setLayout(new BoxLayout(obstaclePanel, BoxLayout.Y_AXIS));
        final JRadioButton variant1 = new JRadioButton("Вариант1");
        final JRadioButton variant2 = new JRadioButton("Вариант2");
        final JRadioButton variant3 = new JRadioButton("Вариант3");
        final JRadioButton variant4 = new JRadioButton("Вариант4");
        final JRadioButton variant5 = new JRadioButton("Вариант5");
        final JRadioButton variant6 = new JRadioButton("Вариант6");
        ButtonGroup groupObstacle = new ButtonGroup();
        groupObstacle.add(variant1);
        groupObstacle.add(variant2);
        groupObstacle.add(variant3);
        groupObstacle.add(variant4);
        groupObstacle.add(variant5);
        groupObstacle.add(variant6);
        JButton save = new JButton("Сохранить");
        JButton cancel = new JButton("Отмена");
        save.addActionListener(new ActionListener() {
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
                                    }
                                }
                            }
                        }
                    }
                }
                if (variant1.isSelected()) {
                    gameGUI.setVariantAsArray(obstacle.getVariant1());
                    gameGUI.setVariant("Вариант препятствий 1");
                } else {
                    if (variant2.isSelected()) {
                        gameGUI.setVariantAsArray(obstacle.getVariant2());
                        gameGUI.setVariant("Вариант препятствий 2");
                    } else {
                        if (variant3.isSelected()) {
                            gameGUI.setVariantAsArray(obstacle.getVariant3());
                            gameGUI.setVariant("Вариант препятствий 3");
                        } else {
                            if (variant4.isSelected()) {
                                gameGUI.setVariantAsArray(obstacle.getVariant4());
                                gameGUI.setVariant("Вариант препятствий 4");
                            } else {
                                if (variant5.isSelected()) {
                                    gameGUI.setVariantAsArray(obstacle.getVariant5());
                                    gameGUI.setVariant("Вариант препятствий 5");
                                } else {
                                    if (variant6.isSelected()) {
                                        gameGUI.setVariantAsArray(obstacle.getVariant6());
                                        gameGUI.setVariant("Вариант препятствий 6");
                                    } else {
                                        gameGUI.setVariantAsArray(null);
                                        gameGUI.setVariant("Без препятствий");
                                    }
                                }
                            }
                        }
                    }
                }
                renewGame(dialog);
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renewGame(dialog);
            }
        });
        backgroundPanel.add(waterfall);
        backgroundPanel.add(river);
        backgroundPanel.add(rain);
        backgroundPanel.add(snake);
        backgroundPanel.add(mowgli);
        backgroundPanel.add(green);
        obstaclePanel.add(variant1);
        obstaclePanel.add(variant2);
        obstaclePanel.add(variant3);
        obstaclePanel.add(variant4);
        obstaclePanel.add(variant5);
        obstaclePanel.add(variant6);
        JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(cancel);
        Box box = Box.createHorizontalBox();
        box.add(backgroundPanel);
        box.add(obstaclePanel);
        dialog.add(box, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
    }

    private JDialog resultsVisible() {
        final JDialog dialog = new JDialog(gameGUI, "Результаты", true);
        dialog.setSize(400, 400);
        final DefaultListModel<String> listModel = new DefaultListModel<String>();
        final JList<String> list = new JList<String>(listModel);
        for (String res : listResults) {
            listModel.addElement(res);
            int index = listModel.size() - 1;
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
        final JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        listPanel.setSize(330, 350);
        final JButton ok = new JButton("Ок");
        final JButton remove = new JButton("Убрать из списка");
        final JButton clear = new JButton("Очистить весь список");
        if (listModel.isEmpty()) {
            remove.setEnabled(false);
            clear.setEnabled(false);
        }
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renewGame(dialog);
            }
        });
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listResults.remove(list.getSelectedIndex());
                listModel.remove(list.getSelectedIndex());
                gameGUI.rewriteFile();
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.removeAllElements();
                listResults = new ArrayList<String>();
                gameGUI.rewriteFile();
            }
        });
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedIndex() >= 0) {
                    remove.setEnabled(true);
                    clear.setEnabled(true);
                } else {
                    remove.setEnabled(false);
                    clear.setEnabled(false);
                }
            }
        });
        JPanel buttons = new JPanel();
        buttons.add(ok);
        buttons.add(remove);
        buttons.add(clear);
        list.setSelectedIndex(0);
        list.setFocusable(false);
        listPanel.add(new JScrollPane(list), BorderLayout.CENTER);
        dialog.add(listPanel, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.pack();
        return dialog;
    }

    private void messageExit() {
        final JDialog dialog = new JDialog(this, "Вы уверены?", true);
        dialog.setSize(300, 120);
        JLabel labelExit = new JLabel("Вы уверены, что хотите выйти из игры?");
        labelExit.setBorder(new EmptyBorder(10, 25, 0, 0));
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
                renewGame(dialog);
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

    private void renewGame(JDialog dialog) {
        dialog.setVisible(false);
        if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
            gameGUI.startThread();
        }
    }

    private void inhibitGame() {
        if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
            gameGUI.stopThread();
        }
    }

    List<String> getListResults() {
        return listResults;
    }
}
