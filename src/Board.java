
import java.awt.*;
import javax.swing.JPanel;

public class Board extends JPanel {

    public int tileSize = 85;

    int coluna = 8;
    int linha = 8;

    public Board(){
        this.setPreferredSize(new Dimension(coluna * tileSize, linha * tileSize));
      //  this.setBackground(Color.blue);
    }

    
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        for (int c = 0; c < coluna; c++)
        for (int l = 0; l < linha; l++) {
            g2d.setColor((c+l) % 2 == 0 ? new Color(255, 129, 0) : new Color(255, 201, 0));
            g2d.fillRect(c * tileSize,  l * tileSize, tileSize, tileSize);
        }
    }
    
}
