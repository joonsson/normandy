package se.academy.main;

import javax.swing.*;
import java.util.ArrayList;

public class Enemy extends Sprite {
    private Bomb bomb;
    public static int speed;
    private final String enemyImg = "src/images/alien.png";
    public Enemy(int x, int y) {
        initEnemy(x, y);
    }
    private void initEnemy(int x, int y) {
        this.x = x;
        this.y = y;
        speed = 2;
        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(enemyImg);
        setImage(ii.getImage());
    }
    public void act(int direction) {
        this.x += direction * speed;
    }
    public Bomb getBomb() {
        return bomb;
    }
}
