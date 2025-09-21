package game;

import pecas.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;


public class Tabuleiro extends JPanel{

    public int tileSize = 85;

    int coluna = 8;
    int linha = 8;

    ArrayList<Peca> listaPecas= new ArrayList<>();

    // peça selecionada para mover
    public Peca selectedPeca;

    Input input = new Input(this);
    private Tabuleiro tabuleiro;

    public Tabuleiro(){

        this.setPreferredSize(new Dimension(coluna * tileSize, linha * tileSize));
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
        move.peca.coluna = move.colNova;
        move.peca.linha = move.linNova;
        move.peca.xPos = move.colNova * tileSize;
        move.peca.yPos = move.linNova * tileSize;

        move.peca.ehPrimeiroMovimento = false;
        capturar(move);
    }

    public void capturar(Move move){
        listaPecas.remove(move.capturar);
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


    public void adicionarPecas(){

        // Pretas:
        // Rei
        listaPecas.add(new Rei(this, 0, 4, false));


        // Rainha
        listaPecas.add(new Rainha(this, 0, 3, false));

        //Bispos
        listaPecas.add(new Bispo(this, 0, 2, false));
        listaPecas.add(new Bispo(this, 0, 5, false));

        // Torres
        listaPecas.add(new Torre(this, 0, 0, false));
        listaPecas.add(new Torre(this, 0, 7, false));

        // Cavalos
        listaPecas.add(new Cavalo(this, 0, 1, false));
        listaPecas.add(new Cavalo(this, 0, 6, false));

        // Peoes
        listaPecas.add(new Peao(this, 1, 0, false));
        listaPecas.add(new Peao(this, 1, 1, false));
        listaPecas.add(new Peao(this, 1, 2, false));
        listaPecas.add(new Peao(this, 1, 3, false));
        listaPecas.add(new Peao(this, 1, 4, false));
        listaPecas.add(new Peao(this, 1, 5, false));
        listaPecas.add(new Peao(this, 1, 6, false));
        listaPecas.add(new Peao(this, 1, 7, false));



        // Brancas:

        // Rei
        listaPecas.add(new Rei(this, 7, 4, true));


        // Rainha
        listaPecas.add(new Rainha(this, 7, 3, true));

        //Bispos
        listaPecas.add(new Bispo(this, 7, 2, true));
        listaPecas.add(new Bispo(this, 7, 5, true));

        // Torres
        listaPecas.add(new Torre(this, 7, 0, true));
        listaPecas.add(new Torre(this, 7, 7, true));

        // Cavalos
        listaPecas.add(new Cavalo(this, 7, 1, true));
        listaPecas.add(new Cavalo(this, 7, 6, true));

        // Peoes
        listaPecas.add(new Peao(this, 6, 0, true));
        listaPecas.add(new Peao(this, 6, 1, true));
        listaPecas.add(new Peao(this, 6, 2, true));
        listaPecas.add(new Peao(this, 6, 3, true));
        listaPecas.add(new Peao(this, 6, 4, true));
        listaPecas.add(new Peao(this, 6, 5, true));
        listaPecas.add(new Peao(this, 6, 6, true));
        listaPecas.add(new Peao(this, 6, 7, true));
    }


    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int c = 0; c < coluna; c++)
        for (int l = 0; l < linha; l++) {
            g2d.setColor((c+l) % 2 == 0 ? new Color(255, 129, 0) : new Color(255, 201, 0));
            g2d.fillRect(c * tileSize,  l * tileSize, tileSize, tileSize);
        }

        if (selectedPeca != null)
            for(int l = 0; l < linha; l++)
                for(int c = 0; c < coluna; c++) {
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
