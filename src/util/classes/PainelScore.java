package util.classes;

import game.Tabuleiro;
import pecas.Peca;
import game.movimento.Movimento;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelScore extends JPanel {
    private Tabuleiro tabuleiro;
    private Pontuacao pontuacao;
    private JLabel titulo;
    private JLabel labelBrancas;
    private JLabel labelPretas;
    private JLabel labelTurn;


    private JLabel historicoTitulo;
    private JTextArea areaHistorico;
    private JScrollPane scrollHistorico;

    public PainelScore(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
        this.pontuacao = new Pontuacao();

        // Configurações do painel
        setPreferredSize(new Dimension(200, 500));
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

        historicoTitulo = new JLabel("Histórico");
        historicoTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        historicoTitulo.setForeground(Color.WHITE);
        historicoTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        areaHistorico = new JTextArea();
        areaHistorico.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaHistorico.setForeground(Color.WHITE);
        areaHistorico.setEditable(false);
        areaHistorico.setOpaque(false); // Para manter seu fundo transparente

        scrollHistorico = new JScrollPane(areaHistorico);
        scrollHistorico.setOpaque(false); // Para o ScrollPane ser transparente
        scrollHistorico.getViewport().setOpaque(false); // E a área interna também
        scrollHistorico.setBorder(null); // Remove a borda padrão

        // ordem no painel
        add(titulo);
        add(Box.createRigidArea(new Dimension(0, 6)));
        add(labelTurn);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(labelBrancas);
        add(labelPretas);
        add(Box.createRigidArea(new Dimension(0, 20))); // Espaço maior
        add(historicoTitulo);
        add(Box.createRigidArea(new Dimension(0, 6)));
        add(scrollHistorico);
    }

    public void atualizarHistorico(List<Movimento> historico) {
        StringBuilder texto = new StringBuilder();
        int numeroJogada = 1;
        for (int i = 0; i < historico.size(); i++) {
            Movimento mov = historico.get(i);
            if (mov.getPecaMovida().ehBranco) {
                texto.append(String.format("%2d. %-18s", numeroJogada, mov.toString()));
                numeroJogada++;
            } else {
                texto.append(String.format("%-18s\n", mov.toString()));
            }
        }
        areaHistorico.setText(texto.toString());

        // Auto-scroll para o final
        areaHistorico.setCaretPosition(areaHistorico.getDocument().getLength());
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