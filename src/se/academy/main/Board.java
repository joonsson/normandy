package se.academy.main;

import javafx.scene.media.MediaPlayer;

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
    private ArrayList<Enemy> enemies2;
    private ArrayList<Enemy> enemies3;
    private Player player;
    private Shot shot;
    private Shot shot2;
    private Music music;

    private final int ENEMY_INIT_X = 150;
    private final int ENEMY_INIT_Y = 5;
    private int direction = -1;
    private int deaths = 0;
    private boolean speedUp = false;
    private boolean speedUp2 = false;
    private boolean newWave = false;

    private boolean inGame = true;
    private final String explImg = "src/images/explosion.png";
    private String message = "Game over!";

    private Thread animator;

    public Board() {
        initBoard();
    }
    private void initBoard() {
        music = new Music();
        music.start();
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
            for (int j = 0; j < 12; j++) {
                Enemy enemy = new Enemy(ENEMY_INIT_X + 50 * j, ENEMY_INIT_Y + 20 * i);
                enemies.add(enemy);
            }
        }
        player = new Player();
        shot = new Shot();
        shot2 = new Shot();

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
        if (speedUp) {
            for (Enemy enemy : enemies2) {
                if (enemy.isVisible()) {
                    g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
                }
                if (enemy.isDying()) {
                    enemy.die();
                }
            }
        }
        if (speedUp2) {
            for (Enemy enemy : enemies3) {
                if (enemy.isVisible()) {
                    g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
                }
                if (enemy.isDying()) {
                    enemy.die();
                }
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
        if (shot2.isVisible()) {
            g.drawImage(shot2.getImage(), shot2.getX(), shot2.getY(), this);
        }
    }
    public void drawBombing(Graphics g) {
        for (Enemy e : enemies) {
            Bomb b = e.getBomb();
                if (!b.isDestroyed()) {
                    g.drawImage(b.getImage(), b.getX(), b.getY(), this);
                }
            }
        if (speedUp) {
            for (Enemy e : enemies2) {
                Bomb b = e.getBomb();
                    if (!b.isDestroyed()) {
                        g.drawImage(b.getImage(), b.getX(), b.getY(), this);
                    }
                }
            }
        if (speedUp2) {
            for (Enemy e : enemies3) {
                Bomb b = e.getBomb();
                if (!b.isDestroyed()) {
                    g.drawImage(b.getImage(), b.getX(), b.getY(), this);
                }
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

        g.setColor(Color.black);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2);
    }
    public void animationCycle() {
        if (deaths == NUMBER_OF_ENEMIES_TO_DESTROY) {
            inGame = false;
            message = "You won!";
        }
        if (newWave) {
            if (!speedUp2) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 12; j++) {
                        Enemy ene = new Enemy(ENEMY_INIT_X + 50 * j, ENEMY_INIT_Y + 20 * i);
                        enemies2.add(ene);
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 12; j++) {
                        Enemy ene = new Enemy(ENEMY_INIT_X + 50 * j, ENEMY_INIT_Y + 20 * i);
                        enemies3.add(ene);
                    }
                }
            }
            newWave = false;
        }
        player.act();

        if (shot.isVisible() || shot2.isVisible()) {
            int shotX = shot.getX();
            int shot2X = shot2.getX();
            int shotY = shot.getY();
            int shot2Y = shot2.getY();

            for (Enemy e : enemies) {
                int eX = e.getX();
                int eY = e.getY();

                if (e.isVisible() && shot.isVisible()) {
                    if (shotX >= (eX) && shotX <= (eX + ENEMY_WIDTH)
                            && shotY >= (eY) && shotY <= (eY + ENEMY_HEIGHT)) {
                        Explosion exp = new Explosion();
                        exp.start();
                        ImageIcon ii = new ImageIcon(explImg);
                        e.setImage(ii.getImage());
                        e.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
                if (e.isVisible() && shot2.isVisible()) {
                    if (shot2X >= (eX) && shot2X <= (eX + ENEMY_WIDTH)
                            && shot2Y >= (eY) && shot2Y <= (eY + ENEMY_HEIGHT)) {
                        Explosion exp = new Explosion();
                        exp.start();
                        ImageIcon ii = new ImageIcon(explImg);
                        e.setImage(ii.getImage());
                        e.setDying(true);
                        deaths++;
                        shot2.die();
                    }
                }
            }
            if (speedUp) {
                for (Enemy e : enemies2) {
                    int eX = e.getX();
                    int eY = e.getY();

                    if (e.isVisible() && shot.isVisible()) {
                        if (shotX >= (eX) && shotX <= (eX + ENEMY_WIDTH)
                                && shotY >= (eY) && shotY <= (eY + ENEMY_HEIGHT)) {
                            Explosion exp = new Explosion();
                            exp.start();
                            ImageIcon ii = new ImageIcon(explImg);
                            e.setImage(ii.getImage());
                            e.setDying(true);
                            deaths++;
                            shot.die();
                        }
                    }
                    if (e.isVisible() && shot2.isVisible()) {
                        if (shot2X >= (eX) && shot2X <= (eX + ENEMY_WIDTH)
                                && shot2Y >= (eY) && shot2Y <= (eY + ENEMY_HEIGHT)) {
                            Explosion exp = new Explosion();
                            exp.start();
                            ImageIcon ii = new ImageIcon(explImg);
                            e.setImage(ii.getImage());
                            e.setDying(true);
                            deaths++;
                            shot2.die();
                        }
                    }
                }
            }
            if (speedUp2) {
                for (Enemy e : enemies3) {
                    int eX = e.getX();
                    int eY = e.getY();

                    if (e.isVisible() && shot.isVisible()) {
                        if (shotX >= (eX) && shotX <= (eX + ENEMY_WIDTH)
                                && shotY >= (eY) && shotY <= (eY + ENEMY_HEIGHT)) {
                            Explosion exp = new Explosion();
                            exp.start();
                            ImageIcon ii = new ImageIcon(explImg);
                            e.setImage(ii.getImage());
                            e.setDying(true);
                            deaths++;
                            shot.die();
                        }
                    }
                    if (e.isVisible() && shot2.isVisible()) {
                        if (shot2X >= (eX) && shot2X <= (eX + ENEMY_WIDTH)
                                && shot2Y >= (eY) && shot2Y <= (eY + ENEMY_HEIGHT)) {
                            Explosion exp = new Explosion();
                            exp.start();
                            ImageIcon ii = new ImageIcon(explImg);
                            e.setImage(ii.getImage());
                            e.setDying(true);
                            deaths++;
                            shot2.die();
                        }
                    }
                }
            }
            if (shot.isVisible()) {
                int y = shot.getY();
                y -= 8;
                if (y < 0) {
                    shot.die();
                } else {
                    shot.setY(y);
                }
            }
            if (shot2.isVisible()) {

                int y = shot2.getY();
                y -= 8;
                if (y < 0) {
                    shot2.die();
                } else {
                    shot2.setY(y);
                }
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
        if (speedUp) {
            for (Enemy enemy : enemies2) {
                int x = enemy.getX();
                if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                    direction = -1;
                    Iterator i1 = enemies2.iterator();
                    while (i1.hasNext()) {
                        Enemy e2 = (Enemy) i1.next();
                        e2.setY(e2.getY() + GO_DOWN);
                    }
                }
                if (x <= BORDER_LEFT && direction != 1) {
                    direction = 1;
                    Iterator i2 = enemies2.iterator();
                    while (i2.hasNext()) {
                        Enemy e = (Enemy) i2.next();
                        e.setY(e.getY() + GO_DOWN);
                    }
                }
            }
        }
        if (speedUp2) {
            for (Enemy enemy : enemies3) {
                int x = enemy.getX();
                if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                    direction = -1;
                    Iterator i1 = enemies3.iterator();
                    while (i1.hasNext()) {
                        Enemy e2 = (Enemy) i1.next();
                        e2.setY(e2.getY() + GO_DOWN);
                    }
                }
                if (x <= BORDER_LEFT && direction != 1) {
                    direction = 1;
                    Iterator i2 = enemies3.iterator();
                    while (i2.hasNext()) {
                        Enemy e = (Enemy) i2.next();
                        e.setY(e.getY() + GO_DOWN);
                    }
                }
            }
        }
        Iterator it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = (Enemy) it.next();
            if (enemy.isVisible()) {
                int y = enemy.getY();
                if (!speedUp && y > 200) {
                    speedUp = true;
                    Enemy.speed *= 2;
                    newWave = true;
                }
                if (!speedUp2 && y > 400) {
                    speedUp2 = true;
                    Enemy.speed *= 2;
                    newWave = true;
                }
                if (y > GROUND - ENEMY_HEIGHT) {
                    inGame = false;
                    message = "Invasion! Ruuuuuun!";
                }
                enemy.act(direction);
            }
        }
        if (speedUp && enemies2.size() != 0) {
            it = enemies2.iterator();
            while (it.hasNext()) {
                Enemy enemy = (Enemy) it.next();
                if (enemy.isVisible()) {
                    int y = enemy.getY();
                    if (!speedUp && y > 200) {
                        speedUp = true;
                        Enemy.speed *= 2;
                        newWave = true;
                    }
                    if (!speedUp2 && y > 400) {
                        speedUp2 = true;
                        Enemy.speed *= 2;
                        newWave = true;
                    }
                    if (y > GROUND - ENEMY_HEIGHT) {
                        inGame = false;
                        message = "Invasion! Ruuuuuun!";
                    }
                    enemy.act(direction);
                }
            }
        }
        if (speedUp2 && enemies3.size() != 0) {
            it = enemies3.iterator();
            while (it.hasNext()) {
                Enemy enemy = (Enemy) it.next();
                if (enemy.isVisible()) {
                    int y = enemy.getY();
                    if (!speedUp && y > 200) {
                        speedUp = true;
                        Enemy.speed *= 2;
                        newWave = true;
                    }
                    if (!speedUp2 && y > 400) {
                        speedUp2 = true;
                        Enemy.speed *= 2;
                        newWave = true;
                    }
                    if (y > GROUND - ENEMY_HEIGHT) {
                        inGame = false;
                        message = "Invasion! Ruuuuuun!";
                    }
                    enemy.act(direction);
                }
            }
        }

        Random generator = new Random(24842);
        boolean fired = false;
        for (Enemy enemy : enemies) {
            int shot = generator.nextInt(15);
            Bomb b = enemy.getBomb();
            if (shot <= CHANCE && enemy.isVisible() && b.isDestroyed() && !fired) {
                b.setX(enemy.getX());
                b.setY(enemy.getY());
                b.setDestroyed(false);
                fired = true;
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
        if (speedUp) {
            boolean hasFired = false;
            for (Enemy enemy : enemies2) {
                int shot = generator.nextInt(15);
                Bomb b = enemy.getBomb();
                if (shot == CHANCE && enemy.isVisible() && b.isDestroyed() && !hasFired) {
                    b.setX(enemy.getX());
                    b.setY(enemy.getY());
                    b.setDestroyed(false);
                    hasFired = true;
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
        if (speedUp2) {
            boolean hasFired = false;
            for (Enemy enemy : enemies3) {
                int shot = generator.nextInt(15);
                Bomb b = enemy.getBomb();
                if (shot == CHANCE && enemy.isVisible() && b.isDestroyed() && !hasFired) {
                    b.setX(enemy.getX());
                    b.setY(enemy.getY());
                    b.setDestroyed(false);
                    hasFired = true;
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
                        Shoot shoot = new Shoot();
                        shot = new Shot(x - 4, y);
                        shoot.start();
                    } else if (!shot2.isVisible()) {
                        Shoot shoot = new Shoot();
                        shot2 = new Shot(x + 20, y);
                        shoot.start();
                    }
                }
            }
        }
    }
}
