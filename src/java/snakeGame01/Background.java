package snakeGame01;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Background extends JPanel {
    private Image image;
    private String imageString;

    public Background(String imageString) {
        this.imageString = imageString;
        loadImage();
    }

    private void loadImage() {
        try {
            image = ImageIO.read(new File(imageString));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
