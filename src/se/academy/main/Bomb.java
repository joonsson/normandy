package se.academy.main;

import javax.swing.*;

public class Bomb extends Sprite {
    private final String bombImg = "src/images/bomb.png";
    private boolean destroyed;

    public Bomb(int x, int y) {
        initBomb(x, y);
    }
    private void initBomb(int x, int y) {
        setDestroyed(true);
        this.x = x;
        this.y = y;
        ImageIcon ii = new ImageIcon(bombImg);
        setImage(ii.getImage());
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

}
