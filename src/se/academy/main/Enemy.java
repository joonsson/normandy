package se.academy.main;

import javax.swing.*;
import java.util.ArrayList;

public class Enemy extends Sprite {
    private ArrayList<Bomb> bombs;
    public static int speed;
    private final String enemyImg = "src/images/alien.png";
    public Enemy(int x, int y) {
        initEnemy(x, y);
    }
    private void initEnemy(int x, int y) {
        this.x = x;
        this.y = y;
        speed = 2;
        bombs = new ArrayList<>();
        ImageIcon ii = new ImageIcon(enemyImg);
        setImage(ii.getImage());
    }
    public void addBomb(Bomb b) {
        bombs.add(b);
    }
    public void removeBomb(Bomb b) {
        bombs.remove(b);
    }
    public void act(int direction) {
        this.x += direction * speed;
    }
    public ArrayList<Bomb> getBombs() {
        return bombs;
    }
}
