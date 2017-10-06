import javax.swing.*;
import java.awt.*;

public class Enemy extends Sprite {
    private Bomb bomb;
    private final String enemyImg = "src/images/alien.png";
    public Enemy(int x, int y) {
        initEnemy(x, y);
    }
    private void initEnemy(int x, int y) {
        this.x = x;
        this.y = y;
        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(enemyImg);
        setImage(ii.getImage());
    }
    public void act(int direction) {
        this.x += direction;
    }
    public Bomb getBomb() {
        return bomb;
    }
    public class Bomb extends Sprite {
        private final String bombImg = "bomb.png";
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
}
