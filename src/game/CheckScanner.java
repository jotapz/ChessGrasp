package game;

import pecas.Peca;

public class CheckScanner {
    Tabuleiro tabuleiro;
    public CheckScanner(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
    }

    public boolean reiEhAtacado(Move move){

        Peca rei = tabuleiro.achaRei(move.peca.ehBranco);
        assert rei != null;

        int reiCol = rei.coluna;
        int reiLin = rei.linha;

        if(tabuleiro.selectedPeca != null && tabuleiro.selectedPeca.nome.equals("Rei")){
            reiCol = move.colNova;
            reiLin = move.linNova;
        }

        return  hitByTorre(move.colNova, move.linNova, rei, reiCol, reiLin, 0, 1) || // cima
                hitByTorre(move.colNova, move.linNova, rei, reiCol, reiLin, 1, 0) || // direita
                hitByTorre(move.colNova, move.linNova, rei, reiCol, reiLin, 0, -1) ||// baixo
                hitByTorre(move.colNova, move.linNova, rei, reiCol, reiLin, -1, 0) || //esquerda

                hitByBispo(move.colNova, move.linNova, rei, reiCol, reiLin, -1, -1) || // esquerda cima
                hitByBispo(move.colNova, move.linNova, rei, reiCol, reiLin, 1, -1) || // direita cima
                hitByBispo(move.colNova, move.linNova, rei, reiCol, reiLin, 1, 1) ||// baixo direita
                hitByBispo(move.colNova, move.linNova, rei, reiCol, reiLin, -1, 1) || // baixo esquerda

                hitByCavalo(move.colNova, move.linNova, rei, reiCol, reiLin) ||
                hitByPeao(move.colNova, move.linNova, rei, reiCol, reiLin) ||
                hitByRei(rei, reiCol, reiLin);
    }

    private  boolean hitByTorre(int coluna, int linha, Peca rei, int reiCol, int reiLin, int colVal, int linVal){
        for (int i = 1; i < 8; i++) {
            if(reiCol + (i * colVal) == coluna && reiLin + (i * linVal) == linha){
                break;
            }
            Peca peca = tabuleiro.getPeca(reiCol + (i * colVal), reiLin + (i * linVal));
            if (peca != null && peca != tabuleiro.selectedPeca){
                if (!tabuleiro.mesmoTime(peca, rei) && (peca.nome.equals("Torre") || peca.nome.equals("Rainha"))){
                    return true;
                }
                break;
            }
        }
        return false;
    }


    private  boolean hitByBispo(int coluna, int linha, Peca rei, int reiCol, int reiLin, int colVal, int linVal){
        for (int i = 1; i < 8; i++) {
            if(reiCol - (i * colVal) == coluna && reiLin - (i * linVal) == linha){
                break;
            }
            Peca peca = tabuleiro.getPeca(reiCol - (i * colVal), reiLin - (i * linVal));
            if (peca != null && peca != tabuleiro.selectedPeca){
                if (!tabuleiro.mesmoTime(peca, rei) && (peca.nome.equals("Bispo") || peca.nome.equals("Rainha"))){
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean hitByCavalo(int coluna, int linha, Peca rei, int reiCol, int reiLin){
        return  checkCavalo(tabuleiro.getPeca(reiCol - 1, reiLin - 2), rei, coluna, linha) ||
                checkCavalo(tabuleiro.getPeca(reiCol + 1, reiLin - 2), rei, coluna, linha) ||
                checkCavalo(tabuleiro.getPeca(reiCol + 2, reiLin - 1), rei, coluna, linha) ||
                checkCavalo(tabuleiro.getPeca(reiCol + 2, reiLin + 1), rei, coluna, linha) ||
                checkCavalo(tabuleiro.getPeca(reiCol + 1, reiLin + 2), rei, coluna, linha) ||
                checkCavalo(tabuleiro.getPeca(reiCol - 1, reiLin + 2), rei, coluna, linha) ||
                checkCavalo(tabuleiro.getPeca(reiCol - 2, reiLin + 1), rei, coluna, linha) ||
                checkCavalo(tabuleiro.getPeca(reiCol - 2, reiLin - 1), rei, coluna, linha);
    }

    private boolean checkCavalo(Peca p, Peca r, int coluna, int linha){
        return p != null && !tabuleiro.mesmoTime(p, r) && p.nome.equals("Cavalo") && !(p.coluna == coluna && p.linha == linha);
    }


    private boolean hitByRei(Peca rei, int reiCol, int reiLin){
        return  checkRei(tabuleiro.getPeca(reiCol - 1, reiLin - 1), rei) ||
                checkRei(tabuleiro.getPeca(reiCol + 1, reiLin - 1), rei) ||
                checkRei(tabuleiro.getPeca(reiCol, reiLin - 1), rei) ||
                checkRei(tabuleiro.getPeca(reiCol - 1, reiLin), rei) ||
                checkRei(tabuleiro.getPeca(reiCol + 1, reiLin), rei) ||
                checkRei(tabuleiro.getPeca(reiCol - 1, reiLin + 1), rei) ||
                checkRei(tabuleiro.getPeca(reiCol + 1, reiLin + 1), rei) ||
                checkRei(tabuleiro.getPeca(reiCol, reiLin + 1), rei);
    }

    private boolean checkRei(Peca p, Peca r){
        return p != null && tabuleiro.mesmoTime(p, r) && p.nome.equals("Rei");
    }

    private boolean hitByPeao(int coluna, int linha, Peca rei, int reiCol, int reiLin){
        int colorVal = rei.ehBranco ? -1 : 1;
        return  checkPeao(tabuleiro.getPeca(reiCol + 1, reiLin + colorVal), rei, coluna, linha) ||
                checkPeao(tabuleiro.getPeca(reiCol - 1, reiLin + colorVal), rei, coluna, linha);
    }

    private boolean checkPeao(Peca p, Peca r, int coluna, int linha){
        return p != null && !tabuleiro.mesmoTime(p, r) && p.nome.equals("Peao") && !(p.coluna == coluna && p.linha == linha);
    }

}
