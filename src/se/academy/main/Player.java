package se.academy.main;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Player extends Sprite implements Commons {
    private final int START_X = 640;
    private final int START_Y = 600;
    private final String playerImg = "src/images/craft.png";
    private int width;

    public Player() {
        initPlayer();
    }
    private void initPlayer() {
        ImageIcon ii = new ImageIcon(playerImg);
        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }
    public void act() {
        x += dx;
        if (x <= 2) {
            x = 2;
        }
        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }
    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            dx = -4;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 4;
        }
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
}
