import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    }

}
