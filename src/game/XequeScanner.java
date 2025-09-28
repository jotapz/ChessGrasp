package game;

import pecas.Peca;

public class XequeScanner {
    Tabuleiro tabuleiro;

    public XequeScanner(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
    }


    /** Está em xeque o lado (ehBranco) na posição atual */
    public boolean estaEmXeque(boolean ehBranco) {
        Peca rei = tabuleiro.achaRei(ehBranco);
        if (rei == null) return false; // segurança
        int reiCol = rei.coluna;
        int reiLin = rei.linha;

        // IMPORTANTÍSSIMO: garantir que não há peça "selecionada" interferindo
        Peca sel = tabuleiro.selectedPeca;
        tabuleiro.selectedPeca = null;

        boolean atacado =
                hitByTorre(-1, -1, rei, reiCol, reiLin, 0, 1)   // cima
                        || hitByTorre(-1, -1, rei, reiCol, reiLin, 1, 0)   // direita
                        || hitByTorre(-1, -1, rei, reiCol, reiLin, 0, -1)  // baixo
                        || hitByTorre(-1, -1, rei, reiCol, reiLin, -1, 0)  // esquerda
                        || hitByBispo(-1, -1, rei, reiCol, reiLin, -1, -1) // esq-cima
                        || hitByBispo(-1, -1, rei, reiCol, reiLin,  1, -1) // dir-cima
                        || hitByBispo(-1, -1, rei, reiCol, reiLin,  1,  1) // dir-baixo
                        || hitByBispo(-1, -1, rei, reiCol, reiLin, -1,  1) // esq-baixo
                        || hitByCavalo(-1, -1, rei, reiCol, reiLin)
                        || hitByPeao(-1, -1, rei, reiCol, reiLin)
                        || hitByRei(rei, reiCol, reiLin);

        tabuleiro.selectedPeca = sel;
        return atacado;
    }

    /** Existe ALGUM game.movimento legal para o lado (ehBranco)? */
    public boolean temMovimentoLegal(boolean ehBranco) {
        // para cada peça do lado a jogar
        for (Peca p : tabuleiro.listaPecas) {
            if (p.ehBranco != ehBranco) continue;

            // varrer todo o tabuleiro como destino
            for (int l = 0; l < 8; l++) {
                for (int c = 0; c < 8; c++) {
                    Move m = new Move(tabuleiro, p, c, l);

                    // IMPORTANTÍSSIMO: o isValidMove usa selectedPeca e reiEhAtacado(move),
                    // então setamos a selectedPeca temporariamente para simular corretamente.
                    Peca selAnt = tabuleiro.selectedPeca;
                    tabuleiro.selectedPeca = p;
                    boolean valido = tabuleiro.isValidMove(m);
                    tabuleiro.selectedPeca = selAnt;

                    if (valido) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Xeque-mate ao lado (ehBranco) na posição atual */
    public boolean estaEmXequeMate(boolean ehBranco) {
        return estaEmXeque(ehBranco) && !temMovimentoLegal(ehBranco);
    }

    /** Afogamento (stalemate) ao lado (ehBranco) na posição atual */
    public boolean estaEmAfogamento(boolean ehBranco) {
        return !estaEmXeque(ehBranco) && !temMovimentoLegal(ehBranco);
    }

    // === Verificação de xeque durante um game.movimento hipotético ===

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

    // === Helpers privados (como você já tinha) ===

    private boolean hitByTorre(int coluna, int linha, Peca rei, int reiCol, int reiLin, int colVal, int linVal){
        for (int i = 1; i < 8; i++) {
            if (tabuleiro.selectedPeca != null && !tabuleiro.selectedPeca.nome.equals("Rei")) {
                if (reiCol + (i * colVal) == coluna && reiLin + (i * linVal) == linha) {
                    break;
                }
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

    private boolean hitByBispo(int coluna, int linha, Peca rei, int reiCol, int reiLin, int colVal, int linVal){
        for (int i = 1; i < 8; i++) {
            if (tabuleiro.selectedPeca != null && !tabuleiro.selectedPeca.nome.equals("Rei")) {
                if (reiCol - (i * colVal) == coluna && reiLin - (i * linVal) == linha) {
                    break;
                }
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
        return  xequeCavalo(tabuleiro.getPeca(reiCol - 1, reiLin - 2), rei, coluna, linha) ||
                xequeCavalo(tabuleiro.getPeca(reiCol + 1, reiLin - 2), rei, coluna, linha) ||
                xequeCavalo(tabuleiro.getPeca(reiCol + 2, reiLin - 1), rei, coluna, linha) ||
                xequeCavalo(tabuleiro.getPeca(reiCol + 2, reiLin + 1), rei, coluna, linha) ||
                xequeCavalo(tabuleiro.getPeca(reiCol + 1, reiLin + 2), rei, coluna, linha) ||
                xequeCavalo(tabuleiro.getPeca(reiCol - 1, reiLin + 2), rei, coluna, linha) ||
                xequeCavalo(tabuleiro.getPeca(reiCol - 2, reiLin + 1), rei, coluna, linha) ||
                xequeCavalo(tabuleiro.getPeca(reiCol - 2, reiLin - 1), rei, coluna, linha);
    }

    private boolean xequeCavalo(Peca p, Peca r, int coluna, int linha){
        return p != null && !tabuleiro.mesmoTime(p, r) && p.nome.equals("Cavalo") && !(p.coluna == coluna && p.linha == linha);
    }

    private boolean hitByRei(Peca rei, int reiCol, int reiLin){
        return  xequeRei(tabuleiro.getPeca(reiCol - 1, reiLin - 1), rei) ||
                xequeRei(tabuleiro.getPeca(reiCol + 1, reiLin - 1), rei) ||
                xequeRei(tabuleiro.getPeca(reiCol, reiLin - 1), rei) ||
                xequeRei(tabuleiro.getPeca(reiCol - 1, reiLin), rei) ||
                xequeRei(tabuleiro.getPeca(reiCol + 1, reiLin), rei) ||
                xequeRei(tabuleiro.getPeca(reiCol - 1, reiLin + 1), rei) ||
                xequeRei(tabuleiro.getPeca(reiCol + 1, reiLin + 1), rei) ||
                xequeRei(tabuleiro.getPeca(reiCol, reiLin + 1), rei);
    }

    private boolean xequeRei(Peca p, Peca r){
        return p != null && !tabuleiro.mesmoTime(p, r) && p.nome.equals("Rei");
    }

    private boolean hitByPeao(int coluna, int linha, Peca rei, int reiCol, int reiLin){
        int colorVal = rei.ehBranco ? -1 : 1;
        return  xequePeao(tabuleiro.getPeca(reiCol + 1, reiLin + colorVal), rei, coluna, linha) ||
                xequePeao(tabuleiro.getPeca(reiCol - 1, reiLin + colorVal), rei, coluna, linha);
    }

    private boolean xequePeao(Peca p, Peca r, int coluna, int linha){
        return p != null && !tabuleiro.mesmoTime(p, r) && p.nome.equals("Peao") && !(p.coluna == coluna && p.linha == linha);
    }
}