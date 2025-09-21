package pecas;

import game.Tabuleiro;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Peca {
    public Integer linha, coluna; // Cada peça terá uma coluna e linha correspondente
    public Integer xPos, yPos;    // Posição da peça

    public boolean ehBranco;
    public String nome;

    public Integer valor;

    public boolean ehPrimeiroMovimento = true;
    BufferedImage sheet;
    {
        try{
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("res/pecas.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    protected int sheetScale = sheet.getWidth()/6; // Divide a imagem

    Image sprite;

    Tabuleiro tabuleiro;

    public Peca(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
    }

    public boolean isValidMovement(int colunas, int linhas) {
        return true;
    }
    public boolean moveCollidesWithPiece(int colunas, int linhas) {return false;}


    public void paint(Graphics g2d){
        g2d.drawImage(sprite, xPos, yPos, null);
    }




}
