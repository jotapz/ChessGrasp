package pecas;

import game.Tabuleiro;

import java.awt.image.BufferedImage;

public class Rainha extends Peca{
    public Rainha(Tabuleiro tabuleiro, int linha, int coluna, boolean ehBranco){
        super(tabuleiro);
        this.linha = linha;
        this.coluna = coluna;

        this.xPos = coluna * tabuleiro.tileSize;
        this.yPos = linha * tabuleiro.tileSize;

        this.ehBranco = ehBranco;
        this.nome = "Rainha";

        this.sprite = sheet.getSubimage(sheetScale, ehBranco ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tabuleiro.tileSize, tabuleiro.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int colunas, int linhas) {
        return this.coluna == colunas || this.linha == linhas || Math.abs(this.coluna - colunas) == Math.abs(this.linha - linhas);
    }

    public boolean moveCollidesWithPiece(int colunas, int linhas) {
        if (this.coluna == coluna || this.linha == linha) {
            //esquerda
            if (this.coluna > colunas)
                for (int c = this.coluna - 1; c > colunas; c--)
                    if (tabuleiro.getPeca(c, this.linha) != null)
                        return true;
            //direita
            if (this.coluna < colunas)
                for (int c = this.coluna + 1; c > colunas; c++)
                    if (tabuleiro.getPeca(c, this.linha) != null)
                        return true;
            //cima
            if (this.linha > linhas)
                for (int l = this.linha - 1; l > colunas; l--)
                    if (tabuleiro.getPeca(this.linha, l) != null)
                        return true;
            //baixo
            if (this.linha < linhas)
                for (int l = this.linha + 1; l < colunas; l++)
                    if (tabuleiro.getPeca(this.linha, l) != null)
                        return true;
        } else {

            // cima esquerda
            if (this.coluna > colunas && this.linha > linhas)
                for (int i = 1; i < Math.abs(this.coluna - colunas); i++)
                    if (tabuleiro.getPeca(this.coluna - i, this.linha - i) != null)
                        return true;
            //cima direita
            if (this.coluna < colunas && this.linha > linhas)
                for (int i = 1; i < Math.abs(this.coluna - colunas); i++)
                    if (tabuleiro.getPeca(this.coluna + i, this.linha - i) != null)
                        return true;
            // baixo esquerda
            if (this.coluna > colunas && this.linha < linhas)
                for (int i = 1; i < Math.abs(this.coluna - colunas); i++)
                    if (tabuleiro.getPeca(this.coluna - i, this.linha + i) != null)
                        return true;
            // baixo direita
            if (this.coluna < colunas && this.linha < linhas)
                for (int i = 1; i < Math.abs(this.coluna - colunas); i++)
                    if (tabuleiro.getPeca(this.coluna + i, this.linha + i) != null)
                        return true;
        }
            return false;
        }
}
