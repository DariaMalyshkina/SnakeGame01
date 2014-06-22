package snakeGame01;

public class Gamer {
    private String name;
    private int score;
    private String level;
    private String obstacle;

    public Gamer(String name, int score, String level, String obstacle) {
        this.name = name;
        this.score = score;
        this.level = level;
        this.obstacle = obstacle;
    }

    public String getStringGamer() {
        return name + " " + score + " баллов (Уровень " + level + ", " + obstacle + ")";
    }
}