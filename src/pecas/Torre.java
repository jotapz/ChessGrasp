package pecas;

import game.Tabuleiro;
import util.enume.TiposPecas;

import java.awt.image.BufferedImage;

public class Torre extends Peca{
    public Torre(Tabuleiro tabuleiro, int linha, int coluna, boolean ehBranco){
        super(tabuleiro);
        this.linha = linha;
        this.coluna = coluna;

        this.xPos = coluna * tabuleiro.tileSize;
        this.yPos = linha * tabuleiro.tileSize;

        this.ehBranco = ehBranco;
        this.nome = "Torre";
        this.tipo = TiposPecas.TORRE;

        this.sprite = sheet.getSubimage(4 * sheetScale, ehBranco ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tabuleiro.tileSize, tabuleiro.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int colunas, int linhas) {
        return this.coluna == colunas || this.linha == linhas;
    }

    public boolean moveCollidesWithPiece(int colunas, int linhas) {

        //esquerda
        if(this.coluna > colunas)
            for(int c = this.coluna - 1; c > colunas; c--)
                if (tabuleiro.getPeca(c, this.linha) != null)
                    return true;
        //direita
        if(this.coluna < colunas)
            for(int c = this.coluna + 1; c > colunas; c++)
                if (tabuleiro.getPeca(c, this.linha) != null)
                    return true;
        //cima
        if(this.linha > linhas)
            for(int l = this.linha - 1; l > colunas; l--)
                if (tabuleiro.getPeca(this.linha, l) != null)
                    return true;
        //baixo
        if(this.linha < linhas)
            for(int l = this.linha + 1; l < colunas; l++)
                if (tabuleiro.getPeca(this.linha, l) != null)
                    return true;

        return false;
    }


}
