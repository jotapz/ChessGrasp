package pecas;

import game.Tabuleiro;
import util.enume.TiposPecas;

import java.awt.image.BufferedImage;

public class Cavalo extends Peca{
    private int colunas;
    private int linhas;

    public Cavalo(Tabuleiro tabuleiro, int linha, int coluna, boolean ehBranco){
        super(tabuleiro);
        this.linha = linha;
        this.coluna = coluna;

        this.xPos = coluna * tabuleiro.tileSize;
        this.yPos = linha * tabuleiro.tileSize;

        this.ehBranco = ehBranco;
        this.nome = "Cavalo";
        this.tipo = TiposPecas.CAVALO;


        this.sprite = sheet.getSubimage(3 * sheetScale, ehBranco ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tabuleiro.tileSize, tabuleiro.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int colunas, int linhas) {
        return Math.abs(colunas - this.coluna) * Math.abs(linhas - this.linha) == 2;
    }
}
