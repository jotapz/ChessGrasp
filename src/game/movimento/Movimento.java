package game.movimento;

import pecas.Peca;

public class Movimento {
    private Peca pecaMovida;
    private Casa casaOrigem;
    private Casa casaDestino;
    private Peca pecaCapturada;
    private String pecaPromovida;

    public Movimento(Peca pecaMovida, Casa casaOrigem, Casa casaDestino, Peca pecaCapturada) {
        this.pecaMovida = pecaMovida;
        this.casaOrigem = casaOrigem;
        this.casaDestino = casaDestino;
        this.pecaCapturada = pecaCapturada;
    }

    public void setPecaPromovida(String tipoPeca) {
        this.pecaPromovida = tipoPeca;
    }

    public Peca getPecaMovida() {
        return pecaMovida;
    }

    public Casa getCasaOrigem() {
        return casaOrigem;
    }

    public Casa getCasaDestino() {
        return casaDestino;
    }

    public Peca getPecaCapturada() {
        return pecaCapturada;
    }

    public boolean ehCaptura() {
        return pecaCapturada != null;
    }

    @Override
    public String toString() {
        // Começa com a descrição padrão do movimento
        String descricao = pecaMovida.nome + " de " + casaOrigem + " para " + casaDestino;

        // Adiciona a informação da captura
        if (pecaCapturada != null) {
            descricao += " capturando " + pecaCapturada.nome;
        }

        // Adiciona a informação da promoção
        if (pecaPromovida != null) {
            descricao += " (promove para " + pecaPromovida + ")";
        }

        return descricao;
    }
}