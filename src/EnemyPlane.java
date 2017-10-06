import java.awt.*;

public class EnemyPlane {
    private int startX;
    private int x;
    private int startY;
    private int y;
    private int speed;
    private Image image;
    private boolean vis;

    public EnemyPlane(int startX, int startY, int speed, Image image) {
        this.startX = startX;
        this.x = startX;
        this.startY = startY;
        this.y = startY;
        this.speed = speed;
        this.image = image;
        vis = true;
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
