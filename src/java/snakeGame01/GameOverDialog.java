package snakeGame01;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class GameOverDialog extends JDialog {
    private GameGUI gameGUI;
    private final String fileName;
    private List<String> listResults;

    public GameOverDialog(GameGUI gameGUI, String fileName, List<String> listResults) {
        this.gameGUI = gameGUI;
        this.fileName = fileName;
        this.listResults = listResults;
        addComponents();
    }

    private JDialog addComponents() {
        gameGUI.setGameOver(true);
        final JDialog dialog = new JDialog(this, "Новая игра?", true);
        dialog.setSize(330, 180);
        JLabel labelGameOver = new JLabel("Вы проиграли. Ваш результат " + gameGUI.getScore());
        labelGameOver.setBorder(new EmptyBorder(10, 50, 5, 0));
        JLabel labelSave = new JLabel("Впишите имя, если хотите сохранить результат");
        labelSave.setBorder(new EmptyBorder(0, 20, 0, 0));
        final JTextField textField = new JTextField(10);
        final JLabel labelSaved = new JLabel(" ");
        labelSaved.setBorder(new EmptyBorder(0, 20, 0, 0));
        JLabel labelNewGame = new JLabel("Хотите начать новую игру?");
        labelNewGame.setBorder(new EmptyBorder(5, 50, 5, 0));
        JButton yes = new JButton("Да");
        JButton no = new JButton("Нет");
        JButton save = new JButton("Сохранить");
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameGUI.newGame();
                dialog.setVisible(false);
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameGUI.getGameBefore()) {
                    gameGUI.stopThread();
                }
                dialog.setVisible(false);
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().trim().length() > 0) {
                    whiteGamer(textField.getText());
                    labelSaved.setText("Результат сохранен");
                }
            }
        });
        JPanel buttons = new JPanel();
        buttons.add(yes);
        buttons.add(no);
        buttons.add(save);
        Box box = Box.createVerticalBox();
        box.add(labelGameOver, BorderLayout.CENTER);
        box.add(labelSave, BorderLayout.CENTER);
        box.add(textField, BorderLayout.CENTER);
        box.add(labelSaved, BorderLayout.CENTER);
        box.add(labelNewGame, BorderLayout.CENTER);
        dialog.add(box, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
        return dialog;
    }

    private void whiteGamer(String name) {
        Gamer gamer = new Gamer(name, gameGUI.getScore(), gameGUI.getLevel(), gameGUI.getVariant());
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            try {
                listResults.add(gamer.getStringGamer());
                for (String s : listResults) {
                    bufferedWriter.write(s);
                    bufferedWriter.write("\n");
                }
            } finally {
                bufferedWriter.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}