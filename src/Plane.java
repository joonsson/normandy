import java.awt.*;

public class Plane {
    private int startX;
    private int x;
    private int startY;
    private int y;
    private int speed;
    private int lives;
    private boolean canFire;
    private Image image;
    private boolean vis;

    public Plane(int x, int y, int speed, boolean canFire, Image image) {
        this.x = x;
        startX = x;
        this.y = y;
        startY = y;
        this.speed = speed;
        this.canFire = canFire;
        this.image = image;
        vis = true;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isVis() {
        return vis;
    }

    public void setVis(boolean vis) {
        this.vis = vis;
    }
}
