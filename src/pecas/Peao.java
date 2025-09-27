package pecas;

import game.Tabuleiro;
import util.enume.TiposPecas;

import java.awt.image.BufferedImage;

public class Peao extends Peca{
    public Peao(Tabuleiro tabuleiro, int linha, int coluna, boolean ehBranco){
        super(tabuleiro);
        this.linha = linha;
        this.coluna = coluna;

        this.xPos = coluna * tabuleiro.tileSize;
        this.yPos = linha * tabuleiro.tileSize;

        this.ehBranco = ehBranco;
        this.nome = "Peao";
        this.tipo = TiposPecas.PEAO;

        this.sprite = sheet.getSubimage(5 * sheetScale, ehBranco ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tabuleiro.tileSize, tabuleiro.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int colunas, int linhas) {
        int colorIndex = ehBranco ? 1: -1;

        // Captura direita
        if (this.coluna == colunas && linhas == this.linha - colorIndex && tabuleiro.getPeca(colunas, linhas) == null){
            return true;
        }

        if (this.coluna == (ehBranco ? 6 : 1) && ehPrimeiroMovimento && this.coluna == colunas && linhas == this.linha - colorIndex * 2 && tabuleiro.getPeca(colunas, linhas) == null && tabuleiro.getPeca(colunas, linhas + colorIndex) == null){
            return true;
        }

        // Captura esquerda
        if (colunas == this.coluna - 1 && linhas == this.linha - colorIndex && tabuleiro.getPeca(colunas, linhas) != null){
            return true;
        }

        if (colunas == this.coluna + 1 && linhas == this.linha - colorIndex && tabuleiro.getPeca(colunas, linhas) != null){
            return true;
        }

//        // en passant esquerda
//        if (tabuleiro.getTileNum(coluna, linha) == tabuleiro.enPassantTile && colunas == this.coluna - 1 && linhas == this.linha - colorIndex && tabuleiro.getPeca(coluna, linha +colorIndex) != null){
//            return true;
//        }
//
//        // en passant direita
//        if (tabuleiro.getTileNum(coluna, linha) == tabuleiro.enPassantTile && colunas == this.coluna + 1 && linhas == this.linha - colorIndex && tabuleiro.getPeca(coluna, linha +colorIndex) != null){
//            return true;
//        }

        // captura esquerda ou direita
        if (Math.abs(colunas - this.coluna) == 1 && linhas == this.linha - colorIndex && tabuleiro.getPeca(colunas, linhas) != null)
            return true;

        // en passant esquerda ou direita
        if (tabuleiro.getTileNum(colunas, linhas) == tabuleiro.enPassantTile && Math.abs(colunas - this.coluna) == 1 && linhas == this.linha - colorIndex && tabuleiro.getPeca(coluna, linha + colorIndex) != null)
            return true;


        return false;
    }
}
