package util.classes;

import game.Tabuleiro;
import pecas.Peca;

import javax.swing.*;
import java.awt.*;

public class PainelScore extends JPanel {
    private Tabuleiro tabuleiro;
    private Pontuacao pontuacao;
    private JLabel titulo;
    private JLabel labelBrancas;
    private JLabel labelPretas;
    private JLabel labelTurn;

    public PainelScore(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
        this.pontuacao = new Pontuacao();

        // Configurações do painel
        setPreferredSize(new Dimension(160, 120));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Título
        titulo = new JLabel("Placar");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label de turno
        labelTurn = new JLabel("Vez: Brancas");
        labelTurn.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelTurn.setForeground(Color.WHITE);
        labelTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTurn.setOpaque(false);

        // Labels de pontuação
        labelBrancas = new JLabel("Brancas: 0");
        labelBrancas.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelBrancas.setForeground(Color.WHITE);
        labelBrancas.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelPretas = new JLabel("Pretas: 0");
        labelPretas.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelPretas.setForeground(Color.WHITE);
        labelPretas.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ordem no painel
        add(titulo);
        add(Box.createRigidArea(new Dimension(0, 6)));
        add(labelTurn);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(labelBrancas);
        add(labelPretas);
    }

    public void registrarCaptura(Peca p) {
        if (p == null) return;
        pontuacao.registrarCaptura(p);
        atualizaPlacar();
    }

    public void atualizaPlacar() {
        int pontosQueBrancasTem = pontuacao.getPontosCapturadosPorBrancas();
        int pontosQuePretasTem  = pontuacao.getPontosCapturadosPorPretas();

        labelBrancas.setText("Brancas: " + pontosQueBrancasTem);
        labelPretas.setText("Pretas: " + pontosQuePretasTem);

        if (tabuleiro != null) {
            labelTurn.setText("Vez: " + (tabuleiro.isTurnoBrancas() ? "Brancas" : "Pretas"));
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Color fundo = new Color(0, 0, 0, 180);
        g2.setColor(fundo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

        g2.dispose();
        super.paintComponent(g);
    }
}