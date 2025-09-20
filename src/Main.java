
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.plaf.DimensionUIResource;

public class Main {
    public static void main(String[] args) throws Exception {

        //interface jframe
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(new Color(255, 141, 0));
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new DimensionUIResource(1000, 1000));
        frame.setLocationRelativeTo(null);
        Board board = new Board();
        frame.add(board);
        frame.setVisible(true);

    }
}
