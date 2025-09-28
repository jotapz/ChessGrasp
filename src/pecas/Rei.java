package pecas;

import game.Tabuleiro;
import util.enume.TiposPecas;

import java.awt.image.BufferedImage;

public class Rei extends Peca {

    public Rei(Tabuleiro tabuleiro, int linha, int coluna, boolean ehBranco) {
        super(tabuleiro);
        this.linha = linha;
        this.coluna = coluna;

        this.xPos = coluna * tabuleiro.tileSize;
        this.yPos = linha * tabuleiro.tileSize;

        this.ehBranco = ehBranco;
        this.nome = "Rei";
        this.tipo = TiposPecas.REI;

        this.sprite = sheet.getSubimage(0, ehBranco ? 0 : sheetScale, sheetScale, sheetScale)
                .getScaledInstance(tabuleiro.tileSize, tabuleiro.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int colunas, int linhas) {
        int dx = Math.abs(colunas - this.coluna);
        int dy = Math.abs(linhas - this.linha);

        // game.movimento normal do rei (1 casa em qualquer direção)
        if (Math.max(dx, dy) == 1) {
            return true;
        }

        // --- ROQUE ---
        // só na mesma linha, deslocamento de 2 colunas
        if (dy == 0 && dx == 2 && this.ehPrimeiroMovimento) {
            // direção: +2 = roque pequeno, -2 = roque grande
            int dir = (colunas - this.coluna > 0) ? 1 : -1;

            // verificar se há torre correta na extremidade
            int torreColuna = (dir == 1) ? 7 : 0;
            Peca torre = tabuleiro.getPeca(torreColuna, this.linha);

            if (torre != null && torre.nome.equals("Torre") && torre.ehBranco == this.ehBranco && torre.ehPrimeiroMovimento) {
                // verificar se não há peças entre rei e torre
                int c = this.coluna + dir;
                while (c != torreColuna) {
                    if (tabuleiro.getPeca(c, this.linha) != null) {
                        return false; // bloqueado
                    }
                    c += dir;
                }
                return true;
            }
        }

        return false;
    }

    public void realizarRoqueSeNecessario(int novaColuna) {
        int dx = novaColuna - this.coluna;
        if (Math.abs(dx) == 2) {
            int dir = (dx > 0) ? 1 : -1;
            int torreColuna = (dir == 1) ? 7 : 0;
            Peca torre = tabuleiro.getPeca(torreColuna, this.linha);

            if (torre != null && torre.nome.equals("Torre")) {
                // nova posição da torre: imediatamente ao lado do rei
                int novaColTorre = this.coluna + dir;
                torre.coluna = novaColTorre;
                torre.linha = this.linha;
                torre.xPos = torre.coluna * tabuleiro.tileSize;
                torre.yPos = torre.linha * tabuleiro.tileSize;
                torre.ehPrimeiroMovimento = false;
            }
        }
    }
}