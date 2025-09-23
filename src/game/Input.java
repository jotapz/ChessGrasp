package game;

import pecas.Peca;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {

    private final Tabuleiro tabuleiro;

    private int origemCol;
    private int origemLin;

    public Input(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / tabuleiro.tileSize;
        int lin = e.getY() / tabuleiro.tileSize;

        Peca p = tabuleiro.getPeca(col, lin);

        // só permite selecionar se for a vez daquela cor
        if (p != null && p.ehBranco == tabuleiro.isTurnoBrancas()) {
            tabuleiro.selectedPeca = p;
            origemCol = p.coluna;
            origemLin = p.linha;
        } else {
            tabuleiro.selectedPeca = null; // ignora clique em peça do oponente
        }
        tabuleiro.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (tabuleiro.selectedPeca == null) return;

        // arrasto visual da peça (centra no cursor)
        tabuleiro.selectedPeca.xPos = e.getX() - tabuleiro.tileSize / 2;
        tabuleiro.selectedPeca.yPos = e.getY() - tabuleiro.tileSize / 2;
        tabuleiro.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (tabuleiro.selectedPeca == null) return;

        int alvoCol = clamp(e.getX() / tabuleiro.tileSize, 0, 7);
        int alvoLin = clamp(e.getY() / tabuleiro.tileSize, 0, 7);

        Peca p = tabuleiro.selectedPeca;

        Move move = new Move(tabuleiro, p, alvoCol, alvoLin);

        // tenta aplicar (respeita turno, validade, xeque…)
        tabuleiro.makeMove(move);

        // checa se moveu mesmo: compara posição final com alvo
        boolean moveAplicado = (p.coluna == alvoCol && p.linha == alvoLin);

        if (!moveAplicado) {
            // snap-back
            p.coluna = origemCol;
            p.linha  = origemLin;
            p.xPos   = origemCol * tabuleiro.tileSize;
            p.yPos   = origemLin  * tabuleiro.tileSize;
        }

        tabuleiro.selectedPeca = null;
        tabuleiro.repaint();
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}