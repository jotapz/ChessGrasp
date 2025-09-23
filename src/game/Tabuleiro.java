package game;

import pecas.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import util.classes.FabricadorPecas;
import util.classes.PainelScore;

public class Tabuleiro extends JPanel{

    public int tileSize = 85;
    private PainelScore painelScore;

    int colunas = 8;
    int linhas = 8;

    public ArrayList<Peca> listaPecas= new ArrayList<>();

    // peça selecionada para mover
    public Peca selectedPeca;

    Input input = new Input(this);
    private Tabuleiro tabuleiro;

    public int enPassantTile = -1;

    public Tabuleiro(){

        this.setPreferredSize(new Dimension(colunas * tileSize, linhas * tileSize));
        adicionarPecas();

        this.addMouseListener(input);
        this.addMouseMotionListener(input);
    }

    // pegar a peça
    public Peca getPeca(int coluna, int linha) {

        for (Peca peca : listaPecas){
            if (peca.coluna == coluna && peca.linha == linha){
                return peca;
            }
        }

        return null;
    }

    public void makeMove(Move move){
        if (move.peca.nome.equals("Peao")){
            movePeao(move);
        } else {
            move.peca.coluna = move.colNova;
            move.peca.linha = move.linNova;
            move.peca.xPos = move.colNova * tileSize;
            move.peca.yPos = move.linNova * tileSize;

            move.peca.ehPrimeiroMovimento = false;
            capturar(move.capturar, true);
        }
        if (painelScore != null) painelScore.atualizaPlacar();
        repaint(); // atualiza visual do tabuleiro
    }

    private void movePeao(Move move) {
        // en passant
        int colorIndex = move.peca.ehBranco ? 1 : -1;

        if (getTileNum(move.colNova, move.linNova) == enPassantTile){
            move.capturar = getPeca(move.colNova, move.linNova + colorIndex);
        }
        if (Math.abs(move.peca.linha - move.linNova) == 2){
            enPassantTile = getTileNum(move.colNova, move.linNova + colorIndex);
        } else{
            enPassantTile = -1;
        }


        // promoções
        colorIndex = move.peca.ehBranco ? 0 : 7;
        if (move.linNova == colorIndex) {
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

        //ajustar posição visual da peça promovida
        promo.xPos = move.colNova * tileSize;
        promo.yPos = move.linNova * tileSize;
        promo.ehPrimeiroMovimento = false;

        listaPecas.add(promo);
        capturar(move.peca, false);

    }

    public void setPainelScore(PainelScore painelScore){
        this.painelScore = painelScore;
        if (this.painelScore != null) this.painelScore.atualizaPlacar();
    }

    public void capturar(Peca peca, boolean contabilizar){
        if (peca == null) return;

        // se for contabilizar (captura real), registra no painel/pontuação
        if (contabilizar && painelScore != null) {
            painelScore.registrarCaptura(peca);
        }

        // remove do tabuleiro (sempre)
        listaPecas.remove(peca);

        // atualiza placar caso não tenha registrado acima (ou apenas força atualização)
        if (!contabilizar && painelScore != null) {
            painelScore.atualizaPlacar();
        }

    }

    public boolean isValidMove(Move move){
        if (mesmoTime(move.peca, move.capturar)){
            return false;
        }
       if (!move.peca.isValidMovement(move.colNova, move.linNova)) {
           return false;
        }

       if(move.peca.moveCollidesWithPiece(move.colNova, move.linNova)) {
           return false;
       }

        return true;
    }

    public boolean mesmoTime(Peca p1, Peca p2) {
        if (p1 == null || p2 == null){
            return false;
        }
        return p1.ehBranco == p2.ehBranco;
    }

    public int getTileNum(int coluna, int linha) {
        return linha * linhas + coluna;
    }


    public void adicionarPecas() {
        listaPecas.addAll(FabricadorPecas.criarPecas(this));

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int c = 0; c < colunas; c++) {
            for (int l = 0; l < linhas; l++) {
                g2d.setColor((c + l) % 2 == 0 ? new Color(255, 129, 0) : new Color(255, 201, 0));
                g2d.fillRect(c * tileSize, l * tileSize, tileSize, tileSize);
            }
        }

        if (selectedPeca != null)
            for(int l = 0; l < linhas; l++)
                for(int c = 0; c < colunas; c++) {
                    if (isValidMove(new Move(this, selectedPeca, c, l))) {
                        g2d.setColor(new Color(68,180,57, 190));
                        g2d.fillRect(c * tileSize, l * tileSize, tileSize, tileSize);
                    }
                }


        for (Peca peca: listaPecas){
            peca.paint(g2d);
        }
    }


}
