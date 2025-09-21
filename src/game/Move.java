package game;
import pecas.Peca;

public class Move {

    int colAntiga;
    int linAntiga;
    int colNova;
    int linNova;

    Peca peca;
    Peca capturar;

    public Move(Tabuleiro tabuleiro, Peca peca, int colNova, int linNova){
        this.colAntiga = peca.coluna;
        this.linAntiga = peca.linha;
        this.colNova = colNova;
        this.linNova = linNova;

        this.peca = peca;
        this.capturar = tabuleiro.getPeca(colNova, linNova);
    }
}
