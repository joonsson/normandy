package se.academy.main;

import java.awt.*;

public class Sprite {
    private boolean visible;
    private Image image;
    protected int x;
    protected int y;
    protected boolean dying;
    protected int dx;

    public Sprite() {
        visible = true;
    }
    public void die() {
        visible = false;
    }
    public boolean isVisible() {
        return visible;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }
}
