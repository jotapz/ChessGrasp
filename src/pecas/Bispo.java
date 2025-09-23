package pecas;

import game.Tabuleiro;
import util.enume.TiposPecas;

import java.awt.image.BufferedImage;

public class Bispo extends Peca{
    public Bispo(Tabuleiro tabuleiro, int linha, int coluna, boolean ehBranco){
        super(tabuleiro);
        this.linha = linha;
        this.coluna = coluna;

        this.xPos = coluna * tabuleiro.tileSize;
        this.yPos = linha * tabuleiro.tileSize;

        this.ehBranco = ehBranco;
        this.nome = "Bispo";
        this.tipo = TiposPecas.BISPO;

        this.sprite = sheet.getSubimage(2* sheetScale, ehBranco ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(tabuleiro.tileSize, tabuleiro.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(int colunas, int linhas) {
        return Math.abs(this.coluna - colunas) == Math.abs(this.linha - linhas);
    }

    public boolean moveCollidesWithPiece(int colunas, int linhas) {

        // cima esquerda
        if(this.coluna > colunas && this.linha > linhas)
            for(int i = 1; i < Math.abs(this.coluna - colunas); i++)
                if(tabuleiro.getPeca(this.coluna - i, this.linha - i) != null)
                    return true;
        //cima direita
        if(this.coluna < colunas && this.linha > linhas)
            for(int i = 1; i < Math.abs(this.coluna - colunas); i++)
                if(tabuleiro.getPeca(this.coluna + i, this.linha - i) != null)
                    return true;
        // baixo esquerda
        if(this.coluna > colunas && this.linha < linhas)
            for(int i = 1; i < Math.abs(this.coluna - colunas); i++)
                if(tabuleiro.getPeca(this.coluna - i, this.linha + i) != null)
                    return true;
        // baixo direita
        if(this.coluna < colunas && this.linha < linhas)
        for(int i = 1; i < Math.abs(this.coluna - colunas); i++)
            if(tabuleiro.getPeca(this.coluna + i, this.linha + i) != null)
                return true;



        return false;
}
}
