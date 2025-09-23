package game;

import java.awt.*;
import javax.swing.*;

import util.classes.PainelScore;

public class App {
    public static void main(String[] args) {
        // Interface JFrame
        JFrame frame = new JFrame("Xadrez");
        frame.getContentPane().setBackground(new Color(255, 141, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Instancia o Tabuleiro e o PainelScore
        Tabuleiro tabuleiro = new Tabuleiro();
        PainelScore painelScore = new PainelScore(tabuleiro);

        // Define a referência do painelScore no tabuleiro
        tabuleiro.setPainelScore(painelScore);

        // Cria um painel principal com BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 141, 0));

        // Coloca o tabuleiro no centro
        mainPanel.add(tabuleiro, BorderLayout.CENTER);

        // Coloca o painel de score à direita
        JPanel sidePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sidePanel.setBackground(new Color(255, 141, 0)); // mesmo fundo
        sidePanel.add(painelScore);

        mainPanel.add(sidePanel, BorderLayout.EAST);

        // Adiciona no frame
        frame.add(mainPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
