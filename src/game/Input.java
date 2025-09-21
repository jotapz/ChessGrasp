package game;
import pecas.Peca;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input extends MouseAdapter {

    Tabuleiro tabuleiro;

    public Input(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
    }


    @Override
    public void mousePressed(MouseEvent e) {

        int coluna = e.getX() / tabuleiro.tileSize;
        int linha = e.getY() / tabuleiro.tileSize;

        Peca pecaXY = tabuleiro.getPeca(coluna, linha);
        if (pecaXY != null) {
            tabuleiro.selectedPeca = pecaXY;
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {

        if (tabuleiro.selectedPeca != null){
            tabuleiro.selectedPeca.xPos = e.getX() - tabuleiro.tileSize / 2;
            tabuleiro.selectedPeca.yPos = e.getY() - tabuleiro.tileSize / 2;

            tabuleiro.repaint();

        }
    }



    @Override
    public void mouseReleased(MouseEvent e) {

        int coluna = e.getX() / tabuleiro.tileSize;
        int linha = e.getY() / tabuleiro.tileSize;

        if (tabuleiro.selectedPeca != null) {
            Move move = new Move(tabuleiro, tabuleiro.selectedPeca, coluna, linha);

            if (tabuleiro.isValidMove(move)){
                tabuleiro.makeMove(move);
            } else {
                tabuleiro.selectedPeca.xPos = tabuleiro.selectedPeca.coluna * tabuleiro.tileSize;
                tabuleiro.selectedPeca.yPos = tabuleiro.selectedPeca.linha * tabuleiro.tileSize;
            }
        }

        tabuleiro.selectedPeca = null;
        tabuleiro.repaint();
    }



}
