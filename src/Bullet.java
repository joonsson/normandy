import java.awt.*;

public class Bullet {
    private int x;
    private int y;
    private int speed;
    private Image image;
    private boolean vis;

    public Bullet(int x, int y, int speed, Image image) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.image = image;
        this.vis = true;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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
