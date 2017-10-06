import javax.swing.*;
import java.awt.EventQueue;

public class Normandy extends JFrame implements Commons {
    public Normandy() {
        initUI();
    }
    private void initUI() {
        add(new Board());
        setTitle("Normandy");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(()-> {
            Normandy ex = new Normandy();
            ex.setVisible(true);
        });
    }
}
