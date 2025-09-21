package pecas;

import game.Tabuleiro;

import java.awt.image.BufferedImage;

public class Rei extends Peca{
    public Rei(Tabuleiro tabuleiro, int linha, int coluna, boolean ehBranco){
        super(tabuleiro);
        this.linha = linha;
        this.coluna = coluna;

        this.xPos = coluna * tabuleiro.tileSize;
        this.yPos = linha * tabuleiro.tileSize;

        this.ehBranco = ehBranco;
        this.nome = "Rei";

        this.sprite = sheet.getSubimage(0, ehBranco ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tabuleiro.tileSize, tabuleiro.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int colunas, int linhas) {
        int dx = Math.abs(colunas - this.coluna);
        int dy = Math.abs(linhas - this.linha);
        return Math.max(dx, dy) == 1;

    }
}
