import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

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
            animator = new Thread((this);
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
}
