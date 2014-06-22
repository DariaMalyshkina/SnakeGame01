package snakeGame01;

import javax.swing.*;
import java.awt.event.*;

public class WindowReaction extends JFrame implements WindowListener {
    private GameGUI gameGUI;

    public WindowReaction(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
            gameGUI.stopThread();
        }
        Object[] options = {"Да", "Нет"};
        int result = JOptionPane
                .showOptionDialog(e.getWindow(), "Вы уверены, что хотите выйти из игры?", "Вы уверены?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (result == JOptionPane.YES_OPTION) {
            e.getWindow().setVisible(false);
            System.exit(0);
        } else {
            gameGUI.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            renewGame();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
        renewGame();
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        renewGame();
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    private void renewGame() {
        if (gameGUI.getGameBefore() && !gameGUI.getGameOver()) {
            gameGUI.startThread();
        }
    }
}

