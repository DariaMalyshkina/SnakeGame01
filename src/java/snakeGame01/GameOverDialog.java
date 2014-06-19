package snakeGame01;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverDialog extends JDialog {
    private GameGUI gameGUI;

    public GameOverDialog(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
        addComponents();
    }

    private JDialog addComponents() {
        gameGUI.setGameOver(true);
        final JDialog dialog = new JDialog(this, "Новая игра?", true);
        dialog.setSize(300, 130);
        JLabel labelGameOver = new JLabel("Вы проиграли. Ваш результат " + gameGUI.getScore());
        JLabel labelNewGame = new JLabel("Хотите начать новую игру?");
        JButton keep = new JButton("Да");
        JButton cancel = new JButton("Нет");
        keep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameGUI.newGame();
                dialog.setVisible(false);
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameGUI.getGameBefore()) {
                    gameGUI.stopThread();
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
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        dialog.pack();
        return dialog;
    }
}