import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Board extends JPanel implements Runnable, Commons {
    private Dimension d;
    private ArrayList<Enemy> enemies;
    private Player player;
    private Shot shot;

    private final int ENEMY_INIT_X = 150;
    private final int ENEMY_INIT_Y = 5;
    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private final String explImg = "src/images/explosion";
    private String message = "Game over!";

    private Thread animator;

    public Board() {
        initBoard();
    }
    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.BLACK);

        gameInit();
        setDoubleBuffered(true);
    }
    @Override
    public void addNotify() {
        super.addNotify();
        gameInit();
    }
    public void gameInit() {
        enemies = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Enemy enemy = new Enemy(ENEMY_INIT_X + 18 * j, ENEMY_INIT_Y + 18 * i);
                enemies.add(enemy);
            }
        }
        Player player = new Player();
        shot = new Shot();

        if (animator == null || !inGame) {
            animator = new Thread(this);
            animator.start();
        }
    }
    public void drawEnemies(Graphics g) {
        Iterator it = enemies.iterator();
        for (Enemy enemy: enemies) {
            if (enemy.isVisible()) {
                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }
            if (enemy.isDying()) {
                enemy.die();
            }
        }
    }
    public void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        if (player.isDying()) {
            player.die();
            inGame = false;
        }
    }
    public void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }
    public void drawBombing(Graphics g) {
        for (Enemy e: enemies) {
            Enemy.Bomb b = e.getBomb();
            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        if (inGame) {
            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            drawEnemies(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    public void gameOver() {
        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 -30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2);
    }
    public void animationCycle() {
        if (deaths == NUMBER_OF_ENEMIES_TO_DESTROY) {
            inGame = false;
            message = "You won!";
        }
        player.act();

        if (shot.isVisible()) {
            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Enemy e : enemies) {
                int eX = e.getX();
                int eY = e.getY();

                if (e.isVisible() && shot.isVisible()) {
                    if (shotX >= (eX) && shotX <= (eX + ENEMY_WIDTH)
                            && shotY >= (eY) && shotY <= (eY + ENEMY_HEIGHT)) {
                        ImageIcon ii = new ImageIcon(explImg);
                        e.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }
            int y = shot.getY();
            y -= 4;
            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        for (Enemy enemy : enemies) {
            int x = enemy.getX();
            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                direction = -1;
                Iterator i1 = enemies.iterator();
                while (i1.hasNext()) {
                    Enemy e2 = (Enemy) i1.next();
                    e2.setY(e2.getY() + GO_DOWN);
                }
            }
            if (x <= BORDER_LEFT && direction != 1) {
                direction = 1;
                Iterator i2 = enemies.iterator();
                while (i2.hasNext()) {
                    Enemy e = (Enemy) i2.next();
                    e.setY(e.getY() + GO_DOWN);
                }
            }
        }
        Iterator it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = (Enemy) it.next();
            if (enemy.isVisible()) {
                int y = enemy.getY();
                if (y > GROUND - ENEMY_HEIGHT) {
                    inGame = false;
                    message = "Invasion! Ruuuuuun!";
                }
                enemy.act(direction);
            }
        }

        Random generator = new Random();

        for (Enemy enemy : enemies) {
            int shot = generator.nextInt(15);
            Enemy.Bomb b = enemy.getBomb();

            if (shot == CHANCE && enemy.isVisible() && b.isDestroyed()) {
                b.setDestroyed(false);
                b.setX(enemy.getX());
                b.setY(enemy.getY());
            }
            int bombX = b.getX();
            int bombY = b.getY();
            int pX = player.getX();
            int pY = player.getY();

            if (player.isVisible() && !b.isDestroyed()) {
                if (bombX >= (pX) && bombX <= (pX + PLAYER_WIDTH)
                        && bombY >= (pY) && bombY <= (pY + PLAYER_HEIGHT)) {
                    ImageIcon ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    b.setDestroyed(true);
                }
            }
            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);

                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

        while (inGame) {
            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e){
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
}
