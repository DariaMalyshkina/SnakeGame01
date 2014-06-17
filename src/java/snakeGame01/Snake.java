package snakeGame01;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Snake {
    private Point[] pointSnake = new Point[1000];
    private int length;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private int directionX;
    private int directionY;
    public int[] x = new int[600];
    public int[] y = new int[400];

    public Snake() {
        this.left = false;
        this.right = true;
        this.up = true;
        this.down = true;
        this.directionX = 10;
        this.directionY = 0;
        this.length = 3;
        this.x[0] = 100;
        this.y[0] = 200;
    }

    public void pressing(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (left) {
                    directionX = -10;
                    directionY = 0;
                    right = false;
                    up = true;
                    down = true;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (right) {
                    directionX = 10;
                    directionY = 0;
                    left = false;
                    up = true;
                    down = true;
                }
                break;
            case KeyEvent.VK_UP:
                if (up) {
                    directionX = 0;
                    directionY = -10;
                    down = false;
                    right = true;
                    left = true;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (down) {
                    directionX = 0;
                    directionY = 10;
                    up = false;
                    right = true;
                    left = true;
                }
                break;
            default:
                break;
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Point[] getPointSnake() {
        return pointSnake;
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }
}
