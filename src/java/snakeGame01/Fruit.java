package snakeGame01;

import java.awt.Point;

public class Fruit {
    private Point point;
    private boolean fruit;

    public Fruit() {
        point = new Point();
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean isFruit() {
        return fruit;
    }

    public void setFruit(boolean fruit) {
        this.fruit = fruit;
    }
}
