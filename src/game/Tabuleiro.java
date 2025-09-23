package game;

import pecas.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import util.classes.FabricadorPecas;
import util.classes.PainelScore;

public class Tabuleiro extends JPanel {

    public int tileSize = 85;
    private PainelScore painelScore;

    int colunas = 8;
    int linhas = 8;

    public ArrayList<Peca> listaPecas = new ArrayList<>();

    // peça selecionada para mover
    public Peca selectedPeca;

    Input input = new Input(this);

    XequeScanner checkScanner = new XequeScanner(this);

    public int enPassantTile = -1;

    // === CONTROLE DE TURNO ===
    // true = vez das brancas; false = vez das pretas
    private boolean turnoBrancas = true;
    public boolean isTurnoBrancas() { return turnoBrancas; }

    public Tabuleiro() {
        this.setPreferredSize(new Dimension(colunas * tileSize, linhas * tileSize));
        adicionarPecas();

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        if (painelScore != null) painelScore.atualizaPlacar();
    }

    // pegar a peça
    public Peca getPeca(int coluna, int linha) {
        for (Peca peca : listaPecas) {
            if (peca.coluna == coluna && peca.linha == linha) {
                return peca;
            }
        }
        return null;
    }

    // Aplica movimento respeitando turno e validade
    public void makeMove(Move move) {
        if (move == null || move.peca == null) return;

        // 1) Checa se é a vez da cor da peça
        if (move.peca.ehBranco != turnoBrancas) {
            return;
        }

        // 2) Checa se o movimento é válido (alcance, colisão, não deixa rei em xeque, etc.)
        if (!isValidMove(move)) {
            return;
        }

        // 3) Aplica o movimento de fato
        if (move.peca.nome.equals("Peao")) {
            movePeao(move);
        } else {
            // guarda coluna antiga para detectar roque
            int colAntiga = move.peca.coluna;

            // move a peça
            move.peca.coluna = move.colNova;
            move.peca.linha  = move.linNova;
            move.peca.xPos   = move.colNova * tileSize;
            move.peca.yPos   = move.linNova * tileSize;

            // captura (se houver) e flags
            move.peca.ehPrimeiroMovimento = false;
            capturar(move.capturar, true);
            enPassantTile = -1;

            // --- ROQUE: rei andou 2 colunas? então move a torre correspondente ---
            if (move.peca instanceof Rei && Math.abs(move.colNova - colAntiga) == 2) {
                int dir = (move.colNova - colAntiga) > 0 ? 1 : -1; // +1: roque pequeno | -1: roque grande
                int torreColOrigem = (dir == 1) ? 7 : 0;
                Peca torre = getPeca(torreColOrigem, move.linNova);
                if (torre != null && "Torre".equals(torre.nome) && torre.ehBranco == move.peca.ehBranco) {
                    int novaColTorre = move.colNova - dir; // f-file (5) no pequeno, d-file (3) no grande
                    torre.coluna = novaColTorre;
                    torre.linha  = move.linNova;
                    torre.xPos   = novaColTorre * tileSize;
                    torre.yPos   = move.linNova  * tileSize;
                    torre.ehPrimeiroMovimento = false;
                }
            }
        }

        // atualiza placar (pontuação e turno)
        if (painelScore != null) painelScore.atualizaPlacar();

        repaint();

        // 4) Alterna o turno apenas após aplicar com sucesso
        turnoBrancas = !turnoBrancas;

        boolean vezBrancas = turnoBrancas;

        if (checkScanner.estaEmXequeMate(vezBrancas)) {
            String vencedor = vezBrancas ? "Pretas" : "Brancas";
            int resp = JOptionPane.showConfirmDialog(this,
                    "Xeque-mate! Vencedor: " + vencedor + "\nDeseja reiniciar?",
                    "Fim de jogo", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                reiniciar();
            }
        } else if (checkScanner.estaEmAfogamento(vezBrancas)) {
            int resp = JOptionPane.showConfirmDialog(this,
                    "Empate por afogamento!\nDeseja reiniciar?",
                    "Fim de jogo", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                reiniciar();
            }
        }

        if (painelScore != null) painelScore.atualizaPlacar();
    }

    private void movePeao(Move move) {
        // en passant
        int colorIndex = move.peca.ehBranco ? 1 : -1;

        if (getTileNum(move.colNova, move.linNova) == enPassantTile) {
            move.capturar = getPeca(move.colNova, move.linNova + colorIndex);
        }
        if (Math.abs(move.peca.linha - move.linNova) == 2) {
            enPassantTile = getTileNum(move.colNova, move.linNova + colorIndex);
        } else {
            enPassantTile = -1;
        }

        // promoções
        int alvoPromo = move.peca.ehBranco ? 0 : 7;
        if (move.linNova == alvoPromo) {
            promovePeao(move);
        }

        move.peca.coluna = move.colNova;
        move.peca.linha  = move.linNova;
        move.peca.xPos   = move.colNova * tileSize;
        move.peca.yPos   = move.linNova * tileSize;

        move.peca.ehPrimeiroMovimento = false;
        capturar(move.capturar, true);
    }

    private void promovePeao(Move move) {
        String[] options = {"Rainha", "Torre", "Bispo", "Cavalo"};

        int escolha = JOptionPane.showOptionDialog(
                this,
                "Escolha a peça para promoção:",
                "Promoção de Peão",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        String tipo = (escolha >= 0) ? options[escolha] : "Rainha";

        if (move.capturar != null) {
            capturar(move.capturar, false);
            if (painelScore != null) painelScore.atualizaPlacar();
        }

        Peca promo = FabricadorPecas.promocaoPeao(tipo, this, move.linNova, move.colNova, move.peca.ehBranco);

        // ajustar posição visual da peça promovida
        promo.xPos = move.colNova * tileSize;
        promo.yPos = move.linNova * tileSize;
        promo.ehPrimeiroMovimento = false;

        listaPecas.add(promo);
        capturar(move.peca, false);
    }

    public void setPainelScore(PainelScore painelScore) {
        this.painelScore = painelScore;
        if (this.painelScore != null) this.painelScore.atualizaPlacar();
    }

    public void capturar(Peca peca, boolean contabilizar) {
        if (peca == null) return;

        if (contabilizar && painelScore != null) {
            painelScore.registrarCaptura(peca);
        }

        listaPecas.remove(peca);

        if (!contabilizar && painelScore != null) {
            painelScore.atualizaPlacar();
        }
    }

    public boolean isValidMove(Move move) {
        if (mesmoTime(move.peca, move.capturar)) {
            return false;
        }
        if (!move.peca.isValidMovement(move.colNova, move.linNova)) {
            return false;
        }
        if (move.peca.moveCollidesWithPiece(move.colNova, move.linNova)) {
            return false;
        }
        if (checkScanner.reiEhAtacado(move)) {
            return false;
        }
        return true;
    }

    public boolean mesmoTime(Peca p1, Peca p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.ehBranco == p2.ehBranco;
    }

    public int getTileNum(int coluna, int linha) {
        return linha * linhas + coluna;
    }

    Peca achaRei(boolean ehBranco) {
        for (Peca peca : listaPecas) {
            if (ehBranco == peca.ehBranco && peca.nome.equals("Rei")) {
                return peca;
            }
        }
        return null;
    }

    public void adicionarPecas() {
        listaPecas.addAll(FabricadorPecas.criarPecas(this));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // tabuleiro
        for (int c = 0; c < colunas; c++) {
            for (int l = 0; l < linhas; l++) {
                // agora A1 (coluna 0, linha 7) será escuro
                g2d.setColor((c + l) % 2 == 0 ? new Color(255, 201, 0) : new Color(255, 129, 0));
                g2d.fillRect(c * tileSize, l * tileSize, tileSize, tileSize);
            }
        }


        // realce de casas possíveis apenas se a peça selecionada for da cor do turno
        if (selectedPeca != null && selectedPeca.ehBranco == turnoBrancas) {
            for (int l = 0; l < linhas; l++) {
                for (int c = 0; c < colunas; c++) {
                    if (isValidMove(new Move(this, selectedPeca, c, l))) {
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(c * tileSize, l * tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        // peças
        for (Peca peca : listaPecas) {
            peca.paint(g2d);
        }
    }

    public void reiniciar() {
        // limpa peças e adiciona novamente
        listaPecas.clear();
        adicionarPecas();

        // zera estados auxiliares
        enPassantTile = -1;
        selectedPeca = null;
        turnoBrancas = true; // brancas começam

        // atualiza placar
        if (painelScore != null) {
            painelScore.atualizaPlacar();
        }

        repaint();
    }

}