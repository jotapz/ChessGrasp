package game;

import pecas.*;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import util.classes.FabricadorPecas;
import util.classes.PainelScore;
import game.movimento.Casa;
import game.movimento.Movimento;

public class Tabuleiro extends JPanel {


    private void loadPositionFromFEN() {
    }
    public String fenStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public final String fenTest = "r3k2r/8/8/8/2Pp4/8/8/R3K2R b KQkq c3 0 1";
    public final String myFEN = "r3k2r/8/8/8/2Pp4/8/8/R3K2R b w - - 0 1";

    public int tileSize = 85;
    private PainelScore painelScore;

    int colunas = 8;
    int linhas = 8;

    public ArrayList<Peca> listaPecas = new ArrayList<>();
    private List<Movimento> historicoDeMovimentos;

    // peça selecionada para mover
    public Peca selectedPeca;

    Input input = new Input(this);

    XequeScanner checkScanner = new XequeScanner(this);

    public int enPassantTile = -1;

    // === CONTROLE DE TURNO ===
    // true = vez das brancas; false = vez das pretas
    private boolean turnoBrancas = true;

    public boolean isTurnoBrancas() {
        return turnoBrancas;
    }

    public Tabuleiro() {
        this.setPreferredSize(new Dimension(colunas * tileSize, linhas * tileSize));
        this.historicoDeMovimentos = new ArrayList<>();
        adicionarPecas();

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        loadPositionFromFEN();

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


    private void moveRei(Move move) {
    }
    // Aplica game.movimento respeitando turno e validade
    public void makeMove(Move move) {
        if (move == null || move.peca == null) return;

        // 1) Checa se é a vez da cor da peça
        if (move.peca.ehBranco != turnoBrancas) {
            return;
        }

        // 2) Checa se o game.movimento é válido (alcance, colisão, não deixa rei em xeque, etc.)
        if (!isValidMove(move)) {
            return;
        }

        Casa casaOrigem = new Casa(move.peca.linha, move.peca.coluna);
        Casa casaDestino = new Casa(move.linNova, move.colNova);
        Peca pecaCapturada = move.capturar;
        Movimento registro = new Movimento(move.peca, casaOrigem, casaDestino, pecaCapturada);
        this.historicoDeMovimentos.add(registro);

        // Linha de teste para você ver o resultado no console
        System.out.println("Jogada registrada: " + registro);

        if (painelScore != null) {
            painelScore.atualizarHistorico(historicoDeMovimentos);
        }

        // 3) Aplica o game.movimento de fato
        if (move.peca.nome.equals("Peao")) {
            movePeao(move);
        } else {
            enPassantTile = -1;
        }
        if (move.peca.nome.equals("Rei")) {
            moveRei((move)); }

            {    // guarda coluna antiga para detectar roque
            int colAntiga = move.peca.coluna;

            // move a peça
            move.peca.coluna = move.colNova;
            move.peca.linha = move.linNova;
            move.peca.xPos = move.colNova * tileSize;
            move.peca.yPos = move.linNova * tileSize;

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
                    torre.linha = move.linNova;
                    torre.xPos = novaColTorre * tileSize;
                    torre.yPos = move.linNova * tileSize;
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
        move.peca.linha = move.linNova;
        move.peca.xPos = move.colNova * tileSize;
        move.peca.yPos = move.linNova * tileSize;

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

    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    public void loadPositionFromFEn(String fenString) {
        listaPecas.clear();
        String[] parts = fenString.split(" ");

        //organizar peças
        String position = parts[0];
        int linha = 0;
        int coluna = 0;
        for (int i = 0; i < position.length(); i++) {
            char ch = position.charAt(i);

            if (ch == '/') {
                linha++;
                coluna = 0;
            } else if (Character.isDigit(ch)) {
                coluna += Character.getNumericValue(ch);
            } else {
                boolean ehBranco = Character.isUpperCase(ch);
                char pecaChar = Character.toLowerCase(ch);
                switch (pecaChar) {
                    case 'r': // r = Rook (Torre)
                        listaPecas.add(new Torre(this, coluna, linha, ehBranco));
                        break;
                    case 'n': // n = Knight (Cavalo)
                        listaPecas.add(new Cavalo(this, coluna, linha, ehBranco));
                        break;
                    case 'b': // b = Bishop (Bispo)
                        listaPecas.add(new Bispo(this, coluna, linha, ehBranco));
                        break;
                    case 'q': // q = Queen (Rainha)
                        listaPecas.add(new Rainha(this, coluna, linha, ehBranco));
                        break;
                    case 'k': // k = King (Rei)
                        listaPecas.add(new Rei(this, coluna, linha, ehBranco));
                        break;
                    case 'p': // p = Pawn (Peão)
                        listaPecas.add(new Peao(this, coluna, linha, ehBranco));
                        break;
                }
                coluna++;
            }
        }

        //cor para mover
        boolean ehBrancoparaMover = parts[1].equals("w");

        //roque
        Peca bqr = getPeca(0, 0);
        if (bqr instanceof Torre) {
            bqr.ehPrimeiroMovimento = parts[2].contains("q");
        }
        Peca bkr = getPeca(7, 0);
        if (bkr instanceof Torre) {
            bkr.ehPrimeiroMovimento = parts[2].contains("k");
        }
        Peca wqr = getPeca(0, 7);
        if (wqr instanceof Torre) {
            wqr.ehPrimeiroMovimento = parts[2].contains("Q");
        }
        Peca wkr = getPeca(7, 7);
        if (wkr instanceof Torre) {
            wkr.ehPrimeiroMovimento = parts[2].contains("K");
        }
//casa en passant
        if (parts[3].equals("-")) {
            enPassantTile = -1;
        } else {
         enPassantTile = (7 - (parts[3].charAt(1) - '1')) * 8 + (parts[3].charAt(0) - 'a');
        }
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
        historicoDeMovimentos.clear();
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